package club.eridani.cursa.concurrent;

import java.util.concurrent.CountDownLatch;

/**
 * Created by B_312 on 05/01/2021
 */
public class Syncer {

    private final CountDownLatch latch;

    public Syncer(int size){
        latch = new CountDownLatch(size);
    }

    public CountDownLatch getLatch(){
        return latch;
    }

    public void countDown(){
        latch.countDown();
    }

    public void await(){
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
