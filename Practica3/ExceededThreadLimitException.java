/**
 * Clase que representa la excepción en el caso que en una instancia de un CandadoLimitado
 * Se quiera hacer una operación, excediendo la cantidad máxima soportada.
 */
public class ExceededThreadLimitException extends RuntimeException {

    public ExceededThreadLimitException() {
        super("Se excedió el límite de threads en el candado.");
    }

    public ExceededThreadLimitException(String message) {
        super(message);
    }
    
}