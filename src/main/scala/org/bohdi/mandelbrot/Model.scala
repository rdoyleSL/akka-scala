package org.bohdi.mandelbrot

import akka.actor.{Props, Actor, ActorRef}
import akka.routing.{Broadcast, RoundRobinPool}


class Model extends Actor {
  var mandelbrot: List[(Int, Int, Int)] = List()
  var viewPort = ViewPort().center(0.27, 0.0054)
  var pallete: Pallete = new OddEvenPallete
  var job = 1
  var tiles = Seq[Tile]()


  def receive = {
    case (settings: Settings, guiActor: ActorRef) =>
      val workers = context.actorOf(RoundRobinPool(settings.workers).props(Props[Calculator]), "workerRouter")
      workers ! Broadcast(settings)

      tiles = TileFactory.create(settings.width, settings.height, settings.workers)


      context.become(active(settings, tiles, workers, guiActor))
  }


  def active(settings: Settings, tile: Seq[Tile], workers: ActorRef, guiActor: ActorRef): Actor.Receive = {

    case Start =>
      calculate(settings, workers)

    case ZoomIn =>
      viewPort = viewPort.zoom(0.7)
      calculate(settings, workers)

    case ZoomIn100 =>
      (0 to 20) foreach {n: Int =>
        println("z " + n)
        viewPort = viewPort.zoom(0.7)
        calculate(settings, workers)
      }

    case ZoomOut =>
      viewPort = viewPort.zoom(1/0.7)
      calculate(settings, workers)

    case PanLeft =>
      viewPort = viewPort.panLeft
      calculate(settings, workers)

    case PanRight =>
      viewPort = viewPort.panRight
      calculate(settings, workers)

    case PanUp =>
      viewPort = viewPort.panUp
      calculate(settings, workers)

    case PanDown =>
      viewPort = viewPort.panDown
      calculate(settings, workers)

    case ChangePallete(newPallete) =>
      pallete = newPallete
      calculate(settings, workers)


    case Result(job, tile, elements) =>
      if (this.job == job) {
        guiActor ! MandelbrotResult(job, tile, elements)
      }
        //context.stop(self)
      //}

    case x:Any => println(s"Model got junk: $x")
  }

  def calculate(settings: Settings, workers: ActorRef) = {
    job = job + 1

    workers ! Broadcast(job)

    for (tile <- tiles) {
      workers ! Work(job, tile, viewPort, pallete)
    }
  }
}
