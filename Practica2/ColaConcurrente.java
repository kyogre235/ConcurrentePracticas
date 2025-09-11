import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class ColaConcurrente{
    /* 
     * usaremos la cola secuencial y
     * simplemente le pasaremos sus metodos
     * al pool de hilos. 
     * */
    private ColaSecuencial cola;
    private ExecutorService executor;

    public ColaConcurrente(int hilos){
      /*
       *  Creamos la cola y el pool
       * */
      this.cola = new ColaSecuencial();
      this.executor = Executors.newFixedThreadPool(hilos);
    }

    public Future<Boolean> enq(String contenido){
      /*
       * el metodo execute nesesita un objeto de tipo Runnable,
       * asi que lo generamos con una lambda que nos facilita la
       * implementacion
       * */
      return executor.submit(() -> {
            System.out.println("Hilo " + Thread.currentThread().getId() + " intenta encolar: " + contenido);
            return cola.enq(contenido);
        });

      }

     public Future<String> deq (){
      return executor.submit(() -> {
         System.out.println("Hilo " + Thread.currentThread().getId() + " intenta desencolar...");
        String elem = cola.deq();
        System.out.println("Hilo " + Thread.currentThread().getId() + " logró desencolar: " + elem);
        return elem;
      });
    }
    
    public void print(){
      cola.print();
    }

    public void shutdown() {
        executor.shutdown(); // No acepta nuevas tareas y espera que las actuales terminen.
        try {
            // Espera un tiempo máximo para que terminen todas las tareas.
            if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
                executor.shutdownNow(); // Intenta forzar la detención de las tareas.
            }
        } catch (InterruptedException ex) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    public static void main(String[] args) throws InterruptedException {
      ColaConcurrente colita = new ColaConcurrente(4);
      
      //Lista para guardar todos los Futures 
      List<Future<?>> futuros = new ArrayList<>();


      // Enviamos varias tareas de enq y deq al mismo tiempo.
        futuros.add(colita.enq("A"));
        futuros.add(colita.enq("B"));
        futuros.add(colita.deq());
        futuros.add(colita.enq("C"));
        futuros.add(colita.enq("D"));
        futuros.add(colita.deq());
        futuros.add(colita.deq());
        
        for (Future<?> future : futuros) {
            try {
                Object result = future.get();
                System.out.println("Resultado de una tarea: " + result);
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
        
        colita.shutdown();

        colita.print();

    }
}


