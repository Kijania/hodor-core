package com.core_api.dto

import com.github.nscala_time.time.Imports._
import spray.json.{DefaultJsonProtocol, RootJsonFormat}
import com.core_api.utils.DateTimeJsonProtocol._

case class Event(
                  name: String,
                  date: DateTime,
                  text: Option[String] = None,
                  mail: Option[String] = None
) {
  def dto: EventDto = EventDto(-1L, name, date, text, mail)
  def dto(id: Long): EventDto = EventDto(id, name, date, text, mail)
  def dto(id: String): EventDto = dto(id.toLong)
}

case class EventDto(
                     id: Long,
                     name: String,
                     date: DateTime,
                     text: Option[String] = None,
                     mail: Option[String] = None
                   ) extends BaseDto {
  def event = Event(name, date, text, mail)
}

object EventJsonProtocol extends DefaultJsonProtocol {
  implicit val eventJsonProtocol: RootJsonFormat[Event] = jsonFormat4(Event.apply)
  implicit val eventDtoJsonProtocol: RootJsonFormat[EventDto] = jsonFormat5(EventDto.apply)
}