package com.core.persistence

import akka.actor.{Actor, Props}
import com.core.persistence.EventPersistenceActor.{AddEvent, GetAllEvents}
import com.core_api.dto.EventDto

class EventPersistenceActorStub extends Actor {

  var list = List[EventDto]()
  var idCounter: Long = 0

  override def receive: Receive = {
    case GetAllEvents =>
      sender ! list

    case AddEvent(eventDto: EventDto) =>
      list = eventDto :: list
      idCounter += 1
      sender ! idCounter
  }
}

object EventPersistenceActorStub {
  def props(): Props = Props(classOf[EventPersistenceActorStub])
}
