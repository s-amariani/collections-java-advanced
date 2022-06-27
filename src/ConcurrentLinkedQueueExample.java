import java.util.concurrent.ConcurrentLinkedQueue;

public class ConcurrentLinkedQueueExample
{
    private ConcurrentLinkedQueue<String> queue = null;

    private volatile boolean cycle = true; //ვიყენებთ volatile-ს იმისათვის რომ cycle ცვლადი არ დაიქეშოს thread-ში

    ConcurrentLinkedQueueExample()
    {
        queue = new ConcurrentLinkedQueue<>();

        Thread t1 = new Thread(new T1());
        Thread t2 = new Thread(new T2());
        t1.start();
        t2.start();

    }
    class T1 implements Runnable {

        public void run() {
            System.out.println("T1 started");
            try {
                for (int i = 1; i <= 10; i++) {
                    String str = "String" + i;
                    queue.add(str);
                    System.out.println("T1 added : " + str);
                    Thread.sleep(200); //200 მილიწამის დაყოვნებით ვამატებ სტრინგებს queue-ში
                }
                cycle = false;
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
    class T2 implements Runnable {
        public void run() {
            String str;
            System.out.println("T2 started\n");
            while (cycle || queue.size() > 0) {
                if ((str = queue.poll()) != null) //ვიყენებ poll() მეთოდს წასაშლელად,სხვაობა არის მხოლოდ მაშინ, როდესაც queue არის ცარიელი: remove აგდებს exceptions ხოლო poll() აბრუნებს null-ს
                    System.out.println("  T2 removed : " + str);
                try {
                    Thread.sleep(500);//500 მილიწამის დაყოვნებით ვშლი,შესაბამისად შევსება ხდება უფრო სწრაფად ვიდრე წაშლა
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
    public static void main(String[] args)
    {
        new ConcurrentLinkedQueueExample();
    }
}
