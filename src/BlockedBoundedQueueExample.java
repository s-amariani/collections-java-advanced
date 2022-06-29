import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.lang.Thread.UncaughtExceptionHandler;

public class BlockedBoundedQueueExample {
    BlockingQueue<Integer> queue = null;
    public BlockedBoundedQueueExample() {
        queue =  new LinkedBlockingDeque<>(5);

        Thread thread = new Thread(new QueueDataGenerator(queue));
        thread.start();

        thread.setUncaughtExceptionHandler(new UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread t, Throwable e) {
                System.out.println(t + " throws exception: " + e);

                System.out.println("removing first element from: "+queue);
                queue.remove(0);
                System.out.println("updated queue is: "+queue);
            }
        });
    }

    record QueueDataGenerator(BlockingQueue<Integer> blockingQueue) implements Runnable {

        @Override
        public void run() {
            //ვამატებთ 5 ელემენტს
            for (int i = 0; i < 5; i++) {
                System.out.println("Put element : " + i + " in blocking queue");
                try {
                    blockingQueue.put(i);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                System.out.println("remaining capacity : " + blockingQueue.remainingCapacity());
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            System.out.println(blockingQueue);
            System.out.println("invoke  Deque full exception by adding element in full queue ");
            blockingQueue.add(19);
        }
    }
    public static void main(String[] args) {
        new BlockedBoundedQueueExample();
    }
}

