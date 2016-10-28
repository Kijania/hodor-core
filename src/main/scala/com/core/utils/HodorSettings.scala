package com.core.utils

import com.typesafe.config.ConfigFactory

object HodorSettings {
  private val config = ConfigFactory.load()

  val host = config.getString("http.host")
  val port = config.getInt("http.port")

  val dbHost = config.getString("postgresql.host")
  val dbName = config.getString("postgresql.name")
  val dbUser = config.getString("postgresql.user")
  val dbPassword = config.getString("postgresql.password")
}
