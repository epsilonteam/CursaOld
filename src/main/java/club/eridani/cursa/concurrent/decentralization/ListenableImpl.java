package club.eridani.cursa.concurrent.decentralization;

import club.eridani.cursa.concurrent.task.EventTask;

import java.util.concurrent.ConcurrentHashMap;

public class ListenableImpl implements Listenable {

    private final ConcurrentHashMap<DecentralizedEvent<? extends EventData>, EventTask<? extends EventData>> listenerMap = new ConcurrentHashMap<>();

    @Override
    public ConcurrentHashMap<DecentralizedEvent<? extends EventData>, EventTask<? extends EventData>> listenerMap() {
        return listenerMap;
    }

    @Override
    public void subscribe() {
        listenerMap.forEach(DecentralizedEvent::register);
    }

    @Override
    public void unsubscribe() {
        listenerMap.forEach(DecentralizedEvent::unregister);
    }

    public <T extends EventData> void listener(Class<? extends DecentralizedEvent<T>> eventClass, EventTask<T> action) {
        listener(this, eventClass, action);
    }

    public static String[] instanceName = {"instance", "INSTANCE"};

    public static <T extends EventData> void listener(Listenable listenable, Class<? extends DecentralizedEvent<T>> eventClass, EventTask<T> action) {
        try {
            for (String name : instanceName) {
                DecentralizedEvent<? extends EventData> instance = tryGetInstance(eventClass, name);
                if (instance != null) {
                    listenable.listenerMap().put(instance, action);
                    return;
                }
            }
            throw new NoSuchFieldException("Can't find instant static field in the DecentralizedEvent class : " + eventClass.getName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static <T extends EventData> DecentralizedEvent<? extends EventData> tryGetInstance(Class<? extends DecentralizedEvent<T>> eventClass, String name) {
        try {
            return (DecentralizedEvent<? extends EventData>) eventClass.getDeclaredField(name).get(null);
        } catch (IllegalAccessException | NoSuchFieldException ignore) {
        }
        return null;
    }

}
