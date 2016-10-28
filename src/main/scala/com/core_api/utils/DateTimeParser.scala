package com.core_api.utils

import com.github.nscala_time.time.Imports._
import org.joda.time.format.{DateTimeFormatter, ISODateTimeFormat}

object DateTimeParser {

  private val parserISO: DateTimeFormatter = ISODateTimeFormat.dateTimeNoMillis()

  def parseDateTime(s: String): DateTime = parserISO.parseDateTime(s)

  def dateTimeToString(obj: DateTime): String = parserISO.print(obj)
}
