package com.core_api.utils

import com.github.nscala_time.time.Imports._
import org.joda.time.format.ISODateTimeFormat

import scala.util.Try

object DateTimeParser {

  // TODO add login when bad format
  def parseDateTime(s: String): DateTime = {
    (Try {
      ISODateTimeFormat
        .dateTime()
        .parseDateTime(s)
        .withSecondOfMinute(0)
        .withMillisOfSecond(0)
    } orElse {
      Try {
        ISODateTimeFormat
          .dateTimeNoMillis()
          .parseDateTime(s)
          .withSecondOfMinute(0)
      }
    } toOption
      ).get
  }

  def dateTimeToString(obj: DateTime): String = ISODateTimeFormat.dateTimeNoMillis().print(obj)

}
