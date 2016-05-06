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
  "com.github.docker-java" % "docker-java" % "2.2.3",
  "com.whisk" %% "docker-testkit-scalatest" % "0.6.1" % "test",
  "com.whisk" %% "docker-testkit-samples" % "0.6.1" % "test",
  "com.whisk" %% "docker-testkit-config" % "0.6.1" % "test"
)

dependencyOverrides +=   "com.typesafe.play" %% "play-json" % "2.3.10"

enablePlugins(DockerPlugin)

imageNames in docker := Seq(
  ImageName(
    namespace = Some("myOrg"),
    repository = name.value,
    tag = Some(s"v${version.value}")
  ),
  ImageName(
    namespace = Some("myOrg"),
    repository = name.value,
    tag = Some("latest")
  )
)

//val artifact = (assemblyOutputPath in assembly).value
val baseDir = "/srv"
val preInstall = Seq(
  "/usr/bin/apt-get update",
  "/usr/bin/apt-get install -y locales git postgresql-client",
  "/usr/bin/apt-get clean",
  "/usr/sbin/locale-gen en_US en_US.UTF-8",
  s"/usr/sbin/useradd -r -s /bin/false -d $baseDir myUser"
).mkString(" && ")

dockerfile in docker := {
  new Dockerfile {
    from("java:openjdk-8-jre")
    runRaw(preInstall)
    copy(artifact, s"$baseDir/app.jar")
    copy(new File("bin/run-docker.sh"), s"$baseDir/run.sh")
    copy(new File("local/etc"), "$baseDir/etc")
    runRaw("chown -R myUser $baseDir && chmod 0544 $baseDir/run.sh")
    user("myUser")
    entryPoint("bin/run.sh")
  }
}

docker <<= (docker dependsOn assembly)

