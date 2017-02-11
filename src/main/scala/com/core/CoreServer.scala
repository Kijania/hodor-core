package com.core

import akka.actor.{ActorRef, ActorSystem}
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.RouteConcatenation
import akka.stream.ActorMaterializer
import com.core.routes.EventRoutes
import com.core.swagger.SwaggerDocService
import com.core.utils.Configuration
import com.typesafe.scalalogging.LazyLogging
import com.wiring.ProductionModule

import scala.util.{Failure, Success}

class CoreServer(actorSystem: ActorSystem, eventCtrl: ActorRef, configuration: Configuration, swaggerDocService: SwaggerDocService) extends LazyLogging with RouteConcatenation {

  implicit val system = actorSystem
  implicit val materializer = ActorMaterializer()

  // TODO separate routings to another class when introducing another routing
  def start() {
    val simpleRoutes = new EventRoutes(eventCtrl).routes

    val routes = swaggerDocService.assets ~
      swaggerDocService.routes ~
      swaggerDocService.handleCorsRejections(simpleRoutes)

    val binding = Http().bindAndHandle(routes, configuration.host, configuration.port)

    binding.onComplete {
      case Success(b) => logger.info(s"Successfully bind to address ${b.localAddress}")
      case Failure(ex) => logger.error(s"Failure binding $ex")
    }(system.dispatcher)
  }
}

object Main extends App {
  ProductionModule.coreServer.start()
}
