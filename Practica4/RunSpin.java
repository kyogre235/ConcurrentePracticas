import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Clase principal donde multiples hilos incrementan un contador
 */
public class RunSpin {

	/**
	 * Cuenta del contador
	 */
	static int counter = 0;
	
	/**
	 * Incrementa en una unidad el valor del contador
	 * @return El valor del contador
	 */
	public static int increment() {
		return counter++;
	}
	
	/**
	 * Realiza una tarea como sección crítica
	 * @param lock Candado
	 * @return El valor actual del contador
	 */
	private static int task(Lock lock) {
		try {
			lock.lock();
			increment();
		} finally {
			lock.unlock();		
		}
		return counter;
	}

	/**
	 * Reestablece la cuenta del contador
	 */
	public static void restart() {
		counter = 0;
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
		locks.add(new MCSLock());
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
                (endTime - startTime) * 0.000001 + "ms, with " + lock.getClass() + " Count result: " + counter) ; //En milisegundos
			restart();
		}

		// Ejecución con contados atómico
		CounterAtomic counter = new CounterAtomic(); // Descomentar para probar el contador atomico

		List<Future<Integer>> futures = new ArrayList<Future<Integer>>();
		ExecutorService executor = Executors.newFixedThreadPool(THREADS);

		long startTime = System.nanoTime();//Start time
		for(int i = 0; i < TASKS; i++)
			futures.add(executor.submit(() -> counter.increment())); // Descomentar para probar el contador atomico
		executor.shutdown();

		for (int i = 0; i < futures.size(); i++)
        	while(!futures.get(i).isDone()){}; // Comprobar que todas las tareas terminen
		long endTime = System.nanoTime();//Finish time

		System.out.println("Program took " +
            (endTime - startTime) * 0.000001 + "ms, Count result: " + counter.getValue()) ; //En milisegundos
	}
	

}
