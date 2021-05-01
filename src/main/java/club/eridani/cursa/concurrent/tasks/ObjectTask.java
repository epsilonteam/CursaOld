package club.eridani.cursa.concurrent.tasks;

import club.eridani.cursa.concurrent.Task;

/**
 * Created by B_312 on 05/01/2021
 */
public interface ObjectTask extends Task<Object> {
    @Override
    void invoke(Object o);
}
