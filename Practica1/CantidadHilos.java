/**
 * Clase que muestra la cantidad de hilos disponibles en tiempo de ejecución.
 */
public class CantidadHilos {

  public static void main(String[] args) {
    int cores = Runtime.getRuntime().availableProcessors();
    System.out.println("El numero de hilos maximos es: " + cores);
  }
}