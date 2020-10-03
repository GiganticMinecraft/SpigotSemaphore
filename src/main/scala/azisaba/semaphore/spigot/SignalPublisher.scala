package azisaba.semaphore.spigot

trait SignalPublisher {

  /**
   * 上流へ向けてシグナルを送信する。
   * このメソッドの実装は[[OutboundSignal]]を上流のすべてのBungeeCordに伝達さえさせればよく、手段は問わない。
   *
   * BungeeSemaphoreが受け取り口となる場合、BungeeSemaphoreと共有するredisの BungeeSemaphore チャンネルに向けて
   *  - confirm_player_data_saved $playerName
   *  - failed_saving_some_player_data $playerName
   * のどちらかをpublishすることが求められる。
   */
  def publish(signal: OutboundSignal): Unit

}
