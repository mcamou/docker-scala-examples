/*
 * Copyright (c) 2016 eBay, Inc.
 * All rights reserved.
 *
 * Contributors:
 */

import com.whisk.docker._
import com.whisk.docker.config.DockerKitConfig
import org.scalatest.FunSpec

trait DockerMongodbService extends DockerKit {
  val mongodbContainer = DockerContainer("mongo:2.6.12")
                         .withPorts(27017 -> None)
                         .withReadyChecker(
                           DockerReadyChecker.LogLineContains(
                             "waiting for connections on port"
                           )
                         )
                         .withCommand("mongod", "--nojournal", "--smallfiles", "--syncdelay", "0")

  abstract override def dockerContainers: List[DockerContainer] =
    mongodbContainer :: super.dockerContainers
}

trait DockerPostgresService extends DockerKitConfig {

  val postgresContainer = configureDockerContainer("docker.postgres")
  abstract override def dockerContainers: List[DockerContainer] = postgresContainer :: super.dockerContainers
}


class MyMongoSpec extends FunSpec with DockerMongodbService {
  // Test assumes the MongoDB container is running
}
class MyPostgresSpec extends FunSpec with DockerPostgresService {
  // Test assumes the Neo4j container is running
}
class MyAllSpec extends FunSpec with DockerMongodbService with DockerNeo4jService with DockerPostgresService{
  // Test assumes all 3 containers are running
}
