package com.core.routes

import akka.http.scaladsl.server.{Route, RouteConcatenation}
import com.core.swagger.SwaggerDocService

class Routes(eventRoutes: Route, swaggerDocService: SwaggerDocService) extends RouteConcatenation {

  val routes = swaggerDocService.assets ~
    swaggerDocService.routes ~
    swaggerDocService.handleCorsRejections(eventRoutes)
}
