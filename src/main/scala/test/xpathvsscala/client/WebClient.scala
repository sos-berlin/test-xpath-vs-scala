package test.xpathvsscala.client

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.HttpMethods.GET
import akka.http.scaladsl.model.StatusCodes.OK
import akka.http.scaladsl.model._
import akka.http.scaladsl.unmarshalling.{FromEntityUnmarshaller, Unmarshal}
import akka.stream.{ActorMaterializer, ActorMaterializerSettings}
import scala.concurrent.Future

/**
  * @author Joacim Zschimmer
  */
final class WebClient extends AutoCloseable {
  private implicit val actorSystem = ActorSystem("client")
  private implicit val materializer: ActorMaterializer = ActorMaterializer(ActorMaterializerSettings(actorSystem))
  private val http = Http(actorSystem)

  import actorSystem.dispatcher

  def get[A: FromEntityUnmarshaller](uri: String): Future[A] = {
    http.singleRequest(HttpRequest(GET, uri)) flatMap { response ⇒
      if (response.status != OK) sys.error(s"Request failed: ${response.status}")
      Unmarshal(response.entity).to[A]
    }
  }

  def close() = actorSystem.terminate()
}
