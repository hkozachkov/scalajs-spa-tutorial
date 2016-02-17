package spatutorial.client.services

import java.nio.ByteBuffer

import upickle.default

//import boopickle.Default._
import upickle.default._
import org.scalajs.dom

import scala.concurrent.Future
import scala.scalajs.concurrent.JSExecutionContext.Implicits.runNow
import scala.scalajs.js.typedarray._

object AjaxClient extends autowire.Client[String, Reader, Writer] {
  override def doCall(req: Request): Future[String] = {

    dom.ext.Ajax.post(
      url = "/api/" + req.path.mkString("/"),
      data = write(req.args),
      headers = Map("Content-Type" -> "application/json;charset=UTF-8")
    ).map(_.responseText)
  }

  override def read[Result: Reader](p: String) = default.read[Result](p)
  override def write[Result: Writer](r: Result) = default.write(r)
}
