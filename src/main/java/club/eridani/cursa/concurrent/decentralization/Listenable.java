package club.eridani.cursa.concurrent.decentralization;

import club.eridani.cursa.concurrent.task.EventTask;

import java.util.concurrent.ConcurrentHashMap;

public interface Listenable {

    ConcurrentHashMap<DecentralizedEvent<? extends EventData>, EventTask<? extends EventData>> listenerMap();

    void subscribe();

    void unsubscribe();

}
