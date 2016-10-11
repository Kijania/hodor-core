package com.core

import akka.http.scaladsl.model.{HttpEntity, MediaTypes}
import org.scalatest.{Matchers, WordSpec}
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.testkit.ScalatestRouteTest
import com.core_api.dto.{Event, EventDto}
import spray.json.{JsObject, JsValue}

class HodorServiceSpec extends WordSpec with Matchers with ScalatestRouteTest with HodorService {

  "The HodorService" should {

    "show an empty list of events" in {
      Get("/events") ~> route ~> check {
        status shouldBe OK
        responseAs[String] shouldEqual "[]"
      }
    }

    "accept a valid event and show it" in {

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

    "show specific event" in {

      val eventString =
        s"""
           |{
           |  "name": "Women's Day",
           |  "date": "2016-12-06T17:31:56+01:00",
           |  "text": "Do not forget to bring flowers!",
           |  "mail": "some@mail.com"
           |}
         """.stripMargin

      val httpEntity = HttpEntity(MediaTypes.`application/json`, eventString)

      val eventId = "event1"
      val event = EventDto(eventId,
        Event("Women's Day",
          parse("2016-12-06T17:31:56+01:00"),
          Some("Do not forget to bring flowers!"),
          Some("some@mail.com")
        ))

      Put(s"/events/$eventId", httpEntity) ~> route ~> check {
        status shouldBe OK
      }
      Get(s"/events/$eventId") ~> route ~> check {
        status shouldBe OK
        responseAs[EventDto] shouldEqual event
      }
    }
  }
}
