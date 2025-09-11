/**
 * Clase que define una cola secuencial.
 * Utiliza la clase {@link Nodo}
 * 
 * @author Prof. MCIC. Gilde Valeria Rodríguez Jiménez
 */
public class ColaSecuencial {

	/**
	 * Nodo cabeza (o primero)
	 */
	private Nodo head;

	/**
	 * Nodo cola (o final)
	 */
	private Nodo tail;

	/**
	 * Constructor de la clase.
	 * Constructor default.
	 * Se inicializan los contenidos como nulos, desde el contenido de un nodo.
	 */
	public ColaSecuencial() {
		this.head  = new Nodo("hnull");
		this.tail  = new Nodo("tnull");
		this.head.next = this.tail;
	}

	/**
	 * Encolar un nodo a la cola
	 * @param x Contenido en el nodo a encolar
	 * @return Respuesta que responde al éxito de encolar el nuevo nodo.
     *         <code>true</code> si fue éxitosa, <code>false</code> si ocurió un error
	 */
	public Boolean enq(String x) {
		Nodo newnode = new Nodo(x);
		Nodo last  = this.head;
		
    	// 1. Recorremos la lista para encontrar el último nodo
		//    (el que está justo antes de 'tail').
		while (last.next != this.tail) {
			last = last.next;
		}
		
		// 2. Insertamos el nuevo nodo entre 'last' y 'tail'.
		newnode.next = this.tail;
		last.next = newnode;
		return true;
	}
	
	/**
	 * Des-encolar un nodo de la cola
	 * @return Cadena con el contenido del elemento des-encolado
	 */
	public String deq() {
		if(this.head.next == this.tail) {
			return "empty";
		}
		Nodo first = this.head.next;
		this.head.next = first.next;		
		return first.item;
	}

	/**
	 * Imprime el contenidos de los nodos en la cola
	 */
	public void print() {
		System.out.println("Print ");
        Nodo pred = this.head;
        Nodo curr = pred.next;
        System.out.println(pred.item);
        while (curr.item != "tnull") {
			pred = curr;
			curr = curr.next;
			System.out.println(pred.item);
        }
	}
}
