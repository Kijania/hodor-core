package com.core.dal

import com.BaseSpec
import com.core.utils.db.DatabaseConnection
import com.core_api.dao.EventDao
import com.core_api.dto.{Event, EventDto}
import com.core_api.utils.DateTimeParser._
import slick.backend.DatabaseConfig
import slick.driver.JdbcProfile
import slick.lifted.TableQuery

import scala.concurrent.Await
import scala.concurrent.duration._

class BaseDalSpec extends BaseSpec with DatabaseConnection {

  private val dbConfig : DatabaseConfig[JdbcProfile] = DatabaseConfig.forConfig("db.h2")

  override implicit val db : JdbcProfile#Backend#Database = dbConfig.db
  override implicit val driver : JdbcProfile = dbConfig.driver

  import CommonFixture._

  "The Event Data Access Layer" should {

    "find nothing in empty database" in {
      val events = Await.result(eventDal.findByFilter(_ => true), timeout)
      val event = Await.result(eventDal.findById(1L), timeout)

      events shouldBe List()
      event shouldBe None
    }

    "insert an event and find it" in {
      val eventId = Await.result(eventDal.insert(event.dto), timeout)
      val events = Await.result(eventDal.findByFilter(_ => true), timeout)
      val theEvent = Await.result(eventDal.findById(eventId), timeout)

      eventId shouldEqual 1L
      events.map(_.event) shouldBe List(event)
      theEvent.map(_.event) shouldBe Some(event)
    }

    "not be able to update not existing event" in {
      val notExistingEvent = Await.result(eventDal.update(updatedEvent.dto(-1L)), timeout)

      notExistingEvent shouldBe None
    }

    "update an event" in {
      val eventId = Await.result(eventDal.insert(event.dto), timeout)
      val theUpdatedEvent = Await.result(eventDal.update(updatedEvent.dto(eventId)), timeout)

      theUpdatedEvent shouldEqual Some(updatedEvent.dto(eventId))
    }

  }

  object CommonFixture {

    implicit val timeout = 5 seconds
    val eventDal = new BaseDalImpl[EventDao, EventDto](TableQuery[EventDao]) {}
    Await.result(eventDal.createTable(), timeout)

    val event = Event("Nicolas Nameday", parseDateTime("2016-12-06T17:31:56+01:00"))

    val updatedEvent = Event(
    "Women's Day",
        parseDateTime("2016-03-08T17:31:56+01:00"),
        Some("Do not forget to bring flowers!"),
        Some("some@mail.com")
    )
  }
}
