package controllers

import javax.inject._
import play.api._
import play.api.mvc._

import java.util.Date
import java.text.SimpleDateFormat
import scala.concurrent.Future

class Application @Inject() (components: ControllerComponents)
    extends AbstractController(components) {
  // note the .async here   
  def index = Action.async {
    val time = new Date()
    val timeStr = new SimpleDateFormat().format(time)
    // this line use successful method of Future
    // So you don't have specific the ExecutionContext implictly
    // in this method, it simply wraps the result of an expression in a Future
    // without scheduling the computation in a different thread
    Future.successful {Ok(views.html.index(timeStr))}  
  }
}
