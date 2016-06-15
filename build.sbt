// Usage: sbt assembly docker

//val mavenLocal = "Local Maven Repository" at s"file:///${Path.userHome}/.m2/repository"
resolvers += Resolver.mavenLocal

lazy val `test-xpath-vs-scala` = (project in file("."))
  .settings(
    name := "test-xpath-vs-scala",
    version := "0.0.0-SNAPSHOT",
    scalaVersion := "2.11.8"
  )

libraryDependencies += "org.scalatest" %% "scalatest" % "2.2.4" % "test"
libraryDependencies += "ch.qos.logback" %  "logback-classic" % "1.1.7"
libraryDependencies += "com.typesafe.scala-logging" %% "scala-logging" % "3.4.0"
val akkaVersion = "2.4.7"
libraryDependencies += "com.typesafe.akka" %% "akka-actor" % akkaVersion
libraryDependencies += "com.typesafe.akka" %% "akka-http-experimental" % akkaVersion
libraryDependencies += "com.typesafe.akka" %% "akka-http-spray-json-experimental" % akkaVersion
libraryDependencies += "com.typesafe.akka" %% "akka-http-testkit-experimental" % "2.4.2-RC3" % "test"
//libraryDependencies += "com.google.guava" % "guava" % "19.0" % "test"
libraryDependencies += "org.mockito" % "mockito-core" % "1.10.19" % "test"
libraryDependencies += "org.scala-lang.modules" %% "scala-xml" % "1.0.4"   // To resolve nested dependency version conflict

libraryDependencies += "com.sos-berlin.jobscheduler.engine" % "engine-kernel" % "1.10.4"
