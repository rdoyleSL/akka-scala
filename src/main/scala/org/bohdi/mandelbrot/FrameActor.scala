package org.bohdi.mandelbrot

import akka.actor.Actor

class FrameActor extends Actor {
  def receive = {
    case _ => println("FrameActor")
  }

}
