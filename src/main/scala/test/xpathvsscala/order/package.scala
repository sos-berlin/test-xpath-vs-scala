package test.xpathvsscala

import com.sos.scheduler.engine.data.jobchain.JobChainPath
import com.sos.scheduler.engine.data.order.{OrderId, OrderState}
import scala.collection.immutable

/**
  * @author Joacim Zschimmer
  */
package object order {
  final case class Order(id: OrderId, isSuspended: Boolean = false, isSetBack: Boolean = false)
  final case class OrderQueue(orders: immutable.Seq[Order])
  final case class JobchainNode(state: OrderState, orderQueue: OrderQueue)
  final case class JobChain(path: JobChainPath, nodes: immutable.Seq[JobchainNode])
  final case class Folder(subfolders: immutable.Seq[Folder], jobchains: immutable.Seq[JobChain])
}
