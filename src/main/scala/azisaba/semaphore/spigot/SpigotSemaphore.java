package azisaba.semaphore.spigot;

import azisaba.semaphore.spigot.listener.PlayerQuitListener;
import azisaba.semaphore.spigot.redis.RedisPublisher;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.concurrent.atomic.AtomicReference;

public class SpigotSemaphore extends JavaPlugin {

    public static final String PLUGIN_MESSAGING_CHANNEL = "BungeeSemaphore";

    public final AtomicReference<RedisPublisher> publisherRef = new AtomicReference<>();

    private final PlayerQuitListener hookRegistry = new PlayerQuitListener(publisherRef, this);

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(hookRegistry, this);
    }

    @Override
    public void onDisable() {
        HandlerList.unregisterAll(this);
    }
}
