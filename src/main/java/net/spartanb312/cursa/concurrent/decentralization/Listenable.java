package net.spartanb312.cursa.concurrent.decentralization;

import net.spartanb312.cursa.concurrent.task.EventTask;

import java.util.concurrent.ConcurrentHashMap;

public interface Listenable {

    ConcurrentHashMap<DecentralizedEvent<? extends EventData>, EventTask<? extends EventData>> listenerMap();

    void subscribe();

    void unsubscribe();

}
