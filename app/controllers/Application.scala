package controllers

import javax.inject._
import play.api._
import play.api.mvc._

import java.time.{ZoneId, ZonedDateTime}
import java.time.format.DateTimeFormatter
// import java.util.Date
// import java.text.SimpleDateFormat
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
    val weatherF = ws.url("http://api.openweathermap.org/data/2.5/weather?lat=31.005&lon=121.4086111&units=metric&appid=188c545b513123ecd39468edf04c097b").get()

    // as we have 2 apis, use for comprehension instead of flatMap/map
    // 之前还在想如果用了两个单独的map，要如何变成一个,塞进view.html里

    for {
      res <- responseF
      weather <- weatherF
    } yield {
      val json = res.json
      // 这里这个奇怪的\符号是什么， 为什么不用. ？
      // 然后这个as[String]是哪里来的？
      val sunRiseTimeStr = (json \ "results" \ "sunrise").as[String]
      val sunSetTimeStr = (json \ "results" \ "sunset").as[String]
      val sunRiseTime = ZonedDateTime.parse(sunRiseTimeStr)
      val sunSetTime = ZonedDateTime.parse(sunSetTimeStr)
      val formatter = DateTimeFormatter.ofPattern("HH:mm:ss").withZone(ZoneId.of("Asia/Shanghai"))
      val sunInfo = SunInfo(sunRiseTime.format(formatter), sunSetTime.format(formatter))

      val weatherJson = weather.json
      val temp = (weatherJson \ "main" \ "temp").as[Double]

      Ok(views.html.index(sunInfo, temp)) 
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
