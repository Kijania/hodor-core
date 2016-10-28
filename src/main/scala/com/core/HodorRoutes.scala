package com.core

import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.server.Directives._
import com.core_api.dto.{Event, EventDto}
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import com.core_api.dto.EventJsonProtocol._
import java.util.UUID

case class HodorRoutes(service: HodorService) {

  var list = List[EventDto]()
  val id = """[a-zA-Z0-9_]+""".r

  val route =
    path("events") {
      post {
        entity(as[Event]) { event =>
          val eventDto = EventDto(UUID.randomUUID().toString, event)
          list = eventDto :: list
          complete(Created -> eventDto)
        }
      } ~
      get {
        complete(list)
      }
    } ~
    path("events" / id) { eventId =>
      get {
        list.find(_.id == eventId)
          .map(eventDto => complete(eventDto))
          .getOrElse(complete(NotFound))
      } ~
      put {
        entity(as[Event]) { event =>
          val eventDto = EventDto(eventId, event)
          list.find(_.id == eventId) match {
            case Some(oldEvent) =>
              val index = list.indexOf(oldEvent)
              list = list.updated[EventDto, List[EventDto]](index, eventDto)
            case None =>
              list = eventDto :: list
          }
          complete(eventDto)
        }
      } ~
      delete {
        list.find(_.id == eventId) match {
          case Some(_) =>
            list = list.filterNot(_.id == eventId)
            complete(NoContent)
          case None =>
            complete(NotFound)
        }
      }
    }
}
