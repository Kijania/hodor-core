package com.core.utils.db

import slick.backend.DatabaseConfig
import slick.driver.JdbcProfile

trait DatabaseConnection {

  private val dbConfig : DatabaseConfig[JdbcProfile] = DatabaseConfig.forConfig("db.postgres")

  // TODO encrypt admin user and password, introduce authentication
  implicit val db : JdbcProfile#Backend#Database = dbConfig.db
  implicit val driver : JdbcProfile = dbConfig.driver
}
