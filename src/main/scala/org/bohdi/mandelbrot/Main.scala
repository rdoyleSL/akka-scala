package org.bohdi.mandelbrot

import akka.actor.{Props, ActorSystem}
import akka.routing.{Broadcast, RoundRobinPool}
import swing._

object Main extends SimpleSwingApplication {
  val environment = Environment(1000, 800, 1000, 8, 80)

  val system = ActorSystem("MandelbrotSystem")

  val viewPort = ViewPort(environment.width, environment.height)//.zoom(1.0).center(0, 0)
  val display = new Display(environment, new CyclePallete)
  display.visible = true

  val workers = system.actorOf(RoundRobinPool(environment.workers).props(Props[Worker]), "workerRouter")
  val master = system.actorOf(Props[Master], "master")
  val guiActor = system.actorOf(Props[GuiActor].withDispatcher("swing-dispatcher"), "frame-actor")

  guiActor ! display
  workers ! Broadcast(environment)
  master ! (environment, workers, guiActor)



  println("Main sending calculate")
  master ! Calculate(viewPort)
  master ! Clear
  master ! Calculate(viewPort.zoom(.00005).center(0.27, 0.0054))

  def top = new MainFrame {

    title = "Mandelbrot"
    minimumSize = new Dimension(500,500)

    contents = new FlowPanel {
      contents += display
      contents += new Button("Press Me")
    }
    //override def size = new Dimension(1000,800)

  }

}
