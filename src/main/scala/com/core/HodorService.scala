package com.core

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.server.Directives._
import com.core_api.dto.{Event, EventDto, EventJsonProtocol}
import java.util.UUID

trait HodorService extends EventJsonProtocol {

  implicit val system: ActorSystem
  implicit val materializer: ActorMaterializer

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
          val eventDto = list.find(_.id == eventId).getOrElse {
            val eventDto = EventDto(eventId, event)
            list = eventDto :: list
            eventDto
          }
          complete(eventDto)
        }
      }
    }
}
