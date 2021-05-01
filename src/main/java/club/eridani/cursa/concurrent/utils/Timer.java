package club.eridani.cursa.concurrent.utils;

public final class Timer {

    private long time;

    public Timer() {
        time = -1;
    }

    public boolean passed(int ms) {
        return ((System.currentTimeMillis() - this.time) >= ms);
    }

    public void reset() {
        time = System.currentTimeMillis();
    }

}