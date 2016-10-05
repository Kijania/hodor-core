package com.core

import akka.http.scaladsl.model.{HttpEntity, MediaTypes}
import org.scalatest.{Matchers, WordSpec}
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.testkit.ScalatestRouteTest
import com.core_api.Event

class HodorServiceSpec extends WordSpec with Matchers with ScalatestRouteTest with HodorService {

  "The HodorService" should {

    "show an empty list of events" in {
      Get("/events") ~> route ~> check {
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
        status shouldBe OK
      }
      Get ("/events") ~> route ~> check {
        responseAs[List[Event]] shouldEqual List(event)
      }
    }
  }
}
