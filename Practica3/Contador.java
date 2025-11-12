/**
 * Clase contador
 * Encargada de llevar un contador visible para múltiples threads.
 */
public class Contador {

    /**
     * Contador de la clase
     */
    private long contador;

    /**
     * Conatructor de la clase.
     * Default
     */
    public Contador() {
        contador = 0;
    }

    /**
     * Regresa el valor del contador
     * @return el valor del contador
     */
    public long get() {
        return contador;
    }

    /**
     * Incrementa en una unidad el contador
     */
    public void increment() {
        ++contador;
    }

    /**
     * Regresa la representación en cadena del contador
     * @return la representación en cadena del contador
     */
    @Override
    public String toString() {
        return "" + get();
    }

}
