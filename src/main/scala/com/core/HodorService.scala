package com.core

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.server.Directives._
import com.core_api.{Event, ServiceJsonProtocol}


trait HodorService extends ServiceJsonProtocol {

  implicit val system: ActorSystem = ActorSystem("hodor-server")
  implicit val materializer: ActorMaterializer = ActorMaterializer()

  import com.github.nscala_time.time.Imports._

  var list = List[Event]()

  val route =
    path("events") {
      post {
        entity(as[Event]) { event =>
          complete {
            list = event :: list
            s"Get event ${event.name}: ${event.date}"
          }
        }
      } ~
      get {
        complete {
          list
        }
      }
    }
}
