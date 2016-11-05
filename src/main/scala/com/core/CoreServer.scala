package com.core

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import com.core.persistence.EventPersistenceActor$
import com.core.routes.EventRoutes
import com.core.utils.HodorSettings
import com.typesafe.scalalogging.LazyLogging
// TODO replace with defined context
import scala.concurrent.ExecutionContext.Implicits.global

import scala.util.{Failure, Success}

trait CoreServer extends LazyLogging {

  implicit val system = ActorSystem("core-server")
  implicit val materializer = ActorMaterializer()

  def run() {
    val eventCtrl = system.actorOf(EventPersistenceActor.props())

    val routes = new EventRoutes(eventCtrl)

    val binding = Http().bindAndHandle(routes.route, HodorSettings.host, HodorSettings.port)

    binding.onComplete {
      case Success(b) => logger.info(s"Successfully bind to address ${b.localAddress}")
      case Failure(ex) => logger.error(s"Failure binding $ex")
    }
  }
}

object Main extends App with CoreServer {
  run()
}
