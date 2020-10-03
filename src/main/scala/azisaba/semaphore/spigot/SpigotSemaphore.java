package azisaba.semaphore.spigot;

import amata1219.redis.plugin.messages.spigot.RedisPluginMessagesAPI;
import azisaba.semaphore.spigot.hook.SynchronousDataSaveHook;
import azisaba.semaphore.spigot.listener.PlayerQuitListener;
import azisaba.semaphore.spigot.redis.RedisPluginMessaging;
import azisaba.semaphore.spigot.redis.RedisPublisher;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class SpigotSemaphore extends JavaPlugin {

    public static final String PLUGIN_MESSAGING_CHANNEL = "BungeeSemaphore";

    private static SpigotSemaphore instance;

    private final PlayerQuitListener hookRegistry = new PlayerQuitListener();
    private RedisPublisher publisher;

    @Override
    public void onEnable() {
        instance = this;

        Plugin maybeRedisPluginMessages = getServer().getPluginManager().getPlugin("RedisPluginMessages");
        if (!(maybeRedisPluginMessages instanceof RedisPluginMessagesAPI))
            throw new NullPointerException("Not found RedisPluginMessages plugin.");

        publisher = new RedisPluginMessaging((RedisPluginMessagesAPI) maybeRedisPluginMessages);

        getServer().getPluginManager().registerEvents(hookRegistry, this);
    }

    @Override
    public void onDisable() {
        HandlerList.unregisterAll(this);
    }

    public static SpigotSemaphore isntance() {
        return instance;
    }

    public RedisPublisher publisher() {
        return publisher;
    }

    public void registerHook(JavaPlugin hostPlugin) {
        SynchronousDataSaveHook.registerHook(hookRegistry, hostPlugin);
    }

}
