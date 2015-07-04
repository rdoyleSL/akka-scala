package org.bohdi.mandelbrot

import java.awt.{Dimension, Color}
import akka.actor.ActorRef

import scala.swing.event.{KeyTyped, ButtonClicked}
import swing._

class Display(env: Environment, model: ActorRef) extends FlowPanel {
  val panel1 = new MandelbrotePanel(env)
  val panel2 = new MandelbrotePanel(env)

  val zoomIn = new Button("+")
  val zoomOut = new Button("-")
  val left = new Button("<")
  val right = new Button(">")
  val up = new Button("^")
  val down = new Button("v")
  val zoomIn100 = new Button("100+")
  val p1 = new Button("p1")
  val p2 = new Button("p2")




  contents += new BoxPanel(Orientation.Vertical) {
    contents += new BoxPanel(Orientation.Horizontal) {
      contents += panel1
      contents += panel2
    }

    contents += new BoxPanel(Orientation.Horizontal) {
      contents += zoomIn
      contents += zoomOut
      contents += left
      contents += right
      contents += up
      contents += down
      contents += zoomIn100
      contents += p1
      contents += p2
    }
  }

  listenTo(zoomIn, zoomOut, left, right, up, down, zoomIn.keys, zoomOut.keys, left.keys, right.keys, up.keys, down.keys,
    zoomIn100,
    p1,
    p2)

  reactions += {
    case ButtonClicked(`zoomIn`) => model ! ZoomIn
    case ButtonClicked(`zoomOut`) => model ! ZoomOut
    case ButtonClicked(`left`) => model ! PanLeft
    case ButtonClicked(`right`) => model ! PanRight
    case ButtonClicked(`up`) => model ! PanUp
    case ButtonClicked(`down`) => model ! PanDown
    case ButtonClicked(`zoomIn100`) => model ! ZoomIn100
    case ButtonClicked(`p1`) => model ! ChangePallete(new OddEvenPallete)
    case ButtonClicked(`p2`) => model ! ChangePallete(new CyclePallete(env.maxIterations))



    case KeyTyped(_, c, _, _) =>
      c match {
        case '+' => model ! ZoomIn
        case '-' => model ! ZoomOut
        case '<' => model ! PanLeft
        case '>' => model ! PanRight
        case '^' => model ! PanUp
        case 'v' => model ! PanDown
        case _ =>
      }
    }

  def setPoints(job: Int, tile: Tile, points: List[(Int, Int, Color)]) = {
    panel1.setPoints(job, tile, points)
    panel2.setPoints(job, tile, points)

  }
}

class MandelbrotePanel(env: Environment) extends FlowPanel {
  private var points: Map[Tile, List[(Int, Int, Color)]] = Map()
  private var dirty: Map[Tile, List[(Int, Int, Color)]] = Map()

  preferredSize = new Dimension(env.width, env.height)

  def setPoints(job: Int, tile: Tile, data: List[(Int, Int, Color)]): Unit = {
    points = points.updated(tile, data)
    dirty = dirty.updated(tile, data)

    //println(s"points: ${points.size} ${points.values.map(_.size).sum}")
    //println(s"dirty: ${dirty.size} ${dirty.values.map(_.size).sum}")

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