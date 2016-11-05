package com.core.persistence

import akka.actor.{Actor, Props}
import com.core.persistence.EventPersistenceActor._
import com.core.dal.BaseDalImpl
import com.core_api.dao.EventDao
import com.core_api.dto.EventDto
import slick.lifted.TableQuery

class EventPersistenceActor extends Actor {

  val dal = new BaseDalImpl[EventDao, EventDto](TableQuery[EventDao])

  override def receive: Receive = {
    case GetAllEvents =>
      dal.findByFilter(_ => true)


    // TODO case event: GetEvent =>
//      Event(event.id, DateTime.now)
    // TODO case e: EditEvent =>
//      e

    case AddEvent(eventDto: EventDto) =>
      dal.insert(eventDto)

    // TODO case d: DeleteEvent =>
//      Event(d.id, DateTime.now)
  }
}

object EventPersistenceActor {

  def props(): Props = Props(classOf[EventPersistenceActor])

  case object GetAllEvents
  case class GetEvent(id: String)
  case class EditEvent(event: EventDto)
  case class AddEvent(event: EventDto)
  case class DeleteEvent(id: String)
}
