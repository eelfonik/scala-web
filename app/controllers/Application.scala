package controllers

import javax.inject._
import play.api._
import play.api.mvc._

import java.util.Date
import java.text.SimpleDateFormat
import scala.concurrent.Future
import play.api.libs.ws._
import scala.concurrent.ExecutionContext.Implicits.global

import model.SunInfo

class Application @Inject() (components: ControllerComponents, ws: WSClient)
    extends AbstractController(components) {
  // note the .async here   
  def index = Action.async {
    // note here we get a Future of response with ws
    val responseF = ws.url("http://api.sunrise-sunset.org/json?" + "lat=31.005&lng=121.4086111&formatted=0").get()
    // map is a buildin method of Future class
    responseF.map { res =>
      val json = res.json
      // 这里这个奇怪的\符号是什么， 为什么不用. ？
      // 然后这个as[String]是哪里来的？
      val sunRiseTimeStr = (json \ "results" \ "sunrise").as[String]
      val sunSetTimeStr = (json \ "results" \ "sunset").as[String]
      val sunInfo = SunInfo(sunRiseTimeStr, sunSetTimeStr)
      Ok(views.html.index(sunInfo))
    }
    // val time = new Date()
    // val timeStr = new SimpleDateFormat().format(time)

    // this line use successful method of Future
    // So you don't have specific the ExecutionContext implictly
    // in this method, it simply wraps the result of an expression in a Future
    // without scheduling the computation in a different thread
    // Future.successful { Ok(views.html.index(timeStr)) }  
  }
}
