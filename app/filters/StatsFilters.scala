package filters

import akka.stream.Materializer
import play.api.mvc.{Filter, RequestHeader, Result}
import scala.concurrent.Future
import play.api._
import akka.actor._

class StatsFilter(actorSystem: ActorSystem, implicit val mat: Materializer) extends Filter {
  override def apply(nextFilter: (RequestHeader) => Future[Result]) (header: RequestHeader): Future[Result] = {
    Logger.info(s"Serving another request: ${header.path}")
    nextFilter(header)
  }
}