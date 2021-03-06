package test.xpathvsscala.common

import spray.json.{JsValue, JsonFormat, RootJsonFormat}

/**
  * @author Joacim Zschimmer
  */
object SprayUtils {

  /**
    * Spray implements only lazyFormat.
    */
  def lazyRootFormat[T](format: ⇒ JsonFormat[T]) = new RootJsonFormat[T] {
    lazy val delegate = format
    def write(x: T) = delegate.write(x)
    def read(value: JsValue) = delegate.read(value)
  }
}
