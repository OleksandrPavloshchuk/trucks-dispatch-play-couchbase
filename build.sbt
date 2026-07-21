
PlayKeys.devSettings += "play.server.http.port" -> "9120"

lazy val root = (project in file("."))
  .enablePlugins(PlayScala)
  .settings(
    name := "trucks-play-couchbase",
    organization := "probe",
    version := "0.0",
    crossScalaVersions := Seq("2.13.18", "3.3.8"),
    scalaVersion := crossScalaVersions.value.head,
    libraryDependencies ++= Seq(
      guice,
      "com.couchbase.client" %% "scala-client" % "3.9.2",
      "org.scalatestplus.play" %% "scalatestplus-play" % "7.0.2" % Test
    ),
    scalacOptions ++= Seq(
      "-feature",
      "-Werror"
    )
  )
