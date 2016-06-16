package test.xpathvsscala.webclient

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.HttpMethods.GET
import akka.http.scaladsl.model.MediaTypes.`application/json`
import akka.http.scaladsl.model.StatusCodes.OK
import akka.http.scaladsl.model.HttpRequest
import akka.http.scaladsl.model.headers.Accept
import akka.http.scaladsl.unmarshalling.{FromEntityUnmarshaller, Unmarshal}
import akka.stream.{ActorMaterializer, ActorMaterializerSettings}
import com.sos.scheduler.engine.common.scalautil.Futures.implicits.SuccessFuture
import com.sos.scheduler.engine.common.time.ScalaTime._
import scala.concurrent.Future

/**
  * @author Joacim Zschimmer
  */
final class WebClient extends AutoCloseable {
  private implicit val actorSystem = ActorSystem("webclient")
  private implicit val materializer = ActorMaterializer(ActorMaterializerSettings(actorSystem))
  private val http = Http(actorSystem)

  import actorSystem.dispatcher

  def get[A: FromEntityUnmarshaller](uri: String): Future[A] =
    http.singleRequest(HttpRequest(GET, uri, List(Accept(`application/json`)))) flatMap { response ⇒
      if (response.status != OK) sys.error(s"Request failed: ${response.status}")
      Unmarshal(response.entity).to[A]
    }


  def close() = {
    http.shutdownAllConnectionPools() await 10.s
    actorSystem.terminate() await 10.s
  }
}
