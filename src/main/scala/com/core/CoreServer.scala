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
import ch.megard.akka.http.cors.CorsDirectives._
// TODO replace with defined context
import scala.concurrent.ExecutionContext.Implicits.global

import scala.util.{Failure, Success}

trait CoreServer extends LazyLogging with RouteConcatenation {

  implicit val system = ActorSystem("core-server")
  implicit val materializer = ActorMaterializer()

  def run() {
    val eventCtrl = system.actorOf(EventPersistenceActor.props())
    val swaggerDocService = new SwaggerDocService(system)

    val routes =
      swaggerDocService.assets ~
      cors() ( new EventRoutes(eventCtrl).route ~
      swaggerDocService.routes)

    val binding = Http().bindAndHandle(routes, HodorSettings.host, HodorSettings.port)

    binding.onComplete {
      case Success(b) => logger.info(s"Successfully bind to address ${b.localAddress}")
      case Failure(ex) => logger.error(s"Failure binding $ex")
    }
  }
}

object Main extends App with CoreServer {
  run()
}
