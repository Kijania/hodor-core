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
// TODO replace with defined context
import scala.concurrent.ExecutionContext.Implicits.global

import scala.util.{Failure, Success}
import scala.concurrent.duration._

case class EventRoutes(eventPersistenceActor: ActorRef) {

  implicit val timeout: Timeout = 3 seconds
  var list = List[EventDto]()
  val id = """[a-zA-Z0-9_]+""".r

  val route =
    path("events") {
      post {
        entity(as[Event]) { event =>
          val eventDto = event.dto(UUID.randomUUID().toString)

          println(s"---got event: $event, send as an eventdto: $eventDto---")
          eventPersistenceActor ! AddEvent(eventDto)
          complete(Created -> eventDto)
        }
      } ~
      get {
//        val allEvents = (eventPersistenceActor ? GetAllEvents).mapTo[Seq[EventDto]]
//        allEvents.onComplete {
        onComplete((eventPersistenceActor ? GetAllEvents).mapTo[Seq[EventDto]]) {
          case Success(events) => println(s"---get events---"); complete(OK -> events)
          case Failure(ex) => println(s"---not get events, get error: ${ex.getMessage}---"); complete(NotFound -> ex.getMessage)
        }
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
          val eventDto = event.dto(eventId)
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
