package com.core.dal

import com.core.utils.db.DatabaseConnection
import com.core_api.dao.BaseDao
import com.core_api.dto.BaseDto
import slick.driver.JdbcProfile
import slick.lifted.{CanBeQueryCondition, TableQuery}
import com.core.utils.ExecutionContexts.databaseExecutionContext

import scala.concurrent.Future

trait BaseDal[T, A] {
  def insert(row: A): Future[Long]
  def update(row: A): Future[Option[A]]
  def findById(id: Long): Future[Option[A]]
  def findByFilter[C: CanBeQueryCondition](f: (T) => C): Future[Seq[A]]
  def deleteById(id: Long): Future[Option[Long]]
  def createTable(): Future[Unit]
}

class BaseDalImpl[T <: BaseDao[A], A <: BaseDto](entities: TableQuery[T])
                                                (implicit val db: JdbcProfile#Backend#Database, implicit val driver: JdbcProfile)
  extends BaseDal[T, A] with DatabaseConnection {

  import driver.api._

  override def insert(row: A): Future[Long] = {
    db.run(entities returning entities.map(_.id) += row)
  }

  override def findById(id: Long): Future[Option[A]] = {
    db.run(entities.filter(_.id === id).result.headOption)
  }

  override def findByFilter[C: CanBeQueryCondition](f: (T) => C): Future[Seq[A]] = {
    db.run(entities.withFilter(f).result)
  }

  override def update(row: A): Future[Option[A]] = {
    db.run { entities.filter(_.id === row.id).update(row) map {
      case 0 => None
      case _ => Some(row)
    }}
  }

  override def deleteById(id: Long): Future[Option[Long]] = {
    db.run { entities.filter(_.id === id).delete map {
      case 0 => None
      case _ => Some(id)
    }}
  }


  override def createTable(): Future[Unit] = {
    db.run(DBIO.seq(entities.schema.create))
  }
}