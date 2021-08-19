package club.eridani.cursa.concurrent.task;

import java.util.List;

/**
 * Created by B_312 on 05/01/2021
 */
public interface MultiParameterTask<T> {
    void invoke(List<T> valueIn);
}
