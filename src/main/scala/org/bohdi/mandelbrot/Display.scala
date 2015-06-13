package org.bohdi.mandelbrot

import java.awt.{Color, Dimension, Graphics}
import javax.swing.{JFrame, JPanel}

object Display {

}

class Display(env: Environment, pallete: Pallete) extends JFrame {
  val panel = new MandelbrotePanel(env, pallete)

  setContentPane(panel)
  setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)
  setSize(new Dimension(env.width, env.height))
  setVisible(true)

  def setPoints(points: List[(Int, Int, Int)]) = {
    panel.setPoints(points.map(p => (p._1, p._2, pallete(p._3))))
  }
}

class MandelbrotePanel(env: Environment, pallete: Pallete) extends JPanel {
  private var points: List[(Int, Int, Color)] = List()

  def setPoints(xpoints: List[(Int, Int, Color)]): Unit = {
    points = points ++ xpoints
    repaint()
  }

  override def paintComponent(g: Graphics) {
    //println(s"About to draw")


    for ((px,py, color) <- points) {
      g.setColor(color)
      g.drawLine(px, py, px, py)
    }

    //println(s"done to draw")
  }


}