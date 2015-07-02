package org.bohdi.mandelbrot

import akka.actor.{Props, ActorSystem}
import akka.routing.{Broadcast, RoundRobinPool}
import swing._

object Main extends SimpleSwingApplication {
  val environment = Environment(1000, 800, 1000, 8, TileFactory.create(1000, 800, 10))

  val system = ActorSystem("MandelbrotSystem")

  val viewPort = ViewPort(environment.width, environment.height).center(0.27, 0.0054)
  val display = new Display(environment, new CyclePallete)
  //display.visible = true

  //val workers = system.actorOf(RoundRobinPool(environment.workers).props(Props[Worker]), "workerRouter")
  val model = system.actorOf(Props[ModelActor], "master")
  val guiActor = system.actorOf(Props[GuiActor].withDispatcher("swing-dispatcher"), "frame-actor")

  guiActor ! display
  //workers ! Broadcast(environment)
  model ! (environment, guiActor)

  def top = new MainFrame {
    title = "Mandlebrot"
    minimumSize = new Dimension(500, 500)

    contents = display
  }



  println("Main sending calculate")
  model ! Calculate(viewPort)

  model ! Calculate(viewPort.zoom(.5).center(0.27, 0.0054))
  w
  model ! Calculate(viewPort.zoom(.05).center(0.27, 0.0054))
  w
  model ! Calculate(viewPort.zoom(.005).center(0.27, 0.0054))

  w
  model ! Calculate(viewPort.zoom(.0005).center(0.27, 0.0054))
  w
  model ! Calculate(viewPort.zoom(.00005).center(0.27, 0.0054))

  def w: Unit = Thread.sleep(1000)

}
