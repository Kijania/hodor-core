package com.core

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import com.typesafe.config.ConfigFactory

class HodorServer(implicit val system: ActorSystem,
  implicit  val materializer: ActorMaterializer) extends HodorService {

  def startServer(address: String, port: Int) = {
    Http().bindAndHandle(route, address, port)
  }
}

object HodorServer extends App {

  override def main(args: Array[String]) = {

    implicit val system = ActorSystem("hodor-server")
    implicit val materializer = ActorMaterializer()

    val server = new HodorServer()
    val config = ConfigFactory.load()
    server.startServer(config.getString("http.host"), config.getInt("http.port"))
  }
}
