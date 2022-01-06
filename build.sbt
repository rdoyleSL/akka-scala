
ThisBuild / scalaVersion     := "2.11.6"
ThisBuild / version          := "0.1.0-SNAPSHOT"
ThisBuild / organization     := "org.bodhi"
ThisBuild / organizationName := "bodhi"

lazy val mandelbrot = (project in file("."))
  .settings(
    name := "Mandelbrot",
    //resolvers += "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots",
    resolvers += "Akka Release Repository" at "https://repo.akka.io/releases/",
    //resolvers += "Akka Snapshot Repository" at "https://repo.akka.io/snapshots/",
    resolvers += "Typesafe Repsository" at "https://repo.typesafe.com/typesage.releases",

    libraryDependencies += "com.typesafe.akka" %% "akka-actor" % "2.3.11",
    libraryDependencies += "org.scala-lang.modules" %% "scala-swing" % "1.0.1",
    libraryDependencies += "org.scalatest" %% "scalatest" % "2.2.4" % Test
  )

