import com.github.dockerjava.core.command.PullImageResultCallback
import com.github.dockerjava.core.{DockerClientBuilder, DockerClientConfig}

/*
 * Copyright (c) 2016 eBay, Inc.
 * All rights reserved.
 *
 * Contributors:
 */
object JavaApi extends App {
  val config = DockerClientConfig.createDefaultConfigBuilder()
               .withServerAddress("tcp://192.168.99.100:2376")
               .withDockerCertPath("/Users/macamou/.docker/machine/machines/default")
               .build
  val docker = DockerClientBuilder.getInstance(config).build

  val callback = new PullImageResultCallback
  docker.pullImageCmd("mongo:2.6.12").exec(callback)
  callback.awaitSuccess

  val container = docker.createContainerCmd("mongo:2.6.12")
    .withCmd("mongod", "--nojournal", "--smallfiles", "--syncdelay", "0")
    .exec

  docker.startContainerCmd(container.getId).exec

  println("Container started")

  docker.stopContainerCmd(container.getId).exec
  val exitcode = docker.waitContainerCmd(container.getId).exec

  println(s"Container stopped: $exitcode")
}
