package com.core

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import com.core.utils.HodorSettings
import com.core.utils.postgresql.PostgresqlDatabaseConnection
import com.typesafe.scalalogging.LazyLogging
// TODO replace with defined context
import scala.concurrent.ExecutionContext.Implicits.global

import scala.util.{Failure, Success}

trait HodorCoreServer extends LazyLogging {

  implicit val system = ActorSystem("hodor-server")
  implicit val materializer = ActorMaterializer()

  // TODO encrypt admin user and password, introduce authentication
  implicit val db = new PostgresqlDatabaseConnection().db()

  def run() {
    val service = new HodorService

    val routes = new HodorRoutes(service)

    val binding = Http().bindAndHandle(routes.route, HodorSettings.host, HodorSettings.port)

    binding.onComplete {
      case Success(b) => logger.info(s"Successfully bind to address ${b.localAddress}")
      case Failure(ex) => logger.error(s"Failure binding $ex")
    }
  }
}

object HodorCoreServer extends App with HodorCoreServer {
  run()
}
