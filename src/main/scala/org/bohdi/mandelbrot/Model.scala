package org.bohdi.mandelbrot

import akka.actor.{Props, Actor, ActorRef}
import akka.routing.{Broadcast, RoundRobinPool}


class ModelActor extends Actor {
  var mandelbrot: List[(Int, Int, Int)] = List()
  val start: Long = System.currentTimeMillis()



  def receive = {
    case (environment: Environment, guiActor: ActorRef) =>
      val workers = context.actorOf(RoundRobinPool(environment.workers).props(Props[Worker]), "workerRouter")
      workers ! Broadcast(environment)

      context.become(active(environment, workers, guiActor))
  }


  def active(env: Environment, workers: ActorRef, guiActor: ActorRef): Actor.Receive = {

    case Calculate(viewPort: ViewPort) =>
      println("Calculating....")
      //guiActor ! Clear

      for (tile <- env.tiles) {
        workers ! Work(tile, viewPort)
      }

    case Result(tile, elements) =>
      guiActor ! MandelbrotResult(tile, elements)
        //context.stop(self)
      //}

    case Clear =>
      guiActor ! Clear

    case x:Any => println(s"Master got junk: $x")
  }
}
