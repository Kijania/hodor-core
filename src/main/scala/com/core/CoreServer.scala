package com.core

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.RouteConcatenation
import akka.stream.ActorMaterializer
import com.core.persistence.EventPersistenceActor
import com.core.routes.EventRoutes
import com.core.swagger.SwaggerDocService
import com.core.utils.HodorSettings
import com.typesafe.scalalogging.LazyLogging
import com.wiring.ProductionModule
import scala.util.{Failure, Success}

class CoreServer(actorSystem: ActorSystem) extends LazyLogging with RouteConcatenation {

  implicit val system = actorSystem
  implicit val materializer = ActorMaterializer()

  def start() {
    val eventCtrl = system.actorOf(EventPersistenceActor.props())
    val simpleRoutes = new EventRoutes(eventCtrl).routes

    val swaggerDocService = new SwaggerDocService(system)

    val routes = swaggerDocService.assets ~
      swaggerDocService.routes ~
      swaggerDocService.handleCorsRejections(simpleRoutes)

    val binding = Http().bindAndHandle(routes, HodorSettings.host, HodorSettings.port)

    binding.onComplete {
      case Success(b) => logger.info(s"Successfully bind to address ${b.localAddress}")
      case Failure(ex) => logger.error(s"Failure binding $ex")
    }(system.dispatcher)
  }
}

object Main extends App {
  ProductionModule.coreServer.start()
}
