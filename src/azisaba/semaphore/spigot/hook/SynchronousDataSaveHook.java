package azisaba.semaphore.spigot.hook;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.concurrent.CompletableFuture;

/*
 * @author Kory
 */

public class SynchronousDataSaveHook {

    // hostPluginをフックを制御するプラグインに指定して、フックを生成する
    // ここでは型引数にIntegerを指定して0をcompleteしているが、Voidとnullの組でも何でもよい
    public static QuitEventDataSaveHook<Integer> generateHook(JavaPlugin hostPlugin) {
        return (playerQuitEvent) -> {
            CompletableFuture<Integer> future = new CompletableFuture<>();

            // 同期的に保存されるデータは次のティックにはすでに保存されているから、1ティック待ってFutureを完了させる
            Bukkit.getScheduler().scheduleSyncDelayedTask(hostPlugin, () -> future.complete(0), 1L);

            return future;
        };
    }

    // 実際にフックを登録する
    public static void registerHook(CoordinationHookRegistry registry, JavaPlugin hostPlugin) {
        registry.register(generateHook(hostPlugin));
    }
}

