import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Clase con la solución al problema no. 6 planteado.
 * 
 * Problema. Supón que perteneces a un equipo de trabajo en el cual estás a
 * cargo de dar acceso a un servidor. Seis personas te pueden mandar tareas para
 * que las ejecutes en el servidor. Las tareas del hilo 0 y 2 tardan 500ns,
 * las del hilo 1 tardan 2000ns y las de los hilos 3, 4 y 5 tardan 3000ns.
 * 
 * Tienes la instrucción de no dar acceso a más de tres al mismo tiempo y de no
 * aceptar al mismo tiempo las tareas del hilo 0 y 2. Diseña e implementa una
 * solución.
 */
public class Ejercicio6 {

    /**
     * Semáforo estático
     */
    static Semaphore semaforo = new Semaphore(3,  true);

    /**
     * Método principal
     * @param args Argumentos (No utilizados)
     */
    public static void main(String[] args) {

        /**
         * Tareas máximas a ejecutar.
         * Inmutable
         */
        final int tareasMax = 6;

        /**
         * Pool de hilos
         */
        ExecutorService executorTarea = Executors.newFixedThreadPool(tareasMax);

        /**
         * Candado
         */
        Lock candado02 = new ReentrantLock();

        /**
         * Duraciones establecidas
         */
        long[] duraciones = {500, 2000, 500, 3000, 3000, 3000};

        /* Inicialización de las Tareas */
        for (int i = 0; i < tareasMax; i++) {
            executorTarea.execute(new Tarea(i, duraciones[i], candado02));
        }
        
        /* Detiene la ejecución */
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

/**
 * Clase que simula una tarea del problema principal
 */
class Tarea implements Runnable {
    
    /**
     * Identificador único
     */
    private int id;

    /**
     * Duración de la tarea en nanosegundos
     */
    private long duracion;

    /**
     * Candado asignado
     */
    private Lock candado;

    /**
     * Constructor de la clase
     * @param id Identificador único
     * @param duracion Duración en nanosegundos
     * @param candado Candado asignado
     */
    public Tarea (int id, long duracion, Lock candado) {
        this.id = id;
        this.duracion = duracion;
        this.candado = candado;
    }

    /**
     * Ejecta la tarea.
     * @see java.lang.Runnable#run() Runnable#run()
     */
    @Override
    public void run () {
        try {
            System.out.println("Tarea " + id + " esperando para entrar al servidor.");
            Ejercicio6.semaforo.acquire();

            try {
                if (id == 0 || id == 2) {
                    candado.lock();
                    try {
                        ejecutar();
                    } finally {
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

    /**
     * Método que ejecuta la tarea.
     * Simula la sección crítica.
     * @throws InterruptedException Si sucede un error, o se interrumpe la ejecución
     */
    private void ejecutar() throws InterruptedException {
        System.out.println(">> Tarea " + id + " ACCEDIÓ al servidor. Duración: " + this.duracion + "ns.");
        TimeUnit.NANOSECONDS.sleep(this.duracion);
        System.out.println("<< Tarea " + id + " TERMINÓ y sale del servidor.");
    }
}
