package com.core

import akka.http.scaladsl.model.{HttpEntity, MediaTypes}
import org.scalatest.{Matchers, WordSpec}
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.testkit.ScalatestRouteTest
import com.core_api.dto.{Event, EventDto}

class HodorServiceSpec extends WordSpec with Matchers with ScalatestRouteTest with HodorService {

  "The HodorService" should {

    "show an empty list of events" in {
      Get("/events") ~> route ~> check {
        status shouldBe OK
        responseAs[String] shouldEqual "[]"
      }
    }

    "post a valid event and show it" in {

      val eventString =
        s"""
           |{
           |  "name": "Nicolas Nameday",
           |  "date": "2016-12-06T17:31:56+01:00"
           |}
         """.stripMargin

      val httpEntity = HttpEntity(MediaTypes.`application/json`, eventString)
      val event = Event("Nicolas Nameday", parse("2016-12-06T17:31:56+01:00"))

      Post("/events", httpEntity) ~> route ~> check {
        status shouldBe Created
        responseAs[EventDto].event shouldBe event
      }
      Get ("/events") ~> route ~> check {
        responseAs[List[EventDto]].map(_.event) shouldBe List(event)
      }
    }

    "put an event with defined id, get it and update it" in {

      val eventString =
        s"""
           |{
           |  "name": "Women's Day",
           |  "date": "2016-03-08T17:31:56+01:00",
           |  "text": "Do not forget to bring flowers!",
           |  "mail": "some@mail.com"
           |}
         """.stripMargin

      val httpEntity = HttpEntity(MediaTypes.`application/json`, eventString)

      val eventId = "event1"
      val event = EventDto(eventId,
        Event("Women's Day",
          parse("2016-03-08T17:31:56+01:00"),
          Some("Do not forget to bring flowers!"),
          Some("some@mail.com")
        ))

      val updatedEventString =
        s"""
           |{
           |  "name": "Men's Day at Romania",
           |  "date": "2016-03-09T17:31:56+01:00"
           |}
         """.stripMargin
      val httpUpdatedEntity = HttpEntity(MediaTypes.`application/json`, updatedEventString)

      val updatedEvent = EventDto(eventId,
        Event("Men's Day at Romania",parse("2016-03-09T17:31:56+01:00")))

      Put(s"/events/$eventId", httpEntity) ~> route ~> check {
        status shouldBe OK
        responseAs[EventDto] shouldEqual event
      }
      Get(s"/events/$eventId") ~> route ~> check {
        status shouldBe OK
        responseAs[EventDto] shouldEqual event
      }
      Put(s"/events/$eventId", httpUpdatedEntity) ~> route ~> check {
        status shouldBe OK
        responseAs[EventDto] shouldEqual updatedEvent
      }
      Get(s"/events/$eventId") ~> route ~> check {
        status shouldBe OK
        responseAs[EventDto] shouldEqual updatedEvent
      }

    }

//    "update an event "
  }
}
