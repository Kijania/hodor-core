package com.core.routes

import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.model.{HttpEntity, MediaTypes}
import akka.http.scaladsl.testkit.ScalatestRouteTest
import com.BaseSpec
import com.core.persistence.EventPersistenceActorStub
import com.core_api.dto.{Event, EventDto}
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import com.core_api.dto.EventJsonProtocol._
import com.core_api.utils.DateTimeParser._

class EventRoutesSpec extends BaseSpec with ScalatestRouteTest {

  "The EventRoutes" should {

    import Fixture._

    "show an empty list of events" in {

      Get("/events") ~> routes.route ~> check {
        status shouldBe OK
        responseAs[List[EventDto]] shouldEqual List()
      }
      Get(s"/events/1") ~> routes.route ~> check {
        status shouldBe NotFound
      }
    }

    "post a valid event and show it" in {

      val eventId = "1"

      Post("/events", httpEntity) ~> routes.route ~> check {
        status shouldBe Created
        responseAs[String] shouldEqual eventId
      }
      Get ("/events") ~> routes.route ~> check {
        status shouldBe OK
        responseAs[List[EventDto]].map(_.event) shouldBe List(event)
      }
      Get(s"/events/$eventId") ~> routes.route ~> check {
        status shouldBe OK
        responseAs[EventDto].event shouldBe event
      }
    }

    pending
    "put an event with defined id, get it and update it" in {

      val eventId = "2"

      Post("/events", httpEntity) ~> routes.route ~> check {
        status shouldBe Created
        responseAs[String] shouldEqual eventId
      }
      Get(s"/events/$eventId") ~> routes.route ~> check {
        status shouldBe OK
        responseAs[EventDto].event shouldEqual event
      }
      Put(s"/events/$eventId", httpUpdatedEntity) ~> routes.route ~> check {
        status shouldBe OK
        responseAs[EventDto].event shouldEqual updatedEvent
      }
      Get(s"/events/$eventId") ~> routes.route ~> check {
        status shouldBe OK
        responseAs[EventDto].event shouldEqual updatedEvent
      }
    }

    pending
    "delete an event" in {

      val eventId = "3"

      Post(s"/events", httpEntity) ~> routes.route ~> check {
        status shouldBe Created
        responseAs[String] shouldEqual eventId
      }
      Delete(s"/events/$eventId") ~> routes.route ~> check {
        status shouldBe NoContent
      }
      Delete(s"/events/$eventId") ~> routes.route ~> check {
        status shouldBe NotFound
      }
      Get(s"/events/$eventId") ~> routes.route ~> check {
        status shouldBe NotFound
      }
    }
  }

  // TODO manage closing resources in Fixture, to get rid of memory leak
  object Fixture {

    val eventPersistenceActorStub = system.actorOf(EventPersistenceActorStub.props())
    val routes = EventRoutes(eventPersistenceActorStub)

    val eventString =
      s"""
         |{
         |  "name": "Nicolas Nameday",
         |  "date": "2016-12-06T17:31:56+01:00"
         |}
         """.stripMargin

    val httpEntity = HttpEntity(MediaTypes.`application/json`, eventString)
    val event = Event("Nicolas Nameday", parseDateTime("2016-12-06T17:31:56+01:00"))

    val updatedEventString =
      s"""
         |{
         |  "name": "Women's Day",
         |  "date": "2016-03-08T17:31:56+01:00",
         |  "text": "Do not forget to bring flowers!",
         |  "mail": "some@mail.com"
         |}
         """.stripMargin

    val httpUpdatedEntity = HttpEntity(MediaTypes.`application/json`, updatedEventString)
    val updatedEvent = Event(
      "Women's Day",
      parseDateTime("2016-03-08T17:31:56+01:00"),
      Some("Do not forget to bring flowers!"),
      Some("some@mail.com")
    )
  }
}
