package com.wiring

import akka.actor.ActorSystem
import com.core.CoreServer

object ProductionModule {

  val actorSystem = ActorSystem("core-server")

  val coreServer = new CoreServer(actorSystem)
}
