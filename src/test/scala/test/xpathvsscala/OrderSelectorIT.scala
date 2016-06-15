package test.xpathvsscala

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import com.sos.scheduler.engine.common.scalautil.Futures.implicits._
import com.sos.scheduler.engine.common.time.ScalaTime._
import com.sos.scheduler.engine.common.utils.FreeTcpPortFinder.findRandomFreeTcpPort
import java.net.InetSocketAddress
import org.scalatest.{BeforeAndAfterAll, FreeSpec}
import spray.json.DefaultJsonProtocol._
import test.xpathvsscala.client.WebClient
import test.xpathvsscala.order.JsonFormats._
import test.xpathvsscala.order.Orders._
import test.xpathvsscala.order.{Folder, Order}
import test.xpathvsscala.testdata.Data
import test.xpathvsscala.testdata.Data._
import test.xpathvsscala.web.MyLittleServer

/**
  * @author Joacim Zschimmer
  */
final class OrderSelectorIT extends FreeSpec with BeforeAndAfterAll {

  private val port = findRandomFreeTcpPort()
  private lazy val server = new MyLittleServer(new InetSocketAddress("127.0.0.1", port))
  private lazy val client = new WebClient

  override protected def beforeAll() = {
    server.start()
    client
    super.beforeAll()
  }

  override protected def afterAll() = {
    client.close()
    server.close()
    super.afterAll()
  }

  for (i ← 1 to 3)
    s"$i" - {
      s"Data directly access" - {
        addClientSideTests(() ⇒ Data.rootFolder)
      }

      s"Data fetched web service" - {
        addClientSideTests(() ⇒ client.get[Folder](s"http://127.0.0.1:$port/api/allNestedFolders") await 10.s)
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
  }

  private def addServerSideTests(): Unit = {
    "all" in {
      val orders = client.get[List[Order]](s"http://127.0.0.1:$port/api/orders") await 10.s
      assert(orders == allOrders)
    }

    "suspended" in {
      val orders = client.get[List[Order]](s"http://127.0.0.1:$port/api/orders?suspended=true") await 10.s
      assert(orders == suspendedOrders)
    }

    "set back" in {
      val orders = client.get[List[Order]](s"http://127.0.0.1:$port/api/orders?setBack=true") await 10.s
      assert(orders == setBackOrders)
    }

    "both suspended and set back" in {
      val orders = client.get[List[Order]](s"http://127.0.0.1:$port/api/orders?suspended=true&setBack=true") await 10.s
      assert(orders == bothSuspendedAndSetBackOrders)
    }
  }
}
