package org.bohdi.mandelbrot

import akka.actor.{Props, ActorSystem}
import com.typesafe.config.ConfigFactory
import swing._

object Main extends SimpleSwingApplication {
  val config = ConfigFactory.load()
  val settings = new Settings(config)

  val system = ActorSystem("MandelbrotSystem")

  val model = system.actorOf(Props[Model], "model")
  val display = new Display(settings, model)

  val guiActor = system.actorOf(Props[GuiActor].withDispatcher("mandelbrot.swing-dispatcher"), "frame-actor")

  guiActor ! display

  model ! (settings, guiActor)

  def top = new MainFrame {
    title = "Mandlebrot"
    minimumSize = new Dimension(500, 500)

    contents = display
  }


  model ! Start
}
