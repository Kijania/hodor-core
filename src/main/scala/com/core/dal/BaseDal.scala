package com.core.dal

import com.core.utils.db.DatabaseConnection
import com.core_api.dao.BaseDao
import com.core_api.dto.BaseDto
import slick.lifted.{CanBeQueryCondition, TableQuery}

import scala.concurrent.Future

trait BaseDal[T, A] {
  def insert(row: A): Future[String]
  // TODO def update(row: A): Future[String]
  // TODO def findById(id: String): Future[Option[A]]
  def findByFilter[C: CanBeQueryCondition](f: (T) => C): Future[Seq[A]]
  // TODO def deleteById(id: String): Future[String]
}

class BaseDalImpl[T <: BaseDao[A], A <: BaseDto](tableQuery: TableQuery[T]) extends BaseDal[T, A] with DatabaseConnection {

  import driver.api._

  override def insert(row: A): Future[String] = {
    println(s"------inserting row: $row------")
    db.run(tableQuery returning tableQuery.map(_.id) += row)
  }

  override def findByFilter[C: CanBeQueryCondition](f: (T) => C): Future[Seq[A]] = {
    println(s"------run condition: $f on the database: $db------")
    db.run(tableQuery.withFilter(f).result)
  }

}