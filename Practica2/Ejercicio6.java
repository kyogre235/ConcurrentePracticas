import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Ejercicio6 {
  // semaforoestaico para poder usarlo en tarea
  static Semaphore semaforo = new Semaphore(3,  true);
  
  public static void main(String[] args) {
    final int tareasMax = 6;
    ExecutorService executorTarea = Executors.newFixedThreadPool(tareasMax);

    Lock candado02 = new ReentrantLock();

    long[] duraciones = {500, 2000, 500, 3000, 3000, 3000};


    for (int i = 0; i < tareasMax; i++) {
            executorTarea.execute(new Tarea(i, duraciones[i], candado02));
        }


     executorTarea.shutdown();
        try {
            if (!executorTarea.awaitTermination(15, TimeUnit.SECONDS)) {
                executorTarea.shutdownNow();
            }
        } catch (InterruptedException e) {
            executorTarea.shutdownNow();
        }
  }
}

class Tarea implements Runnable {
    private int id;
    private long duracion;
    private Lock candado;

    public Tarea (int id, long duracion, Lock candado){
      this.id = id;
      this.duracion = duracion;
      this.candado = candado;
    }

    @Override
    public void run (){
      try{
        System.out.println("Tarea " + id + " esperando para entrar al servidor.");
        Ejercicio6.semaforo.acquire();

        try{
          if(id == 0 || id == 2){
            candado.lock();
            try{
              ejecutar();
            } finally{
              candado.unlock();
            } 
          } else {
            ejecutar();
          }
        } finally {
          Ejercicio6.semaforo.release();
        }
      } catch (InterruptedException e) {
          Thread.currentThread().interrupt();
      }
    }

    private void ejecutar() throws InterruptedException {
      System.out.println(">> Tarea " + id + " ACCEDIÓ al servidor. Duración: " + this.duracion + "ns.");
      TimeUnit.NANOSECONDS.sleep(this.duracion);
      System.out.println("<< Tarea " + id + " TERMINÓ y sale del servidor.");
    }
}
