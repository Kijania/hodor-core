package com.core.swagger

import akka.actor.ActorSystem
import akka.http.scaladsl.model.StatusCodes
import akka.stream.ActorMaterializer
import com.core.routes.EventRoutes
import com.core.utils.{HodorSettings => settings}
import com.github.swagger.akka.{HasActorSystem, SwaggerHttpService}

import scala.reflect.runtime.{universe => runtimeUniverse}
import io.swagger.models.ExternalDocs

class SwaggerDocService(system: ActorSystem) extends SwaggerHttpService with HasActorSystem {
  override implicit val actorSystem: ActorSystem = system
  override implicit val materializer: ActorMaterializer = ActorMaterializer()
  override val apiTypes = Seq(runtimeUniverse.typeOf[EventRoutes])
  override val host: String = s"${settings.host}:${settings.port}"
  override val externalDocs = Some(new ExternalDocs("Hodor-Core Docs", "https://github.com/kijania/hodor-core"))

  def assets = pathPrefix("swagger") {
    getFromResourceDirectory("swagger") ~
      pathSingleSlash(get(redirect("index.html", StatusCodes.PermanentRedirect)))
  }

}
