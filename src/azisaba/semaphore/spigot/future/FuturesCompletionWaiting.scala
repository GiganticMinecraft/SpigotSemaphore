package azisaba.semaphore.spigot.future

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Success, Try}

/*
 * @author amata1219
 */

object FuturesCompletionWaiting {

  private def lift[T](futures: Seq[Future[T]]): Seq[Future[Try[T]]] = futures.map(_.map { Success(_) }.recover { Failure(_) } )

  def waitAllFuturesCompletion[T](futures: Seq[Future[T]]): Future[Seq[Try[T]]] = Future.sequence(lift(futures))

}
