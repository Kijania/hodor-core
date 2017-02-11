package com.wiring

import akka.actor.ActorSystem
import com.core.CoreServer
import com.core.persistence.EventPersistenceActor
import com.core.routes.{EventRoutes, RouteModule}
import com.core.swagger.SwaggerDocService
import com.core.utils.Configuration

object ProductionModule {

  val actorSystem = ActorSystem("core-server")

  val configuration = new Configuration

  val eventCtrl = actorSystem.actorOf(EventPersistenceActor.props())
  val eventRoutes = new EventRoutes(eventCtrl).routes
  val swaggerDocService = new SwaggerDocService(actorSystem, configuration)
  val routes = new RouteModule(eventRoutes, swaggerDocService).routes

  val coreServer = new CoreServer(actorSystem, routes, configuration)
}
