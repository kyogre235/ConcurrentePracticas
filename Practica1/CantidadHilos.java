
public class CantidadHilos {

  public static void main(String[] args) {
    int cores = Runtime.getRuntime().availableProcessors();
    System.out.println("el numero de hilos maximos es: "+ cores);
  }
  
}
