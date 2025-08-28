/**
 * Clase que calcula el determinante de una matriz 3x3 de forma secuencial
 */
public class DeterminanteSecuencial {

    /**
     * Método que calcula el determinante de una matriz 3x3 de forma secuencial
     * @param matriz Matriz de la cual se va a calcular el determinante
     * @return Determinante de la matriz
     */
    public static int determinanteMatriz3x3(int[][] matriz) {
        // Regla de Sarrus
        int positivos = (matriz[0][0] * matriz[1][1] * matriz[2][2])
                      + (matriz[1][0] * matriz[2][1] * matriz[0][2])
                      + (matriz[2][0] * matriz[0][1] * matriz[1][2]);

        int negativos = (matriz[2][0] * matriz[1][1] * matriz[0][2])
                      + (matriz[1][0] * matriz[0][1] * matriz[2][2])
                      + (matriz[0][0] * matriz[2][1] * matriz[1][2]);

        return positivos - negativos;
    }

    public static void main(String[] args) {
        int matriz_prueba[][] = {
            { 1, 2, 2 },
            { 1, 0, -2 },
            { 3, -1, 1 }
        };

        long startTime = System.nanoTime();
        int determinante = determinanteMatriz3x3(matriz_prueba);
        long endTime = System.nanoTime();

        System.out.println("Tiempo de ejecución: " + (endTime - startTime) + " ns");
        System.out.println("Determinante: " + determinante);
    }
}

