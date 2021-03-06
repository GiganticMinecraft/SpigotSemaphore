package azisaba.semaphore.spigot.hook.registry

import azisaba.semaphore.spigot.hook.QuitEventDataSaveHook

import scala.collection.mutable

final class BufferedHookRegistry extends CoordinationHookRegistry {

  val hooks: mutable.ArrayBuffer[QuitEventDataSaveHook[_]] = mutable.ArrayBuffer()

  override def register[U](save: QuitEventDataSaveHook[U]): Unit = hooks += save

}
