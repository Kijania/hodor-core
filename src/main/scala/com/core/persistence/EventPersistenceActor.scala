package com.core.persistence

import akka.actor.{Actor, Props}
import com.core.persistence.EventPersistenceActor._
import com.core.dal.{BaseDal, BaseDalImpl}
import com.core_api.dao.EventDao
import com.core_api.dto.EventDto
import slick.lifted.{CanBeQueryCondition, TableQuery}
import akka.pattern.pipe
import com.core.utils.db.DatabaseConnection

import scala.concurrent.Future
// TODO replace with defined context
import scala.concurrent.ExecutionContext.Implicits.global

class EventPersistenceActor(dal: BaseDal[EventDao, EventDto]) extends Actor {

  override def receive: Receive = {
    case GetAllEvents =>
      println(s"------asked to get all events from: $dal------")
//      dal.findByFilter(_ => true) pipeTo sender
      sender ! dal.findByFilter(_ => true)


    // TODO case event: GetEvent =>
//      Event(event.id, DateTime.now)
    // TODO case e: EditEvent =>
//      e

    case AddEvent(eventDto: EventDto) =>
      println(s"------get eventdto: $eventDto------")
      dal.insert(eventDto)

    // TODO case d: DeleteEvent =>
//      Event(d.id, DateTime.now)
  }

//  def findByFilter[C: CanBeQueryCondition](f: (EventDao) => C): Future[Seq[EventDto]] = {
//    println(s"------run condition: $f on the database: $db------")
//    db.run(tableQuery.withFilter(f).result)
//  }
}

object EventPersistenceActor {

  val tableQuery = TableQuery[EventDao]

  lazy val dal = new BaseDalImpl[EventDao, EventDto](TableQuery[EventDao])

  def props(): Props = Props(classOf[EventPersistenceActor], dal)
  def props(dal: BaseDal[EventDao, EventDto]) = Props(classOf[EventPersistenceActor], dal)

  case object GetAllEvents
  case class GetEvent(id: String)
  case class EditEvent(event: EventDto)
  case class AddEvent(event: EventDto)
  case class DeleteEvent(id: String)
}
