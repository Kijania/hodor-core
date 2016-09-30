name := "hodor-core"

version := "1.0"

scalaVersion := "2.11.8"

val akkaVersion = "2.4.10"
val sprayJsonVersion = "1.3.2"
val mockitoVersion = "1.9.5"
val scalatestVersion = "2.2.4"

resolvers ++= Seq(
  "Typesafe repository" at "http://repo.typesafe.com/typesafe/releases/",
  Resolver.jcenterRepo
)

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-kernel" % akkaVersion,
  "com.typesafe.akka" %% "akka-slf4j" % akkaVersion,
  "com.typesafe.akka" %% "akka-testkit" % akkaVersion % "test",
  "com.typesafe.akka" %% "akka-http-experimental" % akkaVersion,
  "com.typesafe.akka" %% "akka-http-spray-json-experimental" % akkaVersion,
  "com.typesafe.akka" %% "akka-stream" % akkaVersion,
  "io.spray" %% "spray-json" % sprayJsonVersion,
  "org.mockito" % "mockito-core" % mockitoVersion,
  "org.scalatest" %% "scalatest" % scalatestVersion,
  "com.github.nscala-time" %% "nscala-time" % "2.12.0"
)
