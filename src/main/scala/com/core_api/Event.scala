package com.core_api

import com.github.nscala_time.time.Imports._

case class Event(
                name: String,
                date: DateTime = DateTime.now,
                text: Option[String] = None,
                mail: Option[String] = None
                )
