
//Programa 7: Cola Secuencial, utiliza la clase Nodo
public class ColaSecuencial {
	private Nodo head;
	private Nodo tail;
	public ColaSecuencial() {
		this.head  = new Nodo("hnull");
	    this.tail  = new Nodo("tnull");
	    this.head.next = this.tail;
	}
	//public synchronized Boolean enq(String x) {
		public Boolean enq(String x){
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
	//public synchronized String deq() {
	  public String deq(){
    if(this.head.next == this.tail) {
			return "empty";
		}
		Nodo first = this.head.next;
		this.head.next = first.next;		
		return first.item;
	}
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
