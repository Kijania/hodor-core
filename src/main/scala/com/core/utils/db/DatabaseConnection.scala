package com.core.utils.db

import slick.backend.DatabaseConfig
import slick.driver.JdbcProfile

trait DatabaseConnection {

  // TODO encrypt admin user and password, introduce authentication
  private val dbConfig : DatabaseConfig[JdbcProfile] = DatabaseConfig.forConfig("db.postgres")

  implicit val db : JdbcProfile#Backend#Database = dbConfig.db
  implicit val driver : JdbcProfile = dbConfig.driver
}
