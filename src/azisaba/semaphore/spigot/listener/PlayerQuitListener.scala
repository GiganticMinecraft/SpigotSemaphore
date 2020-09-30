package azisaba.semaphore.spigot.listener

import java.util.concurrent.CompletableFuture

import amata1219.redis.plugin.messages.spigot.RedisPluginMessagesAPI
import azisaba.semaphore.spigot.SpigotSemaphore
import azisaba.semaphore.spigot.future.FuturesCompletionWaiting
import azisaba.semaphore.spigot.hook.{CoordinationHookRegistry, QuitEventDataSaveHook}
import org.bukkit.Bukkit
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.event.{EventHandler, EventPriority, Listener}

import scala.collection.mutable
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.jdk.FutureConverters._
import scala.util.{Failure, Success}

/*
 * @author amata1219
 */

class PlayerQuitListener extends Listener with CoordinationHookRegistry {

  val hookList: mutable.ArrayBuffer[QuitEventDataSaveHook[_]] = mutable.ArrayBuffer()

  @EventHandler(priority = EventPriority.MONITOR)
  def on(event: PlayerQuitEvent): Unit = {
    val futures: Seq[Future[_]] = hookList.map[CompletableFuture[_]](_ (event))
      .map(_.asScala)
      .toSeq

    Bukkit.getScheduler.runTask(SpigotSemaphore.isntance(), () => {
      val redis: RedisPluginMessagesAPI = SpigotSemaphore.isntance().redisPluginMessagesAPI()
      val playerName: String = event.getPlayer.getName
      FuturesCompletionWaiting.waitAllFuturesCompletion(futures).onComplete {
        case Success(_) =>
          redis.sendRedisPluginMessage(SpigotSemaphore.PLUGIN_MESSAGING_CHANNEL, s"confirm_player_data_saved $playerName")
        case Failure(ex) =>
          println(s"${playerName}のプレイヤーデータの保存に失敗しました。")
          ex.printStackTrace()
          redis.sendRedisPluginMessage(SpigotSemaphore.PLUGIN_MESSAGING_CHANNEL, s"failed_saving_some_player_data $playerName")
      }
    })
  }

  override def register[U](save: QuitEventDataSaveHook[U]): Unit = hookList += save

}
