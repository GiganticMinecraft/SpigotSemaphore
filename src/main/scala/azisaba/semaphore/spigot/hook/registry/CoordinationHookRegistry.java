package azisaba.semaphore.spigot.hook.registry;

/*
 * @author Kory
 */
import azisaba.semaphore.spigot.hook.QuitEventDataSaveHook;

public interface CoordinationHookRegistry {

    <U> void register(QuitEventDataSaveHook<U> save);

}
