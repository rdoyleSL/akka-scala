package org.bohdi.mandelbrot

import akka.actor.{Props, ActorSystem}
import swing._

object Main extends SimpleSwingApplication {
  val environment = Environment(1000, 800, 2000, 8, TileFactory.create(1000, 800, 10))

  val system = ActorSystem("MandelbrotSystem")

  val model = system.actorOf(Props[Model], "model")
  val display = new Display(environment, model)

  val guiActor = system.actorOf(Props[GuiActor].withDispatcher("swing-dispatcher"), "frame-actor")

  guiActor ! display

  model ! (environment, guiActor)

  def top = new MainFrame {
    title = "Mandlebrot"
    minimumSize = new Dimension(500, 500)

    contents = display
  }


  model ! Start
}
