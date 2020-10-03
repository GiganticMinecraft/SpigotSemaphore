package azisaba.semaphore.spigot;

import azisaba.semaphore.spigot.hook.registry.BufferedHookRegistry;
import azisaba.semaphore.spigot.hook.registry.CoordinationHookRegistry;
import azisaba.semaphore.spigot.listener.PlayerQuitListener;
import azisaba.semaphore.spigot.signal.SignalPublisher;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.concurrent.atomic.AtomicReference;

@SuppressWarnings("unused")
public class SpigotSemaphore extends JavaPlugin {

    private final BufferedHookRegistry globalRegistry = new BufferedHookRegistry();

    public CoordinationHookRegistry hookRegistry() {
        return globalRegistry;
    }

    public final AtomicReference<SignalPublisher> publisherRef = new AtomicReference<>();

    @Override
    public void onEnable() {
        getServer()
                .getPluginManager()
                .registerEvents(new PlayerQuitListener(publisherRef, globalRegistry), this);
    }

    @Override
    public void onDisable() {
        HandlerList.unregisterAll(this);
    }
}
