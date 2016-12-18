package com.core.swagger

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import com.core.routes.EventRoutes
import com.github.swagger.akka.{HasActorSystem, SwaggerHttpService}

import scala.reflect.runtime.{universe => runtimeUniverse}
import com.core.utils.HodorSettings._
import io.swagger.models.ExternalDocs
import com.github.swagger.akka.model.Info

class SwaggerDocService(system: ActorSystem) extends SwaggerHttpService with HasActorSystem {
  override implicit val actorSystem: ActorSystem = system
  override implicit val materializer: ActorMaterializer = ActorMaterializer()
  override val apiTypes = Seq(runtimeUniverse.typeOf[EventRoutes])
  override val host: String = s"$host:$port"
  override val info = Info(version = "1.0")
  override val externalDocs = Some(new ExternalDocs("Hodor-Core Docs", "https://github.com/kijania/hodor-core"))
}
