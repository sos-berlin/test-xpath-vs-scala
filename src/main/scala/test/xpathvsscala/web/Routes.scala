package test.xpathvsscala.web

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.server.Directives._
import spray.json.DefaultJsonProtocol._
import test.xpathvsscala.order.Orders._
import test.xpathvsscala.testdata.Data

/**
  * @author Joacim Zschimmer
  */
object Routes {

  def route =
    get {
      pathPrefix("api") {
        path("allNestedFolders") {
          complete {
            Data.rootFolder
          }
        } ~
        path("orders") {
          parameterMap { parameters ⇒
            val suspendedOption = parameters.get("suspended") map { _.toBoolean }
            val setBackOption = parameters.get("setBack") map { _.toBoolean }
            def isRequested(o: Order) =
              (suspendedOption forall { _ == o.isSuspended }) &&
              (setBackOption forall { _ == o.isSetBack })
            complete {
              selectOrders(Data.rootFolder) filter isRequested
            }
          }
        }
      }
    }
}
