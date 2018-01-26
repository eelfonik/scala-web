package controllers

import javax.inject._
import play.api._
import play.api.mvc._

import java.time.{ZoneId, ZonedDateTime}
import java.time.format.DateTimeFormatter
import java.util.concurrent.TimeUnit
import akka.pattern.ask
import akka.util.Timeout
import akka.actor._
// import java.util.Date
// import java.text.SimpleDateFormat
import scala.concurrent.Future
import play.api.libs.ws._
import scala.concurrent.ExecutionContext.Implicits.global

import model.SunInfo

import service._
import actors._

class Application (components: ControllerComponents, sunService: SunService, weatherService: WeatherService, actorSystem: ActorSystem)
    extends AbstractController(components) {

  // as we're using DI, we don't need to new 2 classes inside controllers
  // and move them to the loader
  // val sunService = new SunService(ws)
  // val weatherService = new WeatherService(ws)

  // note the .async here   
  def index = Action.async {
    val lat = 31.005
    val lng = 121.4086111
    val sunInfoF = sunService.getSunTime(lat, lng)
    val weatherInfoF = weatherService.getWeather(lat, lng)

    implicit val timeout = Timeout(5, TimeUnit.SECONDS)

    val requestsF = (actorSystem.actorSelection(StatsActor.path) ? StatsActor.GetStats).mapTo[Int]

    // as we have 2 apis, use for comprehension instead of flatMap/map
    // 之前还在想如果用了两个单独的map，要如何变成一个,塞进view.html里
    for {
      sunInfo <- sunInfoF
      weatherInfo <- weatherInfoF
      requests <- requestsF
    } yield {
      Ok(views.html.index(sunInfo, weatherInfo, requests))
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
