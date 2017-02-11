package com.core.utils

import com.typesafe.config.ConfigFactory

class Configuration {
  private val config = ConfigFactory.load()

  val host = config.getString("http.host")
  val port = config.getInt("http.port")
}
