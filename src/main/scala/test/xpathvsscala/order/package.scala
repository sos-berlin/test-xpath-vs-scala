package test.xpathvsscala

import com.sos.scheduler.engine.data.jobchain.JobChainPath
import com.sos.scheduler.engine.data.order.{OrderId, OrderState}
import spray.json.DefaultJsonProtocol._
import spray.json.RootJsonFormat
import test.xpathvsscala.common.SprayUtils.lazyRootFormat

/**
  * @author Joacim Zschimmer
  */
package object order {
  final case class Order(id: OrderId, isSuspended: Boolean = false, isSetBack: Boolean = false)
  final case class OrderQueue(orders: Vector[Order])
  final case class JobchainNode(state: OrderState, orderQueue: OrderQueue)
  final case class Jobchain(path: JobChainPath, nodes: Vector[JobchainNode])
  final case class Folder(subfolders: Vector[Folder], jobchains: Vector[Jobchain])

  object JsonFormats {
    implicit val JobChainPathJsonFormat = jsonFormat1(JobChainPath.apply)
    implicit val OrderStateJsonFormat = jsonFormat1(OrderState.apply)
    implicit val OrderIdJsonFormat = jsonFormat1(OrderId.apply)

    implicit val OrderJsonFormat = jsonFormat3(Order)
    implicit val OrderQueueJsonFormat = jsonFormat1(OrderQueue)
    implicit val JobchainNodeJsonFormat = jsonFormat2(JobchainNode)
    implicit val JobchainJsonFormat = jsonFormat2(Jobchain)
    implicit val FolderJsonFormat: RootJsonFormat[Folder] = lazyRootFormat(jsonFormat2(Folder))
  }
}
