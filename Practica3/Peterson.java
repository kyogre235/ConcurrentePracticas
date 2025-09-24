/**
 * Clase que representa un lock de Peterson
 * Fue modificada de la brindada en los ejemplos
 */
public class Peterson implements CandadoLimitado {

	/**
     * Máximo de threads permitido
     */
    private final int MAX_THREADS = 2;

    /**
     * Identificadores de los threads
     */
    private volatile long[] threadsId = new long[MAX_THREADS];

    /**
     * Banderas
     */
	private volatile boolean[] flag = new boolean[MAX_THREADS];

    /**
     * Víctima
     */
	private volatile int victim;

    /**
     * Constructor de la clase
     * Default
     */
	public Peterson() {
		flag[0] = false; flag[1] = false;
		threadsId[0] = -1; threadsId[1] = -1;
		victim = 3;
	}
    
    /**
     * Aquisición del candado
     * @throws IllegalArgumentException Si el thread que intenta registrar, ya está registrado
     * @throws ExceededThreadLimitException Si se intenta registrar un hilo más de la cantidad máxima
     */
	public void lock() {
        register();
		int i = getInternalIdThread();
		int j = 1-i;
		flag[i] = true;
		victim = i;
        while (flag[j] && victim == i) {/* Busy wait */};
    }

    /**
     * Liberación del candado
     * Cuando se libera, se elimina el registro del thread.
     * @throws ExceededThreadLimitException Si se intenta acceder con un hilo no registrado
     */
    public void unlock() {
        int i = getInternalIdThread();
		flag[i] = false;
        threadsId[i] = -1;
	}

    /**
     * Menciona si el candado está a su máxima capacidad de threads que puede aceptar.
     * @return <code>true</code> si está lleno, <code>false</code> si no lo está.
     */
    public boolean isFull() {
        return threadsId[0] != -1 && threadsId[1] != -1;
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
