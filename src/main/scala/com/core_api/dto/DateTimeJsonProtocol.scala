package com.core_api.dto

import com.github.nscala_time.time.Imports._
import spray.json
import spray.json.{DeserializationException, JsString, JsValue, RootJsonFormat}
import com.core_api.utils.DateTimeParser._

object DateTimeJsonProtocol extends json.DefaultJsonProtocol {
  implicit object DateTimeJsonFormat extends RootJsonFormat[DateTime] {

    override def write(obj: DateTime) = JsString(dateTimeToString(obj))

    // example datetime 2016-09-30T21:17:52+02:00
    override def read(json: JsValue): DateTime = json match {
      case JsString(s) => parseDateTime(s)
      case _ => throw new DeserializationException(s"provided json: $json could not be deserialized to proper datetime")
    }
  }
}
