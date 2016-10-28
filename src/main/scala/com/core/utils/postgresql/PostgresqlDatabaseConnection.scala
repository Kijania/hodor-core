package com.core.utils.postgresql

import com.core.utils.HodorSettings
import slick.driver.PostgresDriver.api._

class PostgresqlDatabaseConnection {

  def db(dbName: String = HodorSettings.dbName,
          user: String = HodorSettings.dbUser,
          password: String = HodorSettings.dbPassword,
          host: String = HodorSettings.dbHost,
          driver: String = "org.postgresql.Driver"): Database = {

    val connectionUrl = s"jdbc:postgresql://$host/$dbName?user=$user&password=$password"
    Database.forURL(connectionUrl, driver)
  }
}
