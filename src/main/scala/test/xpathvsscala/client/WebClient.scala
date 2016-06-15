package test.xpathvsscala.client

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.model.HttpMethods.GET
import akka.http.scaladsl.model.StatusCodes.OK
import akka.http.scaladsl.model._
import akka.http.scaladsl.unmarshalling.Unmarshal
import akka.stream.{ActorMaterializer, ActorMaterializerSettings}
import com.sos.scheduler.engine.common.scalautil.Futures.implicits.SuccessFuture
import com.sos.scheduler.engine.common.time.ScalaTime._
import test.xpathvsscala.order.Folder
import test.xpathvsscala.order.jsonFormats._

/**
  * @author Joacim Zschimmer
  */
final class WebClient(uri: String) extends AutoCloseable {
  private implicit val actorSystem = ActorSystem("client")
  private implicit val materializer: ActorMaterializer = ActorMaterializer(ActorMaterializerSettings(actorSystem))

  import actorSystem.dispatcher

  def fetchRootFolder(): Folder = {
    val http = Http(actorSystem)
    val response = http.singleRequest(HttpRequest(GET, uri)) await 10.s
    if (response.status != OK) sys.error(s"Request failed: ${response.status}")
    Unmarshal(response.entity).to[Folder] await 10.s
  }

  def close() = actorSystem.terminate()
}
