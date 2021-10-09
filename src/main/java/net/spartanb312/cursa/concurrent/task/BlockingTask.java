package net.spartanb312.cursa.concurrent.task;

import net.spartanb312.cursa.concurrent.thread.BlockingContent;

public interface BlockingTask {
    void invoke(BlockingContent unit);
}
