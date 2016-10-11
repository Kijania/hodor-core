package com.core_api.dto

import com.github.nscala_time.time.Imports._
import spray.json.RootJsonFormat

case class Event(
                  name: String,
                  date: DateTime,
                  text: Option[String] = None,
                  mail: Option[String] = None
)

case class EventDto(
                     id: String,
                     event: Event
                   )

trait EventJsonProtocol extends DateTimeJsonProtocol {
  implicit val eventJsonProtocol: RootJsonFormat[Event] = jsonFormat4(Event.apply)
  implicit val eventDtoJsonProtocol: RootJsonFormat[EventDto] = jsonFormat2(EventDto.apply)
}