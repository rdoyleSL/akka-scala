package org.bohdi.mandelbrot

import akka.actor.Actor

class ResultHandler extends Actor {

  def receive = {
    case display: Display =>
      println("ResultHandler got display")
      context.become(active(display))
}


def active(display: Display): Actor.Receive = {
    case MandelbrotResult(elements, duration) =>
      println("completed in %s!".format(duration))
      display.setPoints(elements.toMap)
  }
}
