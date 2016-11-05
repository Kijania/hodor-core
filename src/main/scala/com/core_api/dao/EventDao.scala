package com.core_api.dao

import com.core_api.dto.EventDto
import com.github.nscala_time.time.Imports._
import slick.lifted.Tag
import slick.driver.PostgresDriver.api._
import com.github.tototoshi.slick.PostgresJodaSupport._

class EventDao(tag: Tag) extends BaseDao[EventDto](tag, "EVENTS") {
  def name = column[String]("name")
  def date = column[DateTime]("date")
  def text = column[Option[String]]("text")
  def mail = column[Option[String]]("mail")

  def * = (id, name, date, text, mail) <> (EventDto.tupled, EventDto.unapply)
}