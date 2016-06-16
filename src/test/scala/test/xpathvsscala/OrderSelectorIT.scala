package test.xpathvsscala

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import com.sos.scheduler.engine.common.scalautil.Futures.implicits._
import com.sos.scheduler.engine.common.time.ScalaTime._
import java.net.InetSocketAddress
import org.scalatest.{BeforeAndAfterAll, FreeSpec}
import spray.json.DefaultJsonProtocol._
import test.xpathvsscala.order.Orders._
import test.xpathvsscala.testdata.Data
import test.xpathvsscala.testdata.Data._
import test.xpathvsscala.web.WebServer
import test.xpathvsscala.webclient.WebClient

/**
  * @author Joacim Zschimmer
  */
final class OrderSelectorIT extends FreeSpec with BeforeAndAfterAll {

  private lazy val webServer = new WebServer(new InetSocketAddress("127.0.0.1", 0))
  private lazy val port = webServer.port
  private lazy val webClient = new WebClient

  override protected def beforeAll() = {
    webServer
    info(s"$webServer started")
    webClient
    super.beforeAll()
  }

  override protected def afterAll() = {
    webClient.close()
    webServer.close()
    super.afterAll()
  }

  for (i ← 1 to 3)
    s"$i" - {
      s"Data directly access" - {
        addClientSideTests(() ⇒ Data.rootFolder)
      }

      s"Data fetched web service" - {
        def fetchRootFolder() = webClient.get[Folder](s"http://127.0.0.1:$port/api/allNestedFolders") await 10.s
        addClientSideTests(fetchRootFolder)
      }

      "Data selected via web service" - {
        addServerSideTests()
      }
    }

  private def addClientSideTests(fetchRootFolder: () => Folder): Unit = {
    lazy val allOrders = selectOrders(fetchRootFolder())

    "none" in {
      assert((allOrders filter { _ ⇒ false }) == Nil)
    }

    "all" in {
      assert((allOrders filter { _ ⇒ true }) == allOrders)
    }

    "suspended" in {
      assert((allOrders filter { _.isSuspended }) == suspendedOrders)
    }

    "set back" in {
      assert((allOrders filter { _.isSetBack }) == setBackOrders)
    }

    "both suspended and set back" in {
      assert((allOrders filter { o ⇒ o.isSuspended && o.isSetBack }) == bothSuspendedAndSetBackOrders)
    }

    "suspended but not set back" in {
      assert((allOrders filter { o ⇒ o.isSuspended && !o.isSetBack }) == suspendedButNotSetBackOrders)
    }
  }

  private def addServerSideTests(): Unit = {
    "all" in {
      assert(fetchOrders("") == allOrders)
    }

    "suspended" in {
      assert(fetchOrders("suspended=true") == suspendedOrders)
    }

    "set back" in {
      assert(fetchOrders("setBack=true") == setBackOrders)
    }

    "both suspended and set back" in {
      assert(fetchOrders("suspended=true&setBack=true") == bothSuspendedAndSetBackOrders)
    }

    "suspended, but not set back" in {
      assert(fetchOrders("suspended=true&setBack=false") == suspendedButNotSetBackOrders)
    }
  }

  private def fetchOrders(query: String) = webClient.get[List[Order]](s"http://127.0.0.1:$port/api/orders?$query") await 10.s
}
