import com.kolor.docker.api._
import com.kolor.docker.api.entities.ContainerConfiguration
import com.kolor.docker.api.json.FormatsV112._

import scala.concurrent.Await
import scala.concurrent.duration._


object ReactiveDocker extends App {
  implicit val docker = Docker("192.168.99.100", 2375)
  val timeout = 30.seconds

  val containerName = "reactive-docker"
  //val imageTag = RepositoryTag.create("mongo:2.3.6", Some("latest"))
  val cmd = Seq("mongod", "--nojournal", "--smallfiles", "--syncdelay", "0")
  val cfg = ContainerConfiguration(Some("mongo:2.6.12"), Some(cmd))

  // create image, returns a list of docker messages when finished
  //val messages = Await.result(docker.imageCreate(imageTag), timeout)

  //messages.foreach(m => println(s"imageCreate: $m"))

  // create container
  val (containerId, _) = Await.result(docker.containerCreate("mongo:2.6.5", cfg, Some(containerName)), timeout)

  //warnings foreach { println }

  // run container
  Await.ready(docker.containerStart(containerId), timeout)
  println(s"container $containerId is running")

  Await.ready(docker.containerStop(containerId), timeout)
  println(s"container $containerId is stopped")
}