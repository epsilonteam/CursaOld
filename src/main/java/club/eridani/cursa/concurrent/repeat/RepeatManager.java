package club.eridani.cursa.concurrent.repeat;

import java.util.ArrayList;
import java.util.List;

import static club.eridani.cursa.concurrent.TaskManager.launch;

/**
 * Created by B_312 on 05/01/2021
 */
public class RepeatManager {

    public static RepeatManager instance;

    public static void init() {
        if (instance == null) instance = new RepeatManager();
    }

    public final List<RepeatUnit> repeatUnits = new ArrayList<>();

    public static void update() {
        synchronized (instance.repeatUnits) {
            instance.repeatUnits.removeIf(RepeatUnit::isDead);
            instance.repeatUnits.forEach(it -> {
                if (it.shouldRun()) {
                    launch(it::run);
                }
            });
        }
    }

}
