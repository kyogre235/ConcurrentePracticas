/*
 * Programa que implementa un protocolo de consenso con getAndSet() [TAS] y get()
 * Este consenso está destinado a fallas, ya que TAS tiene un número de consenso de 2.
 */
import java.util.concurrent.atomic.AtomicInteger;

public class TASConsensus<T> implements ConsensusProtocol<T>{
    private T[] propose;
    private final int capacity;
    private final int FIRST=-1;
    private AtomicInteger r = new AtomicInteger(FIRST);

    public TASConsensus(int c, T init){
        capacity = c;
        propose =  (T[]) new Object[capacity];
        for (int i = 0; i < capacity; i++) {
            propose[i] = init;
        }
    }

    public T decide(T value, int me){
        propose[me]=value;
        if (r.getAndSet(me) == FIRST) {
            //System.err.println("WIN " + me);
            return propose[me];
        } else {
            //System.err.println("LOSE " + me);
            return propose[r.get()];
        }
    }

}
