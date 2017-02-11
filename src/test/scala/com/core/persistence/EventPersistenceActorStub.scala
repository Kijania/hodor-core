package com.core.persistence

import akka.actor.{Actor, Props}
import com.core.persistence.EventPersistenceActor._
import com.core_api.dto.{Event, EventDto}

class EventPersistenceActorStub extends Actor {

  var list = List[EventDto]()
  var idCounter: Long = 0

  override def receive: Receive = {
    case GetAllEvents =>
      sender ! list

    case GetEvent(eventId: Long) =>
      sender ! list.find(_.id == eventId)

    case AddEvent(event: Event) =>
      idCounter += 1
      list = event.dto(idCounter) :: list
      sender ! idCounter

    case EditEvent(eventDto: EventDto) =>
      val oldEvent = list.find(_.id == eventDto.id)
      list = oldEvent match {
        case Some(theEventDto) => eventDto :: list.filterNot(_.id == eventDto.id)
        case None => list
      }
      sender ! oldEvent.map(_ => eventDto)

    case DeleteEvent(id: Long) =>
      list.find(_.id == id) match {
        case Some(_) =>
          list = list.filterNot(_.id == id)
          sender ! Some(id)
        case None =>
          sender ! None
      }
  }
}

object EventPersistenceActorStub {
  def props(): Props = Props(classOf[EventPersistenceActorStub])
}
