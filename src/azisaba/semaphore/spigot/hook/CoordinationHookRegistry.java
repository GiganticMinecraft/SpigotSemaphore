package azisaba.semaphore.spigot.hook;

/*
 * @author Kory
 */

public interface CoordinationHookRegistry {

    <U> void register(QuitEventDataSaveHook<U> save);

}
