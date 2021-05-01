package club.eridani.cursa.concurrent.tasks;

import club.eridani.cursa.client.ConfigManager;

/**
 * Created by B_312 on 05/01/2021
 */
public class ConfigOperateTask implements ObjectTask {

    public Operation operation;

    public ConfigOperateTask(Operation operation) {
        this.operation = operation;
    }

    @Override
    public void invoke(Object valueIn) {
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
