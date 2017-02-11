package com.wiring

import akka.actor.ActorSystem
import com.core.CoreServer
import com.core.persistence.EventPersistenceActor
import com.core.swagger.SwaggerDocService
import com.core.utils.Configuration

object ProductionModule {

  val actorSystem = ActorSystem("core-server")

  val eventCtrl = actorSystem.actorOf(EventPersistenceActor.props())

  val configuration = new Configuration

  val swaggerDocService = new SwaggerDocService(actorSystem, configuration)

  val coreServer = new CoreServer(actorSystem, eventCtrl, configuration, swaggerDocService)
}
