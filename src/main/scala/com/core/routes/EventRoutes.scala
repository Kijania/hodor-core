package com.core.routes

import java.util.UUID

import akka.actor.ActorRef
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.server.Directives._
import akka.pattern.ask
import akka.util.Timeout
import com.core.persistence.EventPersistenceActor.{AddEvent, GetAllEvents}
import com.core_api.dto.EventJsonProtocol._
import com.core_api.dto.{Event, EventDto}

import scala.util.{Failure, Success}
import scala.concurrent.duration._

case class EventRoutes(eventPersistence: ActorRef) {

  implicit val timeout: Timeout = 3 seconds
  var list = List[EventDto]()
  val id = """[a-zA-Z0-9_]+""".r

  val route =
    path("events") {
      post {
        entity(as[Event]) { event =>
          val eventDto = event.dto(UUID.randomUUID().toString)

          eventPersistence ! AddEvent(eventDto)
          complete(Created -> eventDto)
        }
      } ~
      get {
        onComplete((eventPersistence ? GetAllEvents).mapTo[Seq[EventDto]]) {
          case Success(events) => complete(events)
          case Failure(ex) => complete(NotFound -> ex.getMessage)
        }
      }
    } //~
//    path("events" / id) { eventId =>
//      get {
//        list.find(_.id == eventId)
//          .map(eventDto => complete(eventDto))
//          .getOrElse(complete(NotFound))
//      } ~
//      put {
//        entity(as[Event]) { event =>
//          val eventDto = event.toDto(eventId)
//          list.find(_.id == eventId) match {
//            case Some(oldEvent) =>
//              val index = list.indexOf(oldEvent)
//              list = list.updated[EventDto, List[EventDto]](index, eventDto)
//            case None =>
//              list = eventDto :: list
//          }
//          complete(eventDto)
//        }
//      } ~
//      delete {
//        list.find(_.id == eventId) match {
//          case Some(_) =>
//            list = list.filterNot(_.id == eventId)
//            complete(NoContent)
//          case None =>
//            complete(NotFound)
//        }
//      }
//    }
}