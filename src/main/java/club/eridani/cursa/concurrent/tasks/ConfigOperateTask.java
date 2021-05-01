package club.eridani.cursa.concurrent.tasks;

import club.eridani.cursa.client.ConfigManager;
import club.eridani.cursa.concurrent.Task;

/**
 * Created by B_312 on 05/01/2021
 */
public class ConfigOperateTask implements Task {

    public Operation operation;

    public ConfigOperateTask(Operation operation) {
        this.operation = operation;
    }

    @Override
    public void invoke() {
        switch (operation) {
            case Save: {
                ConfigManager.saveAll();
                break;
            }
            case Load: {
                ConfigManager.safeLoadAll();
                break;
            }
        }
    }

    public enum Operation {
        Save,
        Load
    }

}
