package service

import scala.concurrent.Future
import play.api.libs.ws._
import scala.concurrent.ExecutionContext.Implicits.global

import java.time.{ZoneId, ZonedDateTime}
import java.time.format.DateTimeFormatter

import model.SunInfo

class SunService (ws: WSClient) {
  def getSunTime (lat: Double, lng: Double): Future[SunInfo] = {
    val responseF = ws.url("http://api.sunrise-sunset.org/json?" + s"lat=${lat}&lng=${lng}&formatted=0").get()
    responseF.map { response =>
      val responseJson = response.json
      val sunRiseTimeStr = (responseJson \ "results" \ "sunrise").as[String]
      val sunSetTimeStr = (responseJson \ "results" \ "sunset").as[String]

      val sunRiseTime = ZonedDateTime.parse(sunRiseTimeStr)
      val sunSetTime = ZonedDateTime.parse(sunSetTimeStr)

      val formatter = DateTimeFormatter.ofPattern("HH:mm:ss").withZone(ZoneId.of("Asia/Shanghai"))

      val sunInfo = SunInfo(sunRiseTime.format(formatter), sunSetTime.format(formatter))

      sunInfo
    }
  }
}