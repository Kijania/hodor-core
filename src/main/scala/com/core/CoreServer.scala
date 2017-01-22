package com.core


import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.{RejectionHandler, RouteConcatenation}
import akka.http.scaladsl.server.directives.ExecutionDirectives
import akka.http.scaladsl.model.HttpMethods._
import akka.http.scaladsl.server.Route

import scala.collection.immutable
import akka.stream.ActorMaterializer
import com.core.persistence.EventPersistenceActor
import com.core.routes.EventRoutes
import com.core.swagger.SwaggerDocService
import com.core.utils.HodorSettings
import com.typesafe.scalalogging.LazyLogging
import ch.megard.akka.http.cors.CorsDirectives._
import ch.megard.akka.http.cors.{CorsDirectives, CorsSettings}

// TODO replace with defined context
import scala.concurrent.ExecutionContext.Implicits.global

import scala.util.{Failure, Success}

trait CoreServer extends LazyLogging with RouteConcatenation {

  implicit val system = ActorSystem("core-server")
  implicit val materializer = ActorMaterializer()

  def run() {
    val eventCtrl = system.actorOf(EventPersistenceActor.props())
    val swaggerDocService = new SwaggerDocService(system)

    val corsSettings = CorsSettings.defaultSettings.copy(allowedMethods = immutable.Seq(
      GET, PUT, POST, DELETE)
    )

    val simpleRoutes = new EventRoutes(eventCtrl).routes

    // handle rejected requests (with status 40x or 50x).
    // Rejected requests will go through cors directives untouched it means no response will be visible.
    // Rejection handler is provided to return meaningful HTTP responses
    def handleCorsRejections(routes: Route) = {
      ExecutionDirectives.handleRejections(CorsDirectives.corsRejectionHandler)(
        cors(corsSettings)(
          ExecutionDirectives.handleRejections(RejectionHandler.default) {
            routes
          }))
    }

    val routes = swaggerDocService.assets ~
      swaggerDocService.routes ~
      handleCorsRejections(simpleRoutes)

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
