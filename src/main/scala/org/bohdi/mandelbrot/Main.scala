package org.bohdi.mandelbrot

import akka.actor.{Props, ActorSystem}
import akka.routing.{Broadcast, RoundRobinPool}


object Main extends App {
  val environment = Environment(1000, 800, 1000, 8, 80)

  val system = ActorSystem("MandelbrotSystem")

  val viewPort = ViewPort(environment.width, environment.height)//.zoom(1.0).center(0, 0)
  val display = new Display(environment, new CyclePallete)

  val resultHandler = system.actorOf(Props[ResultHandler], "resultHandler")
  val workers = system.actorOf(RoundRobinPool(environment.workers).props(Props[Worker]), "workerRouter")
  val master = system.actorOf(Props[Master], "master")
  val frameActor = system.actorOf(Props[FrameActor].withDispatcher("swing-dispatcher"), "frame-actor")

  resultHandler ! display
  workers ! Broadcast(environment)
  master ! MasterInit(environment, workers, resultHandler)



  println("Main sending calculate")
  master ! Calculate(0, 0, 0)
}
