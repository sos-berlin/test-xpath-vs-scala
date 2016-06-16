package test.xpathvsscala.order

import com.sos.scheduler.engine.data.jobchain.JobChainPath
import com.sos.scheduler.engine.data.order.{OrderId, OrderState}
import scala.collection.immutable
import spray.json.DefaultJsonProtocol._
import spray.json.RootJsonFormat
import test.xpathvsscala.common.SprayUtils._

/**
  * @author Joacim Zschimmer
  */
object Orders {
  final case class Order(id: OrderId, isSuspended: Boolean = false, isSetBack: Boolean = false)
  final case class OrderQueue(orders: Vector[Order])
  final case class JobchainNode(state: OrderState, orderQueue: OrderQueue)
  final case class Jobchain(path: JobChainPath, nodes: Vector[JobchainNode])
  final case class Folder(subfolders: Vector[Folder], jobchains: Vector[Jobchain])

  implicit val JobChainPathJsonFormat = jsonFormat1(JobChainPath.apply)
  implicit val OrderStateJsonFormat = jsonFormat1(OrderState.apply)
  implicit val OrderIdJsonFormat = jsonFormat1(OrderId.apply)

  implicit val OrderJsonFormat = jsonFormat3(Order)
  implicit val OrderQueueJsonFormat = jsonFormat1(OrderQueue)
  implicit val JobchainNodeJsonFormat = jsonFormat2(JobchainNode)
  implicit val JobchainJsonFormat = jsonFormat2(Jobchain)
  implicit val FolderJsonFormat: RootJsonFormat[Folder] = lazyRootFormat(jsonFormat2(Folder))

  def selectOrders(folder: Folder) =
    for (subfolder ← flattenFolders(folder);
         jobchain ← subfolder.jobchains;
         node ← jobchain.nodes;
         order ← node.orderQueue.orders)
    yield order

  def flattenFolders(folder: Folder): immutable.Seq[Folder] =
    folder +: (folder.subfolders flatMap flattenFolders)
}
