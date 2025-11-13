import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TestSnapshotWait{

public static void main(String[] args) {
  
  int capacity = 4;
  String init = null;

  ConcurrentLinkedQueue<Integer> queue = new ConcurrentLinkedQueue<>();
  OFSnapshotH<String> snapshotI = new OFSnapshotH<String>(capacity,init);
  OFSnapshotH<String> snapshotR = new OFSnapshotH<String>(capacity,init);

  ExecutorService executor = Executors.newFixedThreadPool(4);

  Random rand = new Random();
  for (int i = 0; i < 10; i++) {
    int numRand = rand.nextInt(2);
    final int ntask = i;
    executor.execute(new RunnableQ1(ntask, queue, numRand, snapshotI, snapshotR));

  }
  executor.shutdown();

  while(!executor.isTerminated()){};

  StampedSnapH<String>[] copy = (StampedSnapH<String>[]) new StampedSnapH[capacity];
  copy = snapshotR.collect();

  System.out.println("Snapshot Final --- Values");
  
  for (int j = 0; j < capacity; j++) {
    System.out.println("\n Thread Owner: " + copy[j].owner + 
    " Last Stap: " + copy[j].stamp + " Number of Snaps: " + copy[j].snap.size());
    
    System.out.println("The values are: ");
    for (int i = 0; i < copy[j].values.size(); i++) {
      System.out.println(copy[j].values.get(i)); 
    }
  }
}
}
