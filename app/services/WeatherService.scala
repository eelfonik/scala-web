package service

import scala.concurrent.Future
import play.api.libs.ws._
import scala.concurrent.ExecutionContext.Implicits.global

class WeatherService (ws: WSClient){
  def getWeather(lat: Double, lng: Double): Future[Double] = {
    // note here we get a Future of response with ws
    val weatherF = ws.url("http://api.openweathermap.org/data/2.5/" + s"weather?lat=${lat}&lon=${lng}&units=metric&appid=188c545b513123ecd39468edf04c097b").get()

    weatherF.map { weather =>
      val weatherJson = weather.json
      // 这里这个奇怪的\符号是什么， 为什么不用. ？
      // 然后这个as[String]是哪里来的？
      val temp = (weatherJson \ "main" \ "temp").as[Double]
      // note here as scala always return sth, the last line is important
      temp
    }
  }
}