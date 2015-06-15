package org.bohdi.mandelbrot

import java.awt.{Dimension, Color, Graphics}
import swing._

class Display(env: Environment, pallete: Pallete) extends FlowPanel {
  val panel = new MandelbrotePanel(env, pallete)
  contents += panel

  def clear() = panel.clear()

  def setPoints(points: List[(Int, Int, Int)]) = {
    panel.setPoints(points.map(p => (p._1, p._2, pallete(p._3))))
  }
}

class MandelbrotePanel(env: Environment, pallete: Pallete) extends FlowPanel {
  private var points: List[(Int, Int, Color)] = List()

  preferredSize = new Dimension(env.width, env.height)

  def clear() = {
    println(s"Panel clear")
    points = List()
  }

  def setPoints(xpoints: List[(Int, Int, Color)]): Unit = {
    println(s"Panel setPoints")
    points = points ++ xpoints
    repaint()
    revalidate()
  }

  override def paintComponent(g: Graphics2D) {
    println(s"painting: ${points.size}")
    for ((px,py, color) <- points) {
      g.setColor(color)
      g.drawLine(px, py, px, py)
    }
  }


}