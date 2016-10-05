package com.core_api

import spray.json.{DefaultJsonProtocol, DeserializationException, JsString, JsValue, RootJsonFormat}
import com.github.nscala_time.time.Imports._
import org.joda.time.format.{DateTimeFormatter, ISODateTimeFormat}


trait DateTimeJsonProtocol extends DefaultJsonProtocol {

  private val parserISO: DateTimeFormatter = ISODateTimeFormat.dateTimeNoMillis()

  def parse(s: String): DateTime = parserISO.parseDateTime(s)

  implicit object DateJsonFormat extends RootJsonFormat[DateTime] {

    override def write(obj: DateTime) = JsString(parserISO.print(obj))

    // example datetime 2016-09-30T21:17:52+02:00
    override def read(json: JsValue): DateTime = json match {
      case JsString(s) => parse(s)
      case _ => throw new DeserializationException(s"provided json: $json could not be deserialized to proper datetime")
    }
  }
}
