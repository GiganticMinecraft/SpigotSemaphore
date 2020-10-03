package azisaba.semaphore.spigot;

import azisaba.semaphore.spigot.listener.PlayerQuitListener;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.concurrent.atomic.AtomicReference;

public class SpigotSemaphore extends JavaPlugin {

    public final AtomicReference<SignalPublisher> publisherRef = new AtomicReference<>();

    private final PlayerQuitListener hookRegistry = new PlayerQuitListener(publisherRef);

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(hookRegistry, this);
    }

    @Override
    public void onDisable() {
        HandlerList.unregisterAll(this);
    }
}
