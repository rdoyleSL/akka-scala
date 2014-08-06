package display

import javax.swing.JFrame
import java.awt.{Graphics, Color, Dimension}
import scala.collection.mutable

class MandelbrotDisplay(points: mutable.Map[(Int, Int), Int], height: Int, width: Int, maxIterations: Int) extends JFrame  {
  setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)
  setPreferredSize(new Dimension(height, width))
  pack
  setResizable(false)
  setVisible(true)
  override def paint(g: Graphics) {
    super.paint(g)
    for(px <- 0 until width) {
      for (py <- 0 until height) {
        val numIters = points(px,py)
        val colorVal = points((px, py)).toFloat/maxIterations
        g.setColor(new Color(colorVal, colorVal, colorVal))
        g.drawLine(px, py, px, py)
      }
    }
  }
}