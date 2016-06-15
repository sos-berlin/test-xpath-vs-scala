package test.xpathvsscala

import java.net.InetSocketAddress
import org.scalatest.{BeforeAndAfterAll, FreeSpec}
import test.xpathvsscala.client.WebClient
import test.xpathvsscala.data.Data
import test.xpathvsscala.data.Data._
import test.xpathvsscala.order.Folder
import test.xpathvsscala.order.OrderSelector._
import test.xpathvsscala.web.MyLittleServer

/**
  * @author Joacim Zschimmer
  */
final class OrderSelectorIT extends FreeSpec with BeforeAndAfterAll {

  private lazy val server = new MyLittleServer(new InetSocketAddress("127.0.0.1", 7777))
  private lazy val client = new WebClient("http://127.0.0.1:7777/api/allNestedFolders")

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

  for (i ← 1 to 10)
    s"$i" - {
      s"Data.rootFolder" - {
        addTests(() ⇒ Data.rootFolder)
      }

      s"Via web service" - {
        addTests(client.fetchRootFolder)
      }
    }

  def addTests(fetchRootFolder: () => Folder) {
    lazy val root = fetchRootFolder()

    "none" in {
      assert(find(root) { _ ⇒ false } == Nil)
    }

    "all" in {
      assert(find(root) { _ ⇒ true } == List(
        a100_1, a100_2, a100_3, a100_4,
        a200_1, a200_2,
        b100_1,
        sub_c100_1,
        sub_c200_1,
        sub_sub_d100_1))
    }

    "suspended" in {
      assert(find(root) { _.isSuspended } == List(
        a100_2, a100_4,
        a200_2,
        sub_c200_1,
        sub_sub_d100_1))
    }

    "set back" in {
      assert(find(root) { _.isSetBack } == List(
        a100_3, a100_4,
        b100_1,
        sub_sub_d100_1))
    }

    "both suspended and set back" in {
      assert(find(root) { o ⇒ o.isSuspended && o.isSetBack } == List(
        a100_4,
        sub_sub_d100_1))
    }
  }
}
