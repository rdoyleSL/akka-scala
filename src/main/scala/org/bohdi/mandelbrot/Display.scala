package org.bohdi.mandelbrot

import java.awt.{Dimension, Color, Graphics}
import scala.swing.event.ButtonClicked
import swing._

class Display(env: Environment, pallete: Pallete) extends FlowPanel {
  val panel = new MandelbrotePanel(env, pallete)
  val button = new Button("Kiss Me")

  contents += new BoxPanel(Orientation.Vertical) {
    contents += panel
    contents += button
  }

  listenTo(button)

  reactions += {
    case ButtonClicked(b) =>
      println("Hello")
  }

  def clear() = panel.clear()

  def setPoints(tile: Tile, points: List[(Int, Int, Int)]) = {
    panel.setPoints(tile, points.map(p => (p._1, p._2, pallete(p._3))))
  }
}

class MandelbrotePanel(env: Environment, pallete: Pallete) extends FlowPanel {
  private var points: Map[Tile, List[(Int, Int, Color)]] = Map()
  private var dirty: Map[Tile, List[(Int, Int, Color)]] = Map()


  preferredSize = new Dimension(env.width, env.height)

  def clear() = {
    //println(s"Panel clear")
    //points = Map()
    //dirty = Map()
  }

  def setPoints(tile: Tile, data: List[(Int, Int, Color)]): Unit = {
    //println(s"Panel setPoints")
    points = points.updated(tile, data)
    dirty = dirty.updated(tile, data)

    repaint()
    revalidate()
  }

  override def paintComponent(g: Graphics2D) {
    if (dirty.isEmpty)
      paintPoints(g, points)
    else {
      paintPoints(g, dirty)
      dirty = Map()
    }
  }

  def paintPoints(g: Graphics2D, data: Map[Tile, List[(Int, Int, Color)]]) {
    //println(s"painting: ${points.size}")
    for ((tile, points) <- data; (px,py, color) <- points) {
      g.setColor(color)
      g.drawLine(px, py, px, py)
    }
  }


}