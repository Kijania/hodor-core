package com.core.persistence

import akka.actor.{Actor, Props}
import com.core.persistence.EventPersistenceActor._
import com.core.dal.{BaseDal, BaseDalImpl}
import com.core.utils.db.DatabaseConnectionImpl
import com.core_api.dao.EventDao
import com.core_api.dto.{Event, EventDto}
import slick.lifted.TableQuery

class EventPersistenceActor(dal: BaseDal[EventDao, EventDto]) extends Actor with DatabaseConnectionImpl {

  override def preStart(): Unit = {
    val dal = new BaseDalImpl[EventDao, EventDto](TableQuery[EventDao])
    dal.createTable()
  }

  // overriding postRestart to disable the call to preStart()
  override def postRestart(reason: Throwable): Unit = {}

  override def receive: Receive = {
    case GetAllEvents =>
      sender ! dal.findByFilter(_ => true)

    case GetEvent(eventId: Long) =>
      sender ! dal.findById(eventId)

    case AddEvent(event: Event) =>
      sender ! dal.insert(event.dto)

    case EditEvent(eventDto: EventDto) =>
      sender ! dal.update(eventDto)

    // TODO case d: DeleteEvent =>
  }
}

object EventPersistenceActor {

  val tableQuery = TableQuery[EventDao]

  def props(): Props = Props(classOf[EventPersistenceActor])

  case object GetAllEvents
  case class GetEvent(id: Long)
  case class EditEvent(eventDto: EventDto)
  case class AddEvent(event: Event)
  case class DeleteEvent(id: String)
}
