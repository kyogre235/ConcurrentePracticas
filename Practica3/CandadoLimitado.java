/**
 * Interfaz para candados donde el numero de threads que pueden adquirirlo es límitado
 */
public interface CandadoLimitado {

    /**
     * Aquisición del candado
     */
    public void lock();

    /**
     * Liberación del candado
     */
    public void unlock();

    /**
     * Menciona si el candado está a su máxima capacidad de threads que puede aceptar.
     * @return <code>true</code> si está lleno, <code>false</code> si no lo está.
     */
    public boolean isFull();
}