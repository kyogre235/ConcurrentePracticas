public class DeterminanteConcurrenteRunnable implements Runnable {
  int num1, num2, num3, parcial;

  public DeterminanteConcurrenteRunnable (int num1, int num2, int num3) {
    
    this.num1 = num1;
    this.num2 = num2;
    this.num3 = num3;

  }
  
  @Override
  public void run() {
    this.parcial = this.num1 + this.num2 + this.num3;
  }
  
  public static int determinanteMatriz3x3(int matriz[][]) {
    DeterminanteConcurrente r1 = new DeterminanteConcurrente(matriz[0][0], matriz[1][1], matriz[2][2]);
    DeterminanteConcurrente r2 = new DeterminanteConcurrente(matriz[1][0], matriz[2][1], matriz[0][2]);
    DeterminanteConcurrente r3 = new DeterminanteConcurrente(matriz[2][0], matriz[0][1], matriz[1][2]);
    DeterminanteConcurrente r4 = new DeterminanteConcurrente(matriz[2][0], matriz[1][1], matriz[0][2]);
    DeterminanteConcurrente r5 = new DeterminanteConcurrente(matriz[1][0], matriz[0][1], matriz[2][2]);
    DeterminanteConcurrente r6 = new DeterminanteConcurrente(matriz[0][0], matriz[2][1], matriz[1][2]);

    // Crear Threads con los Runnable
    Thread t1 = new Thread(r1);
    Thread t2 = new Thread(r2);
    Thread t3 = new Thread(r3);
    Thread t4 = new Thread(r4);
    Thread t5 = new Thread(r5);
    Thread t6 = new Thread(r6);

    // Iniciar hilos
    t1.start(); t2.start(); t3.start();
    t4.start(); t5.start(); t6.start();

    try {
        t1.join(); t2.join(); t3.join();
        t4.join(); t5.join(); t6.join();
    } catch (InterruptedException e) {}

    // Sumar y restar resultados
    return r1.partial + r2.partial + r3.partial - r4.partial - r5.partial - r6.partial;
    }

    public static void main(String[] args) {
        int matriz_prueba[][] = { { 1, 2, 2 }, { 1, 0, -2 }, { 3, -1, 1 }};
        long startTime = System.nanoTime();
        int determinante = determinanteMatriz3x3(matriz_prueba);
        long endTime = System.nanoTime();
        System.out.println("Program took " +
                (endTime - startTime) + "ns, result: " + determinante);
    }
}
