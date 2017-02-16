package com.core.routes

import javax.ws.rs.Path

import akka.actor.ActorRef
import akka.event.Logging
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.pattern.ask
import akka.util.Timeout
import com.core.persistence.EventPersistenceActor._
import com.core_api.dto.EventJsonProtocol._
import com.core_api.dto.{Event, EventDto}
import io.swagger.annotations._

import scala.concurrent.duration._
import scala.util.{Failure, Success}

@Path("/events")
@Api(value = "/events")
case class EventRoutes(eventPersistenceActor: ActorRef) {

  implicit val timeout: Timeout = 3 seconds
  var list = List[EventDto]()

  val routes: Route =
    postEvent ~
      getEvents ~
      getEvent ~
      putEvent ~
      deleteEvent()

  @ApiOperation(value = "Add Event", httpMethod = "POST", consumes = "application/json", produces = "text/plain; charset=UTF-8")
  @ApiImplicitParams(Array(
    new ApiImplicitParam(name = "event body", value = "Event Object", required = true,
      dataType = "com.core_api.dto.Event", paramType = "body")
  ))
  @ApiResponses(Array(
    new ApiResponse(code = 201, message = "Event created"),
    new ApiResponse(code = 500, message = "Invalid event or Internal Server Error")
  ))
  def postEvent =
    logRequestResult("EventRoutes", Logging.InfoLevel) {
      path("events") {
        post {
          entity(as[Event]) { event =>
            onComplete((eventPersistenceActor ? AddEvent(event)).mapTo[Long]) {
              case Success(eventId) => complete(Created -> eventId.toString)
              case Failure(ex) => complete(InternalServerError -> ex.getMessage)
            }
          }
        }
      }
    }

  @ApiOperation(value = "Return Events", httpMethod = "GET", produces = "application/json")
  @ApiResponses(Array(
    new ApiResponse(code = 200, message = "Ok", response = classOf[Seq[EventDto]]),
    new ApiResponse(code = 500, message = "Internal server error")
  ))
  def getEvents =
    logRequestResult("EventRoutes", Logging.InfoLevel) {
      path("events") {
        get {
          onComplete((eventPersistenceActor ? GetAllEvents).mapTo[Seq[EventDto]]) {
            case Success(events) => complete(OK -> events)
            case Failure(ex) => complete(InternalServerError -> ex.getMessage)
          }
        }
      }
    }

  @Path("/{id}")
  @ApiOperation(value = "Return Event", httpMethod = "GET", produces = "application/json")
  @ApiImplicitParams(Array(
    new ApiImplicitParam(name = "id", value = "Event Id", required = true, dataType = "long", paramType = "path")
  ))
  @ApiResponses(Array(
    new ApiResponse(code = 200, message = "Ok", response = classOf[EventDto]),
    new ApiResponse(code = 400, message = "The event id must be greater than zero"),
    new ApiResponse(code = 404, message = "The event not found"),
    new ApiResponse(code = 500, message = "Internal server error")
  ))
  def getEvent =
    logRequestResult("EventRoutes", Logging.InfoLevel) {
      path("events" / LongNumber) { eventId =>
        get {
          validate(eventId > 0, "The event id must be greater than zero") {
            onComplete((eventPersistenceActor ? GetEvent(eventId)).mapTo[Option[EventDto]]) {
              case Success(eventDtoOption) => eventDtoOption match {
                case Some(eventDto) => complete(OK -> eventDto)
                case None => complete(NotFound -> "The event does not exist")
              }
              case Failure(ex) => complete(InternalServerError -> ex.getMessage)
            }
          }
        }
      }
    }

  @Path("/{id}")
  @ApiOperation(value = "Edit Event", httpMethod = "PUT", produces = "application/json")
  @ApiImplicitParams(Array(
    new ApiImplicitParam(name = "id", value = "Event Id", required = true, dataType = "long", paramType = "path"),
    new ApiImplicitParam(name = "event body", value = "Event Object", required = true,
      dataType = "com.core_api.dto.Event", paramType = "body")
  ))
  @ApiResponses(Array(
    new ApiResponse(code = 200, message = "Ok", response = classOf[EventDto]),
    new ApiResponse(code = 400, message = "The event id must be greater than zero"),
    new ApiResponse(code = 404, message = "The event not found"),
    new ApiResponse(code = 500, message = "Internal server error")
  ))
  def putEvent =
    logRequestResult("EventRoutes", Logging.InfoLevel) {
      path("events" / LongNumber) { eventId =>
        put {
          validate(eventId > 0, "The event id must be greater than zero") {
            entity(as[Event]) { event =>
              onComplete((eventPersistenceActor ? EditEvent(event.dto(eventId))).mapTo[Option[EventDto]]) {
                case Success(eventDtoOption) => eventDtoOption match {
                  case Some(theEventDto) =>
                    complete(OK -> theEventDto)
                  case None =>
                    complete(NotFound -> "The event meant to update does not exist")
                }
                case Failure(ex) => complete(InternalServerError -> ex.getMessage)
              }
            }
          }
        }
      }
    }

  @Path("/{id}")
  @ApiOperation(value = "Delete Event", httpMethod = "DELETE", produces = "text/plain; charset=UTF-8")
  @ApiImplicitParams(Array(
    new ApiImplicitParam(name = "id", value = "Event Id", required = true, dataType = "long", paramType = "path")
  ))
  @ApiResponses(Array(
    new ApiResponse(code = 204, message = "The event has been deleted"),
    new ApiResponse(code = 400, message = "The event id must be greater than zero"),
    new ApiResponse(code = 404, message = "Event not found"),
    new ApiResponse(code = 500, message = "Internal server error")
  ))
  def deleteEvent() =
    logRequestResult("EventRoutes", Logging.InfoLevel) {
      path("events" / LongNumber) { eventId =>
        delete {
          validate(eventId > 0, "The event id must be greater than zero") {
            onComplete((eventPersistenceActor ? DeleteEvent(eventId)).mapTo[Option[Long]]) {
              case Success(id) => id match {
                case Some(_) =>
                  complete(NoContent)
                case None =>
                  complete(NotFound)
              }
              case Failure(ex) => complete(InternalServerError -> ex.getMessage)
            }
          }
        }
      }
    }
}
