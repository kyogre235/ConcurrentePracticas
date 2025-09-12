import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * Clase que simula una cola concurrente utilizando una pool de hilos
 * Modificaciones a firma de métodos con syncronized. (Comentado)
 */
public class ColaConcurrente {
    
    /**
     * Cola secuencial
     */
    private ColaSecuencial cola;

    /**
     * Pool de hilos
     */
    private ExecutorService executor;

    /**
     * Constructor de la clase
     * @param hilos Cantidad de hilos en la pool
     */
    public ColaConcurrente(int hilos) {

        /* Inicializamos la cola y el pool */
        this.cola = new ColaSecuencial();
        this.executor = Executors.newFixedThreadPool(hilos);
    }

    /**
     * Encolar un nodo a la cola
     * @see ColaSecuencial#enq(String) ColaSecuencial#enq(String)
     * @param contenido Contenido a encolar
     * @return Respuesta a la operación asincrónica.
     *         <code>true</code> si fue éxitosa, <code>false</code> si ocurió un error
     */
    public Future<Boolean> enq(String contenido) {
    //public synchronized Future<Boolean> enq(String contenido) {

        /*
         * El método submit necesita un objeto de tipo Runnable.
         * Lo generamos con una expresión lambda que nos facilita la
         * implementación.
         */
        return executor.submit(() -> {
            // System.out.println("Hilo " + Thread.currentThread().threadId() + " intenta encolar: " + contenido); // Java 19 en adelante
            System.out.println("Hilo " + Thread.currentThread().getId() + " intenta encolar: " + contenido); // Java 18 y algunos predecesoras
            return cola.enq(contenido);
        });
    }

    /**
     * Des-encolar un nodo de la cola
     * @see ColaSecuencial#deq() ColaSecuencial#deq()
     * @return Respuesta a la operación asincrónica.
     *         Cadena con el contenido del elemento des-encolado
     */
    public Future<String> deq () {
    // public synchronized Future<String> deq () {

        /*
         * El método submit necesita un objeto de tipo Runnable.
         * Lo generamos con una expresión lambda que nos facilita la
         * implementación.
         */
        return executor.submit(() -> {
            // System.out.println("Hilo " + Thread.currentThread().threadId() + " intenta desencolar..."); // Java +19
            System.out.println("Hilo " + Thread.currentThread().getId() + " intenta desencolar..."); // Java 19-
            String elem = cola.deq();
            // System.out.println("Hilo " + Thread.currentThread().threadId() + " logró desencolar: " + elem); // Java +19
            System.out.println("Hilo " + Thread.currentThread().getId() + " logró desencolar: " + elem); // Java 19-
            return elem;
        });
    }
    
    /**
     * Imprime el contenido de los nodos en la cola
     * @see ColaSecuencial#print() ColaSecuencial#print()
     */
    public void print() {
        cola.print();
    }

    /**
     * Detiene la ejecución de la cola.
     * Da un tiempo máximo de 60 segundos para terminar o se fuerza a terminar.
     */
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

    /**
     * Ejecución principal de la clase.
     * @param args Argumentos (No utilziados)
     * @throws InterruptedException Si sucede un error al usar la pool de hilos
     */
    public static void main(String[] args) throws InterruptedException {
        // Cola
        ColaConcurrente colita = new ColaConcurrente(4);

        // Lista para guardar todos los Futures 
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


