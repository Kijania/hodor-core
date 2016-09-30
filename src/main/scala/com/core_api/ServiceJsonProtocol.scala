package com.core_api

import spray.json.{DefaultJsonProtocol, DeserializationException, JsString, JsValue, RootJsonFormat}
import com.github.nscala_time.time.Imports._
import org.joda.time.format.{DateTimeFormatter, ISODateTimeFormat}


trait ServiceJsonProtocol extends DefaultJsonProtocol {

  implicit val eventProtocol: RootJsonFormat[Event] = jsonFormat4(Event.apply)

  implicit object DateJsonFormat extends RootJsonFormat[DateTime] {

    private val parserISO: DateTimeFormatter = ISODateTimeFormat.dateTimeNoMillis()

    override def write(obj: DateTime) = JsString(parserISO.print(obj))

    // example datetime 2016-09-30T21:17:52+02:00
    override def read(json: JsValue): DateTime = json match {
      case JsString(s) => parserISO.parseDateTime(s)
      case _ => throw new DeserializationException(s"provided json: $json could not be deserialized to proper datetime")
    }
  }
}
