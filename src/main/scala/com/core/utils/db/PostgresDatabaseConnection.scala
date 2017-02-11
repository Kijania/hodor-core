package com.core.utils.db

import slick.backend.DatabaseConfig
import slick.driver.JdbcProfile

trait DatabaseConnection {
  val db: JdbcProfile#Backend#Database
  val driver: JdbcProfile
}

class PostgresDatabaseConnection extends DatabaseConnection {

  // TODO encrypt admin user and password, introduce authentication
  private val dbConfig : DatabaseConfig[JdbcProfile] = DatabaseConfig.forConfig("db.postgres")

  override implicit val db : JdbcProfile#Backend#Database = dbConfig.db
  override implicit val driver : JdbcProfile = dbConfig.driver
}
