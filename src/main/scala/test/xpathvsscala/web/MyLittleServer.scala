package test.xpathvsscala.web

import akka.actor.ActorSystem
import com.sos.scheduler.engine.common.scalautil.Futures.implicits._
import com.sos.scheduler.engine.common.time.ScalaTime._
import java.net.InetSocketAddress

/**
  * @author Joacim Zschimmer
  */
final class MyLittleServer(address: InetSocketAddress) extends AutoCloseable {

  private implicit lazy val actorSystem = ActorSystem("MyLittleServer")
  lazy val webServer = new WebServer(address, Routes.route)

  def start(): Unit = webServer

  def close(): Unit = {
    webServer.close()
    actorSystem.terminate() await 10.s
  }
}
