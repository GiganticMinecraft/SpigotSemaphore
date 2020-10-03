package azisaba.semaphore.spigot.listener

import java.util.concurrent.CompletableFuture
import java.util.concurrent.atomic.AtomicReference

import azisaba.semaphore.spigot.future.FuturesCompletionWaiting
import azisaba.semaphore.spigot.hook.{CoordinationHookRegistry, QuitEventDataSaveHook}
import azisaba.semaphore.spigot.{PlayerDataSaveFailed, PlayerDataSaved, SignalPublisher}
import org.bukkit.Bukkit
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.event.{EventHandler, EventPriority, Listener}
import org.bukkit.plugin.java.JavaPlugin

import scala.collection.mutable
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.jdk.FutureConverters._
import scala.util.{Failure, Success}

/*
 * @author amata1219
 */
class PlayerQuitListener(publisherRef: AtomicReference[SignalPublisher])
  extends Listener with CoordinationHookRegistry {

  val hookList: mutable.ArrayBuffer[QuitEventDataSaveHook[_]] = mutable.ArrayBuffer()

  @EventHandler(priority = EventPriority.MONITOR)
  def on(event: PlayerQuitEvent): Unit = {
    val publisher = publisherRef.get() match {
      case null =>
        println("Redisへの接続情報が登録されていません。")
        return
      case x => x
    }

    val futures: Seq[Future[_]] = hookList.map[CompletableFuture[_]](_ (event))
      .map(_.asScala)
      .toSeq

    val playerName: String = event.getPlayer.getName

    FuturesCompletionWaiting.waitAllFuturesCompletion(futures).onComplete {
      case Success(_) =>
        publisher.publish(PlayerDataSaved(playerName))
      case Failure(ex) =>
        println(s"${playerName}のプレイヤーデータの保存に失敗しました。")
        ex.printStackTrace()
        publisher.publish(PlayerDataSaveFailed(playerName))
    }
  }

  override def register[U](save: QuitEventDataSaveHook[U]): Unit = hookList += save

}
