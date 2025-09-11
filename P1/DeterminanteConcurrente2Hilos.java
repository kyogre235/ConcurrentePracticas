/**
 * Clase que calcula el determinante de una matriz 3x3 de manera concurrente usando 2 hilos.
 */
public class DeterminanteConcurrente2Hilos implements Runnable {
  
  /**
   * Determinante de la matriz 3x3
   */
  static int determinante;

  /**
   * Matriz de prueba
   */
  static int matrizOg[][] = {{1,2,2},{1,0,-2},{3,-1,1}};

  /**
   * Resultado parcial
   */
  int result;

  /**
   * Matriz de la cual se va a calcular el determinante
   */
  int[][] matriz;

  /**
   * Valor booleano para determinar si se va a calcular la parte positiva o negativa del determinante
   * <code>true</code> para la parte positiva, <code>false</code> para la parte negativa
   */
  boolean valor;

  /**
   * Constructor (único) de la clase
   * @param matriz Matriz de la cual se va a calcular el determinante
   * @param valor Valor booleano para determinar si se va a calcular la parte positiva o negativa del determinante
   */
  public DeterminanteConcurrente2Hilos(int[][] matriz, boolean valor) {
    this.matriz = matriz;
    this.valor = valor;
  }

  /**
   * Método run que calcula la parte positiva o negativa del determinante dependiendo del valor de {@link valor}.
   * 
   * @see java.lang.Runnable#run()
   */
  @Override
  public void run() {
      if (valor){
        result = (matriz[0][0] * matriz[1][1] * matriz[2][2]) +
                 (matriz[1][0] * matriz[2][1] * matriz[0][2]) +
                 (matriz[2][0] * matriz[0][1] * matriz[1][2]);
      } else {
        result = (matriz[2][0] * matriz[1][1] * matriz[0][2]) + 
                 (matriz[1][0] * matriz[0][1] * matriz[2][2]) +
                 (matriz[0][0] * matriz[2][1] * matriz[1][2]);
      }
  }
  
  /**
   * Método que calcula el determinante de una matriz 3x3 usando 2 hilos
   * @param matriz Matriz de la cual se va a calcular el determinante
   * @return Determinante de la matriz
   */
  public static int determinanteMatriz3x3(int matriz[][]) {
    DeterminanteConcurrente2Hilos plus = new DeterminanteConcurrente2Hilos(matriz, true);
    DeterminanteConcurrente2Hilos minus = new DeterminanteConcurrente2Hilos(matriz, false);

    Thread t1 = new Thread(plus);
    Thread t2 = new Thread(minus);

    t1.start();
    t2.start();

    try {
      t1.join();
      t2.join();
    } catch (Exception e) {}

    return plus.result - minus.result;
  }

  public static void main(String[] args) {
    long startTime = System.nanoTime();
    determinante = determinanteMatriz3x3(matrizOg);
    long endTime = System.nanoTime();

    System.out.println("Progam took " +
        (endTime - startTime) + "ns, result: " + determinante);
  }
}
