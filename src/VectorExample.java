import java.util.Vector;

import static java.lang.Thread.currentThread;

public class VectorExample {
    private Vector<Integer> vector;

    private volatile boolean cycle = true;

    VectorExample(){
        vector = new Vector<>();
        Thread t1 = new Thread(new T1());
        Thread t2 = new Thread(new T2());
        t1.start();
        t2.start();
    }

    class T1 implements Runnable{
        @Override
        public void run(){
            System.out.println("T1 started");
            try {
                for (int i = 1; i <= 10; i++) {
                    int value = i;
                    vector.add(value);
                    System.out.println("T1 added : " + value +" "+ currentThread().getName());
                    Thread.sleep(200); //200 მილიწამის დაყოვნებით ვამატებ სტრინგებს queue-ში
                }
                cycle = false;
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    class T2 implements Runnable {
        @Override
        public void run(){
            int k;
            System.out.println("T2 started\n");
            while (cycle || vector.size() > 0) {
                for (int i = vector.size()-1; i >= 0; i--){
                    try {
                        k = vector.remove(i);
                        System.out.println("  T2 removed : " + k + " "+ currentThread().getName() );
                    }catch (Exception ex){
                        ex.printStackTrace();
                    }
                }
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
        new VectorExample();
        System.out.println( currentThread().getName());
    }

}
