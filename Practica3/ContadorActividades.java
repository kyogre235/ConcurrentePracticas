/**
 * Clase contador
 * Encargada de llevar un contador para cada threads que se pueda registrar
 */
public class ContadorActividades {

    /**
     * Cantidad máxima de threads
     */
    private int MAX_THREADS;

    /**
     * Identificadores de los threads
     */
    private volatile long[] threadsId;

    /**
     * Contadores respectivos a cada thread
     */
    private long[] contadores;

    /**
     * Constructor de la clase
     * @param threads la cantidad máxima de threads a registrar
     */
    public ContadorActividades(int threads) {
        MAX_THREADS = threads;
        threadsId = new long[MAX_THREADS];
        contadores = new long[MAX_THREADS];

        for (int i = 0; i < MAX_THREADS; i++) {
            threadsId[i] = -1;
            contadores[i] = 0;
        }
    }

    /**
     * Regresa el arreglo de contadores
     * @return el arreglo de contadores
     */
    public long[] get() {
        return contadores;
    }

    /**
     * Regresa una oración como cadena que indica el valor de los contadores
     * @return una oración como cadena que indica el valor de los contadores
     */
    @Override
    public String toString() {
        String resultado = "";
        for (int i = 0; i < MAX_THREADS; i++)
            resultado += "Contador del Thread con id " + threadsId[i] + ": " + contadores[i] + "\n";
        return resultado;
    }

    /**
     * Incrementa en una unidad el contador del thread actual
     * @throws IllegalArgumentException Si el thread que intenta incrementar, no está registrado
     */
    public boolean increment() {
        long id = Thread.currentThread().threadId();
        register(id);
        for (int i = 0; i < id; i++)
            if (threadsId[i] == id) {
                ++contadores[i];
                return true;
            }
        throw new IllegalArgumentException("El id del hilo no ha sido registrado");
    }

    /**
     * Registra un thread
     * @param id identificador del thread
     * @throws ExceededThreadLimitException Si se intenta registrar un hilo más de la cantidad máxima
     */
    private void register(long id) {
        boolean in = false;
        for (int i = 0; i < MAX_THREADS; i++)
            if (threadsId[i] == id)
                in = true;
        if (!in) {
            for (int i = 0; i < MAX_THREADS; i++)
                if (threadsId[i] == -1) {
                    threadsId[i] = id;
                    return;
                }
            throw new ExceededThreadLimitException("Se intenta llenar con más hilos de los que el contador es posible de sorportar. Deteniendo ejecución");
        }
    }
}