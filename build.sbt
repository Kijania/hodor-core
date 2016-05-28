name := "hodor-core"

version := "1.0"

scalaVersion := "2.11.8"

val akkaVersion = "2.4.6"
val sprayVersion = "1.3.3"
val mockitoVersion = "1.9.5"
val scalatestVersion = "2.2.4"

resolvers ++= Seq(
  "Typesafe repository" at "http://repo.typesafe.com/typesafe/releases/",
  "spray repo" at "http://repo.spray.io"
)

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-kernel" % akkaVersion,
  "com.typesafe.akka" %% "akka-slf4j" % akkaVersion,
  "com.typesafe.akka" %% "akka-testkit" % akkaVersion % "test",
  "io.spray" %% "spray-can" % sprayVersion,
  "io.spray" %% "spray-routing" % sprayVersion,
  "io.spray" %% "spray-testkit" % sprayVersion % "test",
  "org.mockito" % "mockito-core" % mockitoVersion,
  "org.scalatest" %% "scalatest" % scalatestVersion
)
