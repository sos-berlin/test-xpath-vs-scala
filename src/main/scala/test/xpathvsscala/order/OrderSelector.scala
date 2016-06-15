package test.xpathvsscala.order

import scala.collection.immutable

/**
  * @author Joacim Zschimmer
  */
object OrderSelector {
  def find(folder: Folder)(query: Order ⇒ Boolean) =
    for (subfolder ← flattenFolders(folder);
        jobchain ← subfolder.jobchains;
         node ← jobchain.nodes;
         order ← node.orderQueue.orders if query(order))
    yield order

  def flattenFolders(folder: Folder): immutable.Seq[Folder] =
    folder +: (folder.subfolders flatMap flattenFolders)
}
