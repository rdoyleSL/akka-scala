package org.bohdi.mandelbrot

import akka.actor.Actor

class GuiActor extends Actor {
  def receive = {
    case (display: Display) =>
      context.become(active(display))
  }

  def active(display: Display): Actor.Receive = {

    case MandelbrotResult(job: Int, tile, elements) =>
      display.setPoints(job, tile, elements)

  }
}
