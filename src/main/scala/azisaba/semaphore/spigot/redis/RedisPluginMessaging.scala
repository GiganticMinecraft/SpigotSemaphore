package azisaba.semaphore.spigot.redis

import amata1219.redis.plugin.messages.spigot.RedisPluginMessagesAPI

class RedisPluginMessaging(val api: RedisPluginMessagesAPI) extends RedisPublisher {

  override def publish(channel: String, message: String): Unit = api.sendRedisPluginMessage(channel, message)

}
