resolvers += Resolver.mavenLocal

lazy val `test-xpath-vs-scala` = (project in file("."))
  .settings(
    name := "test-xpath-vs-scala",
    version := "0.0.0-SNAPSHOT",
    scalaVersion := "2.11.8"
  )

val akkaVersion = "2.4.7"
val scalaVersionString = "2.11.8"
val engineVersion = "1.10.4"

libraryDependencies += "com.typesafe.akka" %% "akka-actor" % akkaVersion
libraryDependencies += "com.typesafe.akka" %% "akka-http-experimental" % akkaVersion
libraryDependencies += "com.typesafe.akka" %% "akka-http-spray-json-experimental" % akkaVersion
libraryDependencies += "com.typesafe.akka" %% "akka-http-testkit-experimental" % "2.4.2-RC3" % "test"
libraryDependencies += "org.mockito" % "mockito-core" % "1.10.19" % "test"
libraryDependencies += "org.scala-lang" % "scala-reflect" % scalaVersionString   // To resolve nested dependency version conflict
libraryDependencies += "org.scala-lang.modules" %% "scala-xml" % "1.0.4"   // To resolve nested dependency version conflict

libraryDependencies += "com.sos-berlin.jobscheduler.engine" % "engine-common" % engineVersion
libraryDependencies += "com.sos-berlin.jobscheduler.engine" % "engine-data" % engineVersion
libraryDependencies += "org.scalatest" %% "scalatest" % "2.2.4" % "test"
