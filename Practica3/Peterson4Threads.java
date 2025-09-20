/**
 * Clase que representa un lock de Peterson para 4 hilos (DoublePeterson)
 * Fue modificada de la brindada en tareas pasadas
 */
public class Peterson4Threads implements CandadoLimitado {

    /**
     * Candados para primer y segundo hilo
     */
    private volatile CandadoLimitado lock1;

    /**
     * Candados para tercer y cuarto hilo
     */
    private volatile CandadoLimitado lock2;
    
    /**
     * Candados para los hilos que pasaron los candados anteriores
     */
    private volatile CandadoLimitado lock3;

    /**
     * Máximo de threads permitido
     */
    private final int MAX_THREADS = 4;

    /**
     * Identificadores de los threads
     */
    private volatile long[] threadsId = new long[MAX_THREADS];

    /**
     * Constructor de la clase
     * Default
     */
    public Peterson4Threads() {
        lock1 = new Peterson();
        lock2 = new Peterson();
        lock3 = new Peterson();
        
        for (int i = 0; i < MAX_THREADS; i++)
            threadsId[i] = -1;
    }

    /**
     * Aquisición del candado
     * @throws IllegalArgumentException Si el thread que intenta registrar, ya está registrado
     * @throws ExceededThreadLimitException Si se intenta registrar un hilo más de la cantidad máxima
     */
    public void lock() {
        register();
        int internalThreadId = getInternalIdThread();
        
        if (internalThreadId == 0 || internalThreadId == 1)
            lock1.lock();
        else if (internalThreadId == 2 || internalThreadId == 3)
            lock2.lock();
        lock3.lock();
    }

    /**
     * Liberación del candado
     * Cuando se libera, se elimina el registro del thread.
     * @throws ExceededThreadLimitException Si se intenta acceder con un hilo no registrado
     */
    public void unlock() {
        int internalThreadId = getInternalIdThread();
        
        lock3.unlock();
        if (internalThreadId == 0 || internalThreadId == 1)
            lock1.unlock();
        else if (internalThreadId == 2 || internalThreadId == 3)
            lock2.unlock();

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
            throw new ExceededThreadLimitException("Se intentó acceder a un candado Peterson con más de 2 hilos. Deteniendo ejecución.");
        return adq;
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
    
}