package azisaba.semaphore.spigot.signal

/**
 * 上流、即ちBungeeCordへ向けてSpigotから送信するメッセージの型
 */
sealed trait OutboundSignal

case class PlayerDataSaved(playerName: String) extends OutboundSignal
case class PlayerDataSaveFailed(playerName: String) extends OutboundSignal
