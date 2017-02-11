package com.core.utils

import java.util.concurrent.Executors

import scala.concurrent.ExecutionContext

object ExecutionContexts {
  implicit val databaseExecutionContext = ExecutionContext.fromExecutorService(
    Executors.newFixedThreadPool(Runtime.getRuntime.availableProcessors())
  )
}
