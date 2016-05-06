import tugboat.Docker

import scala.concurrent.duration._
import scala.concurrent.{Await, Future}

object Tugboat extends App {
  import scala.concurrent.ExecutionContext.Implicits.global

  println(1)
  val docker: Docker = tugboat.Docker()

  println(2)
  val containerIdFuture: Future[String] = for {
    container <- docker.containers.create("mongo:2.6.12")()
    run       <- docker.containers.get(container.id).start.portBind(tugboat.Port.Tcp(27017), tugboat.PortBinding.local(27017)
    )()
  } yield container.id

  val containerId: String = Await.result(containerIdFuture andThen {case x => println(x)}, 1.minute)

  val mongo: docker.containers.Container = docker.containers.get(containerId)

  val stopped: Future[Unit] = mongo.stop(5.seconds)()

  Await.ready(stopped, 1.minute)
}
