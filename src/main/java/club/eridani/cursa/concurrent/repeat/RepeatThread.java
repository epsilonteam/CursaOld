package club.eridani.cursa.concurrent.repeat;

/**
 * Created by B_312 on 05/01/2021
 */
public class RepeatThread extends Thread {

    @SuppressWarnings("BusyWait")
    @Override
    public void run() {
        while (RepeatManager.getInstance().repeatUnits.size() != 0) {
            RepeatManager.runDelay();
            try {
                sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        this.interrupt();
        RepeatManager.getInstance().repeatThread = null;
    }

}
