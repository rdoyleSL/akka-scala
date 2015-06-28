package org.bohdi.mandelbrot

import akka.actor.{Actor, ActorRef}


class Master extends Actor {
  var mandelbrot: List[(Int, Int, Int)] = List()
  val start: Long = System.currentTimeMillis()



  def receive = {
    case (environment: Environment, workers: ActorRef, guiActor: ActorRef) =>
      context.become(active(environment, workers, guiActor))
  }


  def active(env: Environment, workers: ActorRef, guiActor: ActorRef): Actor.Receive = {

    case Calculate(viewPort: ViewPort) =>
      println("Calculating....")
      guiActor ! Clear

      for (tile <- env.tiles) {
        workers ! Work(tile, viewPort)
      }

    case Result(elements) =>
      guiActor ! MandelbrotResult(elements)
        //context.stop(self)
      //}

    case Clear =>
      guiActor ! Clear

    case x:Any => println(s"Master got junk: $x")
  }
}
