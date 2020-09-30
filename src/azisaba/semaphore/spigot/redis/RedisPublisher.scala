package azisaba.semaphore.spigot.redis

trait RedisPublisher {

  def publish(channel: String, message: String)

}
