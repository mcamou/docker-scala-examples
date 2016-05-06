name := "docker-samples"

version := "1.0"

scalaVersion := "2.11.8"

resolvers ++= Seq(
  "softprops-maven" at "http://dl.bintray.com/content/softprops/maven",
  Resolver.sonatypeRepo("snapshots")
)

libraryDependencies ++= Seq(
  "org.almoehi" %% "reactive-docker" % "0.1-SNAPSHOT",
  "me.lessis" %% "tugboat" % "0.2.0",
  "com.typesafe.play" %% "play-json" % "2.3.10",
  "com.github.docker-java" % "docker-java" % "2.2.3"
)

dependencyOverrides +=   "com.typesafe.play" %% "play-json" % "2.3.10"
