/**
 * Clase que representa un lock de panadero para n = 4 hilos
 */
public class Bakery4Threads implements CandadoLimitado {

    /**
     * Máximo de threads permitido
     */
    private final int MAX_THREADS = 4;

    /**
     * Identificadores de los threads
     */
    private volatile long[] threadsId = new long[MAX_THREADS];

    /**
     * Identificadores de los threads
     */
    private volatile boolean[] flags = new boolean[MAX_THREADS];

    /**
     * Identificadores de los threads
     */
    private volatile int[] labels = new int[MAX_THREADS];

    /**
     * Constructor de la clase
     * Default
     */
    public Bakery4Threads() {
        for (int i = 0; i < MAX_THREADS; i++) {
            threadsId[i] = -1;
            flags[i] = false;
            labels[i] = 0;
        }
    }

    /**
     * Aquisición del candado
     * @throws IllegalArgumentException Si el thread que intenta registrar, ya está registrado
     * @throws ExceededThreadLimitException Si se intenta registrar un hilo más de la cantidad máxima
     */
    public void lock() {
        register();
        int internalThreadId = getInternalIdThread();
        flags[internalThreadId] = true;
        labels[internalThreadId] = maxLabel() + 1;
        
        for (int k = 0; k < MAX_THREADS; k++) {
            if (k == internalThreadId)
                continue;
            while (flags[k] && (labels[k] <= labels[internalThreadId] && k < internalThreadId)) { /* Busy wait */ }
            break;
        }
    }

    /**
     * Liberación del candado
     * Cuando se libera, se elimina el registro del thread.
     * @throws ExceededThreadLimitException Si se intenta acceder con un hilo no registrado
     */
    public void unlock() {
        int internalThreadId = getInternalIdThread();
        flags[internalThreadId] = false;
        threadsId[internalThreadId] = -1;
    }

    /**
     * Menciona si el candado está a su máxima capacidad de threads que puede aceptar.
     * @return <code>true</code> si está lleno, <code>false</code> si no lo está.
     */
    public boolean isFull() {
        for (long threadID : threadsId)
            if (threadID == -1)
                return false;
        return true;
    }

    /**
     * Registra un hilo nuevo
     * @throws IllegalArgumentException Si el thread que intenta registrar, ya está registrado
     */
    private void register() {
        long id = Thread.currentThread().threadId();
        for (int i = 0; i < MAX_THREADS; i++)
            if (threadsId[i] == id)
                throw new IllegalArgumentException("El id del hilo ya ha sido registrado");

        for (int i = 0; i < MAX_THREADS; i++)
            if (threadsId[i] == -1) {
                threadsId[i] = id;
                return;
            }
    }

    /**
     * Regresa el identificador interno del thread actual
     * @return el identificador interno del thread actual
     * @throws ExceededThreadLimitException Si se intenta acceder con un hilo no registrado
     */
    private int getInternalIdThread() {
        Thread currentThread = Thread.currentThread();
        long id = currentThread.threadId();
        
        int adq = -1;
        for (int i = 0; i < MAX_THREADS; i++)
            if (threadsId[i] == id)
                adq = i;

        if (adq == -1)
            throw new ExceededThreadLimitException("Se intentó acceder a un candado Bakery con más de 4 hilos. Deteniendo ejecución.");
        return adq;
    }

    /**
     * Regresa el label con valor máximo
     * @return El label con valor máximo
     */
    private int maxLabel() {
        int max = labels[0];
        for (int i = 1; i < MAX_THREADS; i++)
            max = Math.max(max, labels[i]);
        return max;
    }
}