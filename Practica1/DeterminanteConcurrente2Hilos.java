public class DeterminanteConcurrente2Hilos implements Runnable {
  static int determinante;
  static int matrizOg[][] = {{1,2,2},{1,0,-2},{3,-1,1}};

  int result;
  int[][] matriz;
  boolean valor;

  public DeterminanteConcurrente2Hilos(int[][] matriz, boolean valor) {
    this.matriz = matriz;
    this.valor = valor;
  }

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
  
   public static int determinanteMatriz3x3(int matriz[][]) {
     DeterminanteConcurrente2Hilos plus = new DeterminanteConcurrente2Hilos(matriz, true);
     DeterminanteConcurrente2Hilos minus = new DeterminanteConcurrente2Hilos(matriz,false);

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
         (endTime-startTime) + "ns, result: " + determinante);
   }
}
