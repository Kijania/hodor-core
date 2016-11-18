package com.core.persistence

import akka.actor.{Actor, Props}
import com.core.persistence.EventPersistenceActor._
import com.core.dal.{BaseDal, BaseDalImpl}
import com.core_api.dao.EventDao
import com.core_api.dto.EventDto
import slick.lifted.TableQuery

class EventPersistenceActor(dal: BaseDal[EventDao, EventDto]) extends Actor {

  override def receive: Receive = {
    case GetAllEvents =>
      sender ! dal.findByFilter(_ => true)

    // TODO case event: GetEvent =>

    case AddEvent(eventDto: EventDto) =>
      dal.insert(eventDto)

    // TODO case e: EditEvent =>

    // TODO case d: DeleteEvent =>
  }
}

object EventPersistenceActor {

  val tableQuery = TableQuery[EventDao]

  lazy val dal = new BaseDalImpl[EventDao, EventDto](TableQuery[EventDao])

  def props(): Props = Props(classOf[EventPersistenceActor], dal)

  case object GetAllEvents
  case class GetEvent(id: String)
  case class EditEvent(event: EventDto)
  case class AddEvent(event: EventDto)
  case class DeleteEvent(id: String)
}
