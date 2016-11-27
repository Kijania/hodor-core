package com.core_api.dao

import slick.lifted.Tag
import slick.driver.PostgresDriver.api._

abstract class BaseDao[T](tag: Tag, name: String) extends Table[T](tag, name) {
  def id = column[String]("id", O.PrimaryKey, O.AutoInc)
}
