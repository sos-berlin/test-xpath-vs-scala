package test.xpathvsscala.order

import com.sos.scheduler.engine.data.jobchain.JobChainPath
import com.sos.scheduler.engine.data.order.{OrderId, OrderState}
import org.scalatest.FreeSpec
import test.xpathvsscala.order.OrderSelector._
import test.xpathvsscala.order.OrderSelectorTest._

/**
  * @author Joacim Zschimmer
  */
final class OrderSelectorTest extends FreeSpec {

  "none" in {
    assert(find(rootFolder) { _ ⇒ false } == Nil)
  }

  "all" in {
    assert(find(rootFolder) { _ ⇒ true } == List(
      a100_1, a100_2, a100_3, a100_4,
      a200_1, a200_2,
      b100_1,
      sub_c100_1,
      sub_c200_1,
      sub_sub_d100_1))
  }

  "suspended" in {
    assert(find(rootFolder) { _.isSuspended } == List(
      a100_2, a100_4,
      a200_2,
      sub_c200_1,
      sub_sub_d100_1))
  }

  "set back" in {
    assert(find(rootFolder) { _.isSetBack } == List(
      a100_3, a100_4,
      b100_1,
      sub_sub_d100_1))
  }

  "both suspended and set back" in {
    assert(find(rootFolder) { o ⇒ o.isSuspended && o.isSetBack } == List(
      a100_4,
      sub_sub_d100_1))
  }
}

private object OrderSelectorTest {
  private val a100_1 = Order(OrderId("1"))
  private val a100_2 = Order(OrderId("1"), isSuspended = true)
  private val a100_3 = Order(OrderId("1"), isSetBack = true)
  private val a100_4 = Order(OrderId("1"), isSuspended = true, isSetBack = true)

  private val a200_1 = Order(OrderId("1"))
  private val a200_2 = Order(OrderId("1"), isSuspended = true)

  private val b100_1 = Order(OrderId("1"), isSetBack = true)

  private val sub_c100_1 = Order(OrderId("1"))
  private val sub_c200_1 = Order(OrderId("1"), isSuspended = true)

  private val sub_sub_d100_1 = Order(OrderId("1"), isSuspended = true, isSetBack = true)

  private val rootFolder =
    Folder(
      jobchains = List(
        JobChain(
          JobChainPath("/A"),
          List(
            JobchainNode(OrderState("100"), OrderQueue(List(a100_1, a100_2, a100_3, a100_4))),
            JobchainNode(OrderState("200"), OrderQueue(List(a200_1, a200_2))))),
        JobChain(
          JobChainPath("/B"),
          List(
            JobchainNode(OrderState("100"), OrderQueue(List(b100_1)))))),
      subfolders = List(
        Folder(
          jobchains = List(
            JobChain(
              JobChainPath("/sub/C"),
              List(
                JobchainNode(OrderState("100"), OrderQueue(List(sub_c100_1))),
                JobchainNode(OrderState("200"), OrderQueue(List(sub_c200_1)))))),
          subfolders = List(
            Folder(
              jobchains = List(
                JobChain(
                  JobChainPath("/sub/subsub/D"),
                  List(
                    JobchainNode(OrderState("100"), OrderQueue(List(sub_sub_d100_1)))))),
              subfolders = Nil)))))
}
