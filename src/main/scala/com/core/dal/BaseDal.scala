package com.core.dal

import com.core.utils.db.DatabaseConnection
import com.core_api.dao.BaseDao
import com.core_api.dto.BaseDto
import slick.driver.JdbcProfile
import slick.lifted.{CanBeQueryCondition, TableQuery}

import scala.concurrent.Future

trait BaseDal[T, A] {
  def insert(row: A): Future[Long]
  // TODO def update(row: A): Future[Long]
  // TODO def findById(id: String): Future[Option[A]]
  def findByFilter[C: CanBeQueryCondition](f: (T) => C): Future[Seq[A]]
  // TODO def deleteById(id: String): Future[Long]
  def createTable(): Future[Unit]
}

class BaseDalImpl[T <: BaseDao[A], A <: BaseDto](tableQuery: TableQuery[T])
                                                (implicit val db: JdbcProfile#Backend#Database, implicit val driver: JdbcProfile)
  extends BaseDal[T, A] with DatabaseConnection {

  import driver.api._

  override def insert(row: A): Future[Long] = {
    db.run(tableQuery returning tableQuery.map(_.id) += row)
  }

  override def findByFilter[C: CanBeQueryCondition](f: (T) => C): Future[Seq[A]] = {
    db.run(tableQuery.withFilter(f).result)
  }

  override def createTable(): Future[Unit] = {
    db.run(DBIO.seq(tableQuery.schema.create))
  }
}