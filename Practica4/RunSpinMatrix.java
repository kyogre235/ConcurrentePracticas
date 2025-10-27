import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Clase principal donde multiples hilos asignan valores a una matriz compartida
 */
public class RunSpinMatrix {

	/**
	 * Tamaño n de la matriz cuadrada
	 */
	final static int SIZE = 10;

	/**
	 * Matriz compartida
	 */
	static int[][] matriz = new int[SIZE][SIZE];

	/**
	 * Índice actual
	 */
	static int index = 0;

	/** 
	 * Contador
	 */
	static int contador = 0;
	
	/**
	 * Agrega un valor en la última casilla vacía o con el valor modificado más viejo
	 * @return el índice actual
	 */
	public static int add() {
		if (++index == SIZE * SIZE)
			index = 0;
		matriz[(int) Math.floor(index / SIZE)][index % SIZE] = ++contador;
		return index;
	}
	
	/**
	 * Realiza una tarea como sección crítica
	 * @param lock Candado
	 * @return el valor del índice actual
	 */
	private static int task(Lock lock) {
		try {
			lock.lock();
			add();
			//matrixString();
		} finally {
			lock.unlock();		
		}
		return index;
	}

	/**
	 * Reinicia los valores de la matriz e índice
	 */
	public static void restart() {
		matriz = new int[SIZE][SIZE];
		index = 0;
		contador = 0;
	}

	/**
	 * Imprime la representación en cadena de la matriz
	 * @return La representación en cadena de la matriz
	 */
	public static String matrixString() {
		StringBuilder sb = new StringBuilder();
		for (int[] row : matriz) {
			sb.append("| ");
			for (int i = 0; i < SIZE - 1; i++)
				sb.append(row[i] + " ");
			sb.append(row[SIZE -1] + " |\n");
		}
		return sb.toString();
	}

	public static void main(String[] args) {
		// Valores a modificar
		final int THREADS = 4;
		final int TASKS = 400;

		// Candados
		List<Lock> locks = new ArrayList<>(); 
		locks.add(new TASLock());
		locks.add(new TTASLock());
		locks.add(new BackoffLock());
		locks.add(new MCSLock()); // MCS se queda pillado al realizar 1000 tareas
		locks.add(new ALock(THREADS));
		locks.add(new CLHLock());
		locks.add(new ReentrantLock());

		for (Lock lock : locks) {
			List<Future<Integer>> futures = new ArrayList<Future<Integer>>();
			ExecutorService executor = Executors.newFixedThreadPool(THREADS);

			long startTime = System.nanoTime();//Start time
			for(int i = 0; i < TASKS; i++)
				futures.add(executor.submit(() -> task(lock)));
			executor.shutdown();

			for (int i = 0; i < futures.size(); i++)
            	while(!futures.get(i).isDone()){}; // Comprobar que todas las tareas terminen
			long endTime = System.nanoTime();//Finish time

			System.out.println("Program took " +
                (endTime - startTime) * 0.000001 + "ms, with " + lock.getClass() + " on " + contador + " tasks.") ; //En milisegundos
			System.out.println("Matriz:\n" + matrixString());
			restart();
		}
	}
	

}
