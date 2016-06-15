package test.xpathvsscala.web

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.Http.ServerBinding
import akka.http.scaladsl.model.Uri
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer
import com.sos.scheduler.engine.common.scalautil.Futures.implicits._
import com.sos.scheduler.engine.common.time.ScalaTime._
import java.net.InetSocketAddress

/**
  * @author Joacim Zschimmer
  */
final class WebServer(address: InetSocketAddress, route: Route)(implicit actorSystem: ActorSystem) {

  import actorSystem.dispatcher

  private implicit val materializer = ActorMaterializer(namePrefix = Some("WebServer"))
  val serverBinding: ServerBinding = Http().bindAndHandle(route, address.getAddress.getHostAddress, address.getPort) await 10.s
  val localUri = Uri(s"http://127.0.0.1:${serverBinding.localAddress.getPort}/")

  def close(): Unit = serverBinding.unbind() await 10.s

  override def toString: String = s"WebServer($address)"
}
