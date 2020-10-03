package azisaba.semaphore.spigot.hook;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.concurrent.CompletableFuture;

public class QuitEventDataSaveHooks {

    /**
     * 同期的なデータ保存をすべて待つようなフックを生成する。
     */
    public static QuitEventDataSaveHook<Void> synchronousDataSaveHook(JavaPlugin hostPlugin) {
        return (playerQuitEvent) -> {
            CompletableFuture<Void> future = new CompletableFuture<>();

            // 同期的に保存されるデータは次のティックにはすでに保存されているから、
            // 1ティック待ってFutureを完了させればよい
            Bukkit.getScheduler().scheduleSyncDelayedTask(hostPlugin, () -> future.complete(null), 1L);

            return future;
        };
    }

}
