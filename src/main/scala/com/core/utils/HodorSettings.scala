package com.core.utils

import com.typesafe.config.ConfigFactory

object HodorSettings {
  private val config = ConfigFactory.load()

  val host = config.getString("http.host")
  val port = config.getInt("http.port")
}
