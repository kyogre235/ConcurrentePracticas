import java.util.Arrays;

public class Collect<T> implements Snapshot<T> {
  private StampedSnapH<T>[] a_table;

  public Collect(int capacity, T init){
    a_table = (StampedSnapH<T>[]) new StampedSnapH[capacity];

    for (int i = 0; i < a_table.length; i++){
      a_table[i] = new StampedSnapH<T>(init);
    }
  }

  public void update(T value){
    int me = ThreadID.get();
    T[] snap1 = this.scan();
    StampedSnapH<T> oldValue = a_table[me];
    oldValue.snap.add(snap1);
    oldValue.values.add(value);
    StampedSnapH<T> newValue = 
      new StampedSnapH<T>(oldValue.stamp+1,value, oldValue.snap, oldValue.values);
    a_table[me] = newValue;
  }

  public StampedSnapH<T>[] collect(){
    StampedSnapH<T>[] copy = (StampedSnapH<T>[]) new StampedSnapH[a_table.length];
    for (int j = 0; j < a_table.length; j++) {
      copy[j] = a_table[j];
    }
    return copy;
  }

  public T[] scan(){
    StampedValue<T>[] copy = collect();

      T[] result = (T[]) new Object[a_table.length];
      for (int j = 0; j < a_table.length; j++) {
        result[j] = copy[j].value;
      }
      return result;
    }
}
