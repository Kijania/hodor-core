package com.core.swagger

import akka.actor.ActorSystem
import akka.http.scaladsl.model.HttpMethods._
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.{RejectionHandler, _}
import akka.http.scaladsl.server.directives.ExecutionDirectives
import akka.stream.ActorMaterializer
import ch.megard.akka.http.cors.{CorsDirectives, CorsSettings}
import ch.megard.akka.http.cors.CorsDirectives._
import com.core.routes.EventRoutes
import com.core.utils.{HodorSettings => settings}
import com.github.swagger.akka.{HasActorSystem, SwaggerHttpService}

import scala.reflect.runtime.{universe => runtimeUniverse}
import io.swagger.models.ExternalDocs

import scala.collection.immutable

class SwaggerDocService(system: ActorSystem) extends SwaggerHttpService with HasActorSystem {
  override implicit val actorSystem: ActorSystem = system
  override implicit val materializer: ActorMaterializer = ActorMaterializer()
  override val apiTypes = Seq(runtimeUniverse.typeOf[EventRoutes])
  override val host: String = s"${settings.host}:${settings.port}"
  override val externalDocs = Some(new ExternalDocs("Hodor-Core Docs", "https://github.com/kijania/hodor-core"))

  val corsSettings = CorsSettings.defaultSettings.copy(allowedMethods = immutable.Seq(
    GET, PUT, POST, DELETE)
  )

  def assets = pathPrefix("swagger") {
    getFromResourceDirectory("swagger") ~
      pathSingleSlash(get(redirect("index.html", StatusCodes.PermanentRedirect)))
  }

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
}
