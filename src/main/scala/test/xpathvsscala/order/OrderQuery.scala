package test.xpathvsscala.order

/**
  * @author Joacim Zschimmer
  */
final case class OrderQuery(
  suspended: Option[Boolean] = None,
  setBack: Option[Boolean] = None
) extends (Order ⇒ Boolean) {

  def apply(order: Order) =
    (suspended forall { _ == order.isSuspended }) &&
    (setBack forall { _ == order.isSetBack })
}
