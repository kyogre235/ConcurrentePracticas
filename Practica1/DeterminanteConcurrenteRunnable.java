public class DeterminanteConcurrenteRunnable implements Runnable {
    static int determinante;
    static int n_prueba = 3;
    static int matriz_prueba[][] = { { 1, 2, 2 }, { 1, 0, -2 }, { 3, -1, 1 }};
    
    int num1, num2, num3, partial;

    public DeterminanteConcurrenteRunnable(int num1, int num2, int num3) {
        this.num1 = num1;
        this.num2 = num2;
        this.num3 = num3;
    }

    @Override
    public void run() {
        this.partial = this.num1 * this.num2 * this.num3;
    }

    public static int determinanteMatriz3x3(int matriz[][], int n_prueba) {
        // Crear las tareas (Runnable)
        DeterminanteConcurrenteRunnable r1 = new DeterminanteConcurrenteRunnable(matriz[0][0], matriz[1][1], matriz[2][2]);
        DeterminanteConcurrenteRunnable r2 = new DeterminanteConcurrenteRunnable(matriz[1][0], matriz[2][1], matriz[0][2]);
        DeterminanteConcurrenteRunnable r3 = new DeterminanteConcurrenteRunnable(matriz[2][0], matriz[0][1], matriz[1][2]);
        DeterminanteConcurrenteRunnable r4 = new DeterminanteConcurrenteRunnable(matriz[2][0], matriz[1][1], matriz[0][2]);
        DeterminanteConcurrenteRunnable r5 = new DeterminanteConcurrenteRunnable(matriz[1][0], matriz[0][1], matriz[2][2]);
        DeterminanteConcurrenteRunnable r6 = new DeterminanteConcurrenteRunnable(matriz[0][0], matriz[2][1], matriz[1][2]);

        // Crear hilos a partir de los Runnable
        Thread t1 = new Thread(r1);
        Thread t2 = new Thread(r2);
        Thread t3 = new Thread(r3);
        Thread t4 = new Thread(r4);
        Thread t5 = new Thread(r5);
        Thread t6 = new Thread(r6);

        // Iniciar los hilos
        t1.start(); t2.start(); t3.start();
        t4.start(); t5.start(); t6.start();

        // Esperar a que todos terminen
        try {
            t1.join(); t2.join(); t3.join();
            t4.join(); t5.join(); t6.join();
        } catch (InterruptedException e) {}

        // Combinar resultados
        return r1.partial + r2.partial + r3.partial
             - r4.partial - r5.partial - r6.partial;
    }

    public static void main(String[] args) {
        long startTime = System.nanoTime();
        determinante = determinanteMatriz3x3(matriz_prueba, n_prueba);
        long endTime = System.nanoTime();
        System.out.println("Program took " +
                (endTime - startTime) + "ns, result: " + determinante);
    }
}

