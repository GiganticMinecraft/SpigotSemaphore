package azisaba.semaphore.spigot.listener

import java.util.concurrent.CompletableFuture
import java.util.concurrent.atomic.AtomicReference

import azisaba.semaphore.spigot.future.FuturesCompletionWaiting
import azisaba.semaphore.spigot.hook.registry.BufferedHookRegistry
import azisaba.semaphore.spigot.signal.{PlayerDataSaveFailed, PlayerDataSaved, SignalPublisher}
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.event.{EventHandler, EventPriority, Listener}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.jdk.FutureConverters._
import scala.util.{Failure, Success}

/*
 * @author amata1219
 */
class PlayerQuitListener(publisherRef: AtomicReference[SignalPublisher], hookRegistry: BufferedHookRegistry) extends Listener {

  @EventHandler(priority = EventPriority.MONITOR)
  def on(event: PlayerQuitEvent): Unit = {
    val publisher = publisherRef.get() match {
      case null =>
        println("Redisへの接続情報が登録されていません。")
        return
      case x => x
    }

    val futures: Seq[Future[_]] = hookRegistry.hooks
      .map[CompletableFuture[_]](_ (event))
      .map(_.asScala)
      .toSeq

    val playerName: String = event.getPlayer.getName

    if (futures.nonEmpty) {
      FuturesCompletionWaiting.waitAllFuturesCompletion(futures).onComplete {
        case Success(_) =>
          publisher.publish(PlayerDataSaved(playerName))
        case Failure(ex) =>
          println(s"${playerName}のプレイヤーデータの保存に失敗しました。")
          ex.printStackTrace()
          publisher.publish(PlayerDataSaveFailed(playerName))
      }
    }
  }

}
