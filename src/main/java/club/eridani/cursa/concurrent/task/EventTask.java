package club.eridani.cursa.concurrent.task;

/**
 * Created by B_312 on 05/01/2021
 */
public interface EventTask<T> {
    void invoke(T valueIn);
}
