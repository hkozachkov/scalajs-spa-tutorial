package controllers

import java.nio.ByteBuffer

import upickle.default
import upickle.default._

import play.api.libs.json._
import play.api.libs.functional.syntax._

import play.api.mvc._
import services.ApiService
import spatutorial.shared.Api

import scala.concurrent.ExecutionContext.Implicits.global

object Router extends autowire.Server[String, Reader, Writer] {
  override def read[Result: Reader](p: String) = default.read[Result](p)
  override def write[Result: Writer](r: Result) = default.write(r)
}

object Application extends Controller {
  val apiService = new ApiService()

  def index = Action {
    Ok(views.html.index("SPA tutorial"))
  }

  def autowireApi(path: String) = Action.async(parse.json) {
    implicit request =>
      println(s"Request path: $path")

      // get the request body as string
      val b = request.body.as[String]

      // call Autowire route
      Router.route[Api](apiService)(
        autowire.Core.Request(path.split("/"), read[Map[String, String]](b))
      ).map(json=> {

        Ok(json)
      })
  }

  def logging = Action(parse.anyContent) {
    implicit request =>
      request.body.asJson.foreach { msg =>
        println(s"CLIENT - $msg")
      }
      Ok("")
  }
}
