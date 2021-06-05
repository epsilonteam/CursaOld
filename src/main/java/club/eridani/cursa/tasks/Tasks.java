package club.eridani.cursa.tasks;

import club.eridani.cursa.concurrent.task.VoidTask;

public enum Tasks {

    LoadConfig(new ConfigOperateTask(ConfigOperateTask.Operation.Load)),
    SaveConfig(new ConfigOperateTask(ConfigOperateTask.Operation.Save));

    public VoidTask task;

    Tasks(VoidTask task) {
        this.task = task;
    }

}