package azisaba.semaphore.spigot

import azisaba.semaphore.spigot.hook.QuitEventDataSaveHook
import azisaba.semaphore.spigot.listener.PlayerQuitListener
import org.bukkit.event.HandlerList
import org.bukkit.plugin.java.JavaPlugin

import scala.collection.mutable

/*
 * @author amata1219
 */

class SpigotSemaphore extends JavaPlugin() {

  SpigotSemaphore.instance = this

  override def onEnable(): Unit = {
    getServer.getPluginManager.registerEvents(SpigotSemaphore.hookRegistry, this)
  }

  override def onDisable(): Unit = {
    HandlerList.unregisterAll(this)
  }

}
object SpigotSemaphore {

  var instance: SpigotSemaphore = _

  val hookList: mutable.ArrayBuffer[QuitEventDataSaveHook[_]] = mutable.ArrayBuffer()

  // フックの登録先として機能するイベントリスナ
  val hookRegistry = new PlayerQuitListener()

}
