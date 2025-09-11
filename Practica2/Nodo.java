/**
 * Clase que define una Nodo de una Cola.
 * Utilizada en la clase {@link ColaSecuencial}
 * 
 * @author Prof. MCIC. Gilde Valeria Rodríguez Jiménez
 * Modificaciones para el documentado
 */
public class Nodo {

	/**
	 * Contenido del nodo
	 */
	public String item;

	/**
	 * Relación al nodo posterior
	 */
	public Nodo next;

	/**
	 * Constructor de la clase
	 * @param item Contenido del nodo
	 */
	public Nodo(String item) {
		this.item = item;
	}
}
