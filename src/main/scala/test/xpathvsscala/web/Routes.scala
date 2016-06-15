package test.xpathvsscala.web

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.server.Directives._
import test.xpathvsscala.data.Data
import test.xpathvsscala.order.jsonFormats._

/**
  * @author Joacim Zschimmer
  */
object Routes {
  def route =
    path("api" / "allNestedFolders") {
      get {
        complete {
          Data.rootFolder
        }
      }
    }
}
