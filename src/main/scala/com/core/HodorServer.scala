package com.core

import akka.http.scaladsl.Http

class HodorServer() extends HodorService {

  def startServer(address: String, port: Int) = {
    Http().bindAndHandle(route, address, port)
  }
}

object HodorServer extends App {

  val server = new HodorServer()
  server.startServer("localhost", 8080)
}
