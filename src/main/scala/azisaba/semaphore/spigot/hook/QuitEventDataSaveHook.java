package azisaba.semaphore.spigot.hook;

/*
 * @author Kory
 */

import org.bukkit.event.player.PlayerQuitEvent;

import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

public interface QuitEventDataSaveHook<U> extends Function<PlayerQuitEvent, CompletableFuture<U>> {

}
