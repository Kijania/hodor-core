package com.core

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer
import com.core.utils.Configuration
import com.typesafe.scalalogging.LazyLogging
import com.wiring.ProductionModule

import scala.util.{Failure, Success}

class CoreServer(actorSystem: ActorSystem, routes: Route, configuration: Configuration) extends LazyLogging {

  implicit val system = actorSystem
  implicit val materializer = ActorMaterializer()

  def start() {
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
