package test.xpathvsscala.web

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.server.Directives._
import spray.json.DefaultJsonProtocol._
import test.xpathvsscala.order.JsonFormats._
import test.xpathvsscala.order.{Order, Orders}
import test.xpathvsscala.testdata.Data

/**
  * @author Joacim Zschimmer
  */
object Routes {

  def route =
    pathPrefix("api") {
      get {
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
              Orders.selectOrders(Data.rootFolder) filter isRequested
            }
          }
        }
      }
    }
}
