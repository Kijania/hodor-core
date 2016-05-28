package com.core

import akka.actor.Actor
import spray.routing._
import spray.http._
import MediaTypes._

class HodorServiceActor extends Actor with HodorService {

  def actorRefFactory = context

  override def receive: Receive = runRoute(route)
}


trait HodorService extends HttpService {

  val route =
    path("") {
      get {
        respondWithMediaType(`text/html`) {
          complete {
            <html>
              <body>
                <h1>Say hello to <i>spray-routing</i> on <i>spray-can</i>!</h1>
              </body>
            </html>
          }
        }
      }
    }
}
