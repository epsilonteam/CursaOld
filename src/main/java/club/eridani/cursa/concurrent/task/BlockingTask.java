package club.eridani.cursa.concurrent.task;

import club.eridani.cursa.concurrent.thread.BlockingContent;

public interface BlockingTask {
    void invoke(BlockingContent unit);
}
