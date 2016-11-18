enablePlugins(JavaAppPackaging)

name := "hodor-core"

version := "1.0"

val scalaV = "2.11.8"

scalaVersion := scalaV

val akkaVersion = "2.4.11"
val sprayJsonVersion = "1.3.2"
val mockitoVersion = "1.9.5"
val scalatestVersion = "3.0.0"
val nscalaTimeVersion = "2.14.0"
val logbackVersion = "1.1.7"
val scalaLoggingVersion = "3.5.0"
val slickVersion = "3.1.1"
val postgresqlVersion = "9.4.1211"
val h2Version = "1.4.193"
val slickJodaMapperVersion = "2.2.0"

val slf4jApiVersion = "1.7.20"
val typesafeConfigVersion = "1.3.0"
val scalaXmlVersion = "1.0.4"

resolvers ++= Seq(
  "Typesafe repository" at "http://repo.typesafe.com/typesafe/releases/",
  Resolver.jcenterRepo
)

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-kernel" % akkaVersion,
  "com.typesafe.akka" %% "akka-slf4j" % akkaVersion,
  "com.typesafe.akka" %% "akka-testkit" % akkaVersion % "test",
  "com.typesafe.akka" %% "akka-http-testkit" % akkaVersion % "test",
  "com.typesafe.akka" %% "akka-http-core" % akkaVersion,
  "com.typesafe.akka" %% "akka-http-experimental" % akkaVersion,
  "com.typesafe.akka" %% "akka-http-spray-json-experimental" % akkaVersion,
  "com.typesafe.akka" %% "akka-stream" % akkaVersion,
  "io.spray" %% "spray-json" % sprayJsonVersion,
  "org.mockito" % "mockito-core" % mockitoVersion,
  "org.scalatest" %% "scalatest" % scalatestVersion,
  // joda time wrapper for scala
  "com.github.nscala-time" %% "nscala-time" % nscalaTimeVersion,
  "ch.qos.logback" %  "logback-classic" % logbackVersion,
  "com.typesafe.scala-logging" %% "scala-logging" % scalaLoggingVersion,
  "org.postgresql" % "postgresql" % postgresqlVersion,
  "com.h2database" % "h2" % h2Version,
  "com.typesafe.slick" %% "slick" % slickVersion,
  // slick joda mapper
  "com.github.tototoshi" %% "slick-joda-mapper" % slickJodaMapperVersion,
  // jdbc connection pool, successor of Bone-CP
  "com.typesafe.slick" %% "slick-hikaricp" % slickVersion
)

conflictManager := ConflictManager.strict
dependencyOverrides ++= Set(
  "org.scala-lang" % "scala-library" % scalaV,                // because of slickVersion
  "org.slf4j" % "slf4j-api" % slf4jApiVersion,                // because of akkaVersion
  "com.typesafe" % "config" % typesafeConfigVersion,          // because of slickVersion
  "org.scala-lang.modules" %% "scala-xml" % scalaXmlVersion
)