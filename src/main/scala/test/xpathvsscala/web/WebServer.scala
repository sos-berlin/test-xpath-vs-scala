package test.xpathvsscala.web

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import com.sos.scheduler.engine.common.scalautil.Futures.implicits._
import com.sos.scheduler.engine.common.time.ScalaTime._
import java.net.InetSocketAddress
import test.xpathvsscala.web.Routes.route

/**
  * @author Joacim Zschimmer
  */
final class WebServer(address: InetSocketAddress) extends AutoCloseable {

  private implicit lazy val actorSystem = ActorSystem("WebServer")
  import actorSystem.dispatcher
  private implicit val materializer = ActorMaterializer(namePrefix = Some("WebServer"))
  private val binding = Http().bindAndHandle(route, address.getAddress.getHostAddress, address.getPort) await 10.s
  val port = binding.localAddress.getPort

  def close(): Unit = {
    binding.unbind() await 10.s
    actorSystem.terminate() await 10.s
  }

  override def toString = s"WebServer(${binding.localAddress})"
}
