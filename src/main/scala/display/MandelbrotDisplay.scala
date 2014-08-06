package display

import javax.swing.JFrame
import java.awt.{Graphics, Color, Dimension}
import scala.collection.mutable

class MandelbrotDisplay(points: mutable.Map[(Int, Int), Int], height: Int, width: Int) extends JFrame  {
  setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)
  setBackground(Color.BLACK)
  setPreferredSize(new Dimension(height, width))
  pack
  setResizable(false)
  setVisible(true)
  override def paint(g: Graphics) {
    super.paint(g)
    for(px <- 0 until 1000) {
      for (py <- 0 until 1000) {
        val numIters = points(px,py)
        val colorVal = points((px, py)).toFloat/1000
        g.setColor(new Color(colorVal, colorVal, colorVal))
        g.drawLine(px, py, px, py)
      }
    }
  }
}