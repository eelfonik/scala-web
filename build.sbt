name := """scala-web"""
version := "1.0-SNAPSHOT"
scalaVersion := "2.12.2"

lazy val root = (project in file(".")).enablePlugins(PlayScala)
pipelineStages := Seq(digest)

// note here the macwire lib is marked as provided
// because MacWire macros are only used at compile time
// and they are not needed when the app is running
libraryDependencies ++= Seq(
  jdbc,
  ehcache,
  ws,
  "com.softwaremill.macwire" %% "macros" % "2.3.0" % "provided"
)

resolvers += "scalaz-bintray" at "http://dl.bintray.com/scalaz/releases"
