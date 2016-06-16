package test.xpathvsscala.testdata

import com.sos.scheduler.engine.data.jobchain.JobChainPath
import com.sos.scheduler.engine.data.order.{OrderId, OrderState}
import test.xpathvsscala.order.Orders.{Folder, Jobchain, JobchainNode, Order, OrderQueue}

/**
  * @author Joacim Zschimmer
  */
object Data {
  val a100_1 = Order(OrderId("1"))
  val a100_2 = Order(OrderId("1"), isSuspended = true)
  val a100_3 = Order(OrderId("1"), isSetBack = true)
  val a100_4 = Order(OrderId("1"), isSuspended = true, isSetBack = true)

  val a200_1 = Order(OrderId("1"))
  val a200_2 = Order(OrderId("1"), isSuspended = true)

  val b100_1 = Order(OrderId("1"), isSetBack = true)

  val sub_c100_1 = Order(OrderId("1"))
  val sub_c200_1 = Order(OrderId("1"), isSuspended = true)

  val sub_sub_d100_1 = Order(OrderId("1"), isSuspended = true, isSetBack = true)

  val allOrders = List(
    a100_1, a100_2, a100_3, a100_4,
    a200_1, a200_2,
    b100_1,
    sub_c100_1,
    sub_c200_1,
    sub_sub_d100_1)

  val suspendedOrders = List(
    a100_2, a100_4,
    a200_2,
    sub_c200_1,
    sub_sub_d100_1)

  val setBackOrders = List(
    a100_3, a100_4,
    b100_1,
    sub_sub_d100_1)

  val bothSuspendedAndSetBackOrders = List(
    a100_4,
    sub_sub_d100_1)

  val suspendedButNotSetBackOrders = List(
    a100_2,
    a200_2,
    sub_c200_1)

  val rootFolder =
    Folder(
      jobchains = Vector(
        Jobchain(
          JobChainPath("/A"),
          Vector(
            JobchainNode(OrderState("100"), OrderQueue(Vector(a100_1, a100_2, a100_3, a100_4))),
            JobchainNode(OrderState("200"), OrderQueue(Vector(a200_1, a200_2))))),
        Jobchain(
          JobChainPath("/B"),
          Vector(
            JobchainNode(OrderState("100"), OrderQueue(Vector(b100_1)))))),
      subfolders = Vector(
        Folder(
          jobchains = Vector(
            Jobchain(
              JobChainPath("/sub/C"),
              Vector(
                JobchainNode(OrderState("100"), OrderQueue(Vector(sub_c100_1))),
                JobchainNode(OrderState("200"), OrderQueue(Vector(sub_c200_1)))))),
          subfolders = Vector(
            Folder(
              jobchains = Vector(
                Jobchain(
                  JobChainPath("/sub/subsub/D"),
                  Vector(
                    JobchainNode(OrderState("100"), OrderQueue(Vector(sub_sub_d100_1)))))),
              subfolders = Vector())))))
}
