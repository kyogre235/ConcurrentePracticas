import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Clase con el método principal que simula un lock Peterson para 4 threads, ejecutando 400 tareas.
 */
public class Main {

    /**
     * Imprime el uso del programa
     */
    private static void uso() {
        System.err.println("Se necesita una cadena como argumento para seleccionar el tipo de candado\n\t* Peterson para 4 threads: \"P\"\n\t* Bakery para 4 threads: \"B\"");
        System.err.println("Uso: javac *.java\n    java Main \"P\"\njava Main \"B\"");
        System.exit(1);
    }

    public static void main(String[] args) {
        final int threads = 4;
        final int tareas = 400;
        // final int tareas = 100000; // Más cantidad de tareas :D
        ExecutorService executorTarea = Executors.newFixedThreadPool(threads);
        CandadoLimitado lock = null;
        if (args.length != 0) {
            if (args[0].toUpperCase().equals("P"))
                lock = new Peterson4Threads();
            else if (args[0].toUpperCase().equals("B"))
                lock = new Bakery4Threads();
        }
        if (lock == null)
            uso();
        Contador contador = new Contador();
        ContadorActividades contadorThreads = new ContadorActividades(threads);

        for (int i = 0; i < tareas; i++)
            executorTarea.execute(new Tarea(i, lock, contador, contadorThreads));
        
        /* Detiene la ejecución */
        executorTarea.shutdown();

        try {
            if (!executorTarea.awaitTermination(15, TimeUnit.SECONDS))
                executorTarea.shutdownNow();
        } catch (InterruptedException e) {
            executorTarea.shutdownNow();
        }

        System.out.println("\n\nValor esperado del contador: " + tareas);
        System.out.println("Valor logrado del contador: " + contador);

        System.out.println("\nLa cantidad de veces que los diferentes threads entraron a la sección crítica son:");
        System.out.println(contadorThreads);
        
    }
}

/**
 * Clase que simula una tarea del problema principal
 */
class Tarea implements Runnable {
    
    /**
     * Identificador único
     */
    private int id;

    /**
     * Candado asignado
     */
    private CandadoLimitado lock;

    /**
     * Contador general
     */
    private Contador contador;

    /**
     * Contador por threads
     */
    private ContadorActividades contadorThreads;

    /**
     * Constructor de la clase
     * @param id Identificador único
     * @param lock Candado asignado
     * @param contador Contador general
     * @param contadorThreads Contador por threads
     */
    public Tarea (int id, CandadoLimitado lock, Contador contador, ContadorActividades contadorThreads) {
        this.id = id;
        this.lock = lock;
        this.contador = contador;
        this.contadorThreads = contadorThreads;
    }

    /**
     * Ejecuta la tarea dentro del lock brindado.
     * @see java.lang.Runnable#run() Runnable#run()
     */
    @Override
    public void run () {
        try {
            System.out.println("Tarea " + id + " en espera de entrar.");
            lock.lock();
            try {
                ejecutar();
            } finally {
                lock.unlock();
            }
        } catch (Exception e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Método que ejecuta la tarea.
     * Simula la sección crítica.
     */
    private void ejecutar() {
        System.out.println(">> Tarea " + id + " Entra a la sección crítica. Valor contador antes: " + contador);
        contador.increment();
        contadorThreads.increment();
        System.out.println("<< Tarea " + id + " Sale de la sección crítica. Valor contador después: " + contador);
    }
}