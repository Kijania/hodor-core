package com.core_api

import com.github.nscala_time.time.Imports._
import spray.json.RootJsonFormat

case class Event(
                name: String,
                date: DateTime = DateTime.now,
                text: Option[String] = None,
                mail: Option[String] = None
                )

trait EventJsonProtocol extends DateTimeJsonProtocol {
  implicit val eventJsonProtocol: RootJsonFormat[Event] = jsonFormat4(Event.apply)
}