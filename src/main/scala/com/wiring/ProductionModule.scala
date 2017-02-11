package com.wiring

import akka.actor.ActorSystem
import com.core.CoreServer
import com.core.dal.{BaseDal, BaseDalImpl}
import com.core.persistence.EventPersistenceActor
import com.core.routes.{EventRoutes, Routes}
import com.core.swagger.SwaggerDocService
import com.core.utils.Configuration
import com.core.utils.db.PostgresDatabaseConnection
import com.core_api.dao.EventDao
import com.core_api.dto.EventDto
import slick.lifted.TableQuery

object ProductionModule {

  val actorSystem = ActorSystem("core-server")

  val configuration = new Configuration

  val postgresDatabaseConnection = new PostgresDatabaseConnection
  val eventDal: BaseDal[EventDao, EventDto] = new BaseDalImpl[EventDao, EventDto](
    TableQuery[EventDao])(
    postgresDatabaseConnection.db, postgresDatabaseConnection.driver
  )
  val eventCtrl = actorSystem.actorOf(EventPersistenceActor.props(eventDal))

  val eventRoutes = new EventRoutes(eventCtrl).routes
  val swaggerDocService = new SwaggerDocService(actorSystem, configuration)

  val routes = new Routes(eventRoutes, swaggerDocService).routes

  val coreServer = new CoreServer(actorSystem, routes, configuration)
}
