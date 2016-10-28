package com.core

import akka.http.scaladsl.model.{HttpEntity, MediaTypes}
import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.testkit.ScalatestRouteTest
import com.BaseSpec
import com.core_api.dto.{Event, EventDto}
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import com.core_api.dto.EventJsonProtocol._
import com.core_api.utils.DateTimeParser._

class HodorRoutesSpec extends BaseSpec with ScalatestRouteTest {

  "The HodorRoutes" should {

    "show an empty list of events" in new Fixture {

      Get("/events") ~> routes.route ~> check {
        status shouldBe OK
        responseAs[String] shouldEqual "[]"
      }
    }

    "post a valid event and show it" in new Fixture {

      Post("/events", httpEntity) ~> routes.route ~> check {
        status shouldBe Created
        responseAs[EventDto].event shouldBe event
      }
      Get ("/events") ~> routes.route ~> check {
        responseAs[List[EventDto]].map(_.event) shouldBe List(event)
      }
    }

    "put an event with defined id, get it and update it" in new Fixture {

      Put(s"/events/$eventId", httpEntity) ~> routes.route ~> check {
        status shouldBe OK
        responseAs[EventDto] shouldEqual eventDto
      }
      Get(s"/events/$eventId") ~> routes.route ~> check {
        status shouldBe OK
        responseAs[EventDto] shouldEqual eventDto
      }
      Put(s"/events/$eventId", httpUpdatedEntity) ~> routes.route ~> check {
        status shouldBe OK
        responseAs[EventDto] shouldEqual updatedEventDto
      }
      Get(s"/events/$eventId") ~> routes.route ~> check {
        status shouldBe OK
        responseAs[EventDto] shouldEqual updatedEventDto
      }
    }

    "delete and event" in new Fixture {

      Put(s"/events/$eventId", httpEntity) ~> routes.route ~> check {
        status shouldBe OK
        responseAs[EventDto] shouldEqual eventDto
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
  class Fixture {
    val service = mock[HodorService]
    val routes = HodorRoutes(service)

    val eventString =
      s"""
         |{
         |  "name": "Nicolas Nameday",
         |  "date": "2016-12-06T17:31:56+01:00"
         |}
         """.stripMargin

    val httpEntity = HttpEntity(MediaTypes.`application/json`, eventString)
    val event = Event("Nicolas Nameday", parseDateTime("2016-12-06T17:31:56+01:00"))

    val eventId = "event1"
    val eventDto = EventDto(eventId, event)

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
    val updatedEventDto = EventDto(eventId,
      Event("Women's Day",
        parseDateTime("2016-03-08T17:31:56+01:00"),
        Some("Do not forget to bring flowers!"),
        Some("some@mail.com")
      ))
  }
}
