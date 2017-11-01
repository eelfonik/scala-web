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

import service._

class Application @Inject() (components: ControllerComponents, ws: WSClient)
    extends AbstractController(components) {

  val sunService = new SunService(ws)
  val weatherService = new WeatherService(ws)

  // note the .async here   
  def index = Action.async {
    val lat = 31.005
    val lng = 121.4086111
    val sunInfoF = sunService.getSunTime(lat, lng)
    val weatherInfoF = weatherService.getWeather(lat, lng)

    for {
      sunInfo <- sunInfoF
      weatherInfo <- weatherInfoF
    } yield {
      Ok(views.html.index(sunInfo, weatherInfo))
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
