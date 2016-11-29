package com.core.persistence

import akka.actor.{Actor, Props}
import com.core.persistence.EventPersistenceActor.{AddEvent, GetAllEvents, GetEvent}
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
  }
}

object EventPersistenceActorStub {
  def props(): Props = Props(classOf[EventPersistenceActorStub])
}
