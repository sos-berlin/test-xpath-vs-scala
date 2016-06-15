package test.xpathvsscala.order

import scala.collection.immutable

/**
  * @author Joacim Zschimmer
  */
object Orders {

  def selectOrders(folder: Folder) =
    for (subfolder ← flattenFolders(folder);
         jobchain ← subfolder.jobchains;
         node ← jobchain.nodes;
         order ← node.orderQueue.orders)
    yield order

  def flattenFolders(folder: Folder): immutable.Seq[Folder] =
    folder +: (folder.subfolders flatMap flattenFolders)
}
