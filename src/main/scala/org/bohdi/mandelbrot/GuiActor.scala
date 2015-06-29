package org.bohdi.mandelbrot

import akka.actor.Actor

class GuiActor extends Actor {
  def receive = {
    case display: Display =>
      context.become(active(display))
  }

  def active(display: Display): Actor.Receive = {

    case MandelbrotResult(tile, elements) =>
      //println(s"GUI Actor got results: ${elements.size}")
      display.setPoints(tile, elements)

    case Clear => display.clear
  }
}
