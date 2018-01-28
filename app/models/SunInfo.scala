package model

import play.api.libs.json.Json

case class SunInfo(sunRise: String, sunSet: String)

object SunInfo {
  implicit val writes = Json.writes[SunInfo]
}