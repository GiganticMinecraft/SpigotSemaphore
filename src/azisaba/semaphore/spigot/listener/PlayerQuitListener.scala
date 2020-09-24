package azisaba.semaphore.spigot.listener

import java.util.concurrent.CompletableFuture

import azisaba.semaphore.spigot.SpigotSemaphore
import azisaba.semaphore.spigot.future.FuturesCompletionWaiting
import azisaba.semaphore.spigot.hook.{CoordinationHookRegistry, QuitEventDataSaveHook}
import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.event.{EventHandler, EventPriority, Listener}

import scala.collection.mutable.ArrayBuffer
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.jdk.FutureConverters._
import scala.util.{Failure, Success}

/*
 * @author amata1219
 */

class PlayerQuitListener extends Listener with CoordinationHookRegistry {

  val hookList: ArrayBuffer[QuitEventDataSaveHook[_]] = SpigotSemaphore.hookList

  @EventHandler(priority = EventPriority.MONITOR)
  def on(event: PlayerQuitEvent): Unit = {
    val futures: Seq[Future[_]] = hookList.map[CompletableFuture[_]](_ (event))
      .map(_.asScala)
      .toSeq

    val player: Player = event.getPlayer
    FuturesCompletionWaiting.waitAllFuturesCompletion(futures).onComplete {
      case Success(_) =>
      case Failure(ex) =>
    }
  }

  override def register[U](save: QuitEventDataSaveHook[U]): Unit = hookList += save

}
