package com.core.persistence

import akka.actor.{Actor, Props}
// TODO replace with defined context
import scala.concurrent.ExecutionContext.Implicits.global
import akka.pattern.pipe
import com.core.persistence.EventPersistenceActor._
import com.core.dal.{BaseDal, BaseDalImpl}
import com.core.utils.db.DatabaseConnectionImpl
import com.core_api.dao.EventDao
import com.core_api.dto.{Event, EventDto}
import slick.lifted.TableQuery

class EventPersistenceActor extends Actor with DatabaseConnectionImpl{

  val dal: BaseDal[EventDao, EventDto] = new BaseDalImpl[EventDao, EventDto](TableQuery[EventDao])

  override def preStart(): Unit = {
    dal.createTable()
  }

  // overriding postRestart to disable the call to preStart()
  override def postRestart(reason: Throwable): Unit = {}

  override def receive: Receive = {
    case GetAllEvents =>
      dal.findByFilter(_ => true) pipeTo sender

    case GetEvent(eventId: Long) =>
      dal.findById(eventId) pipeTo sender

    case AddEvent(event: Event) =>
      dal.insert(event.dto) pipeTo sender

    case EditEvent(eventDto: EventDto) =>
      dal.update(eventDto) pipeTo sender

    case DeleteEvent(id: Long) =>
      dal.deleteById(id) pipeTo sender
  }
}

object EventPersistenceActor {

  def props(): Props = Props(classOf[EventPersistenceActor])

  case object GetAllEvents
  case class GetEvent(id: Long)
  case class EditEvent(eventDto: EventDto)
  case class AddEvent(event: Event)
  case class DeleteEvent(id: Long)
}
