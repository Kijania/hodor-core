package com.core.dal

import com.BaseSpec
import com.core.utils.db.DatabaseConnection
import com.core_api.dao.EventDao
import com.core_api.dto.EventDto
import slick.backend.DatabaseConfig
import slick.driver.JdbcProfile
import slick.lifted.TableQuery

class BaseDalSpec extends BaseSpec {

  "The Event Data Access Layer" should {

    "insert an event to the database" in new Fixture {

    }

    "find all events in the database" in new Fixture {

    }

    "find the newest event in the database" in new Fixture {

    }

  }

  class Fixture extends DatabaseConnection {

    private val dbConfig : DatabaseConfig[JdbcProfile] = DatabaseConfig.forConfig("db.h2")

    override implicit val db : JdbcProfile#Backend#Database = dbConfig.db
    override implicit val driver : JdbcProfile = dbConfig.driver

    val eventDal = new BaseDalImpl[EventDao, EventDto](TableQuery[EventDao]) {}



  }
}
