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
    var histogram: Array[Int] = new Array[Int](maxIterations)
    for(px <- 0 until width) {
      for (py <- 0 until height) {
        val numIters = points(px,py)
        histogram(numIters-1) += 1
      }
    }
    var total = 0
    for(i <- 0 until maxIterations) {
      total += histogram(i)
    }

    for(px <- 0 until width) {
      for (py <- 0 until height) {
        val numIters = points(px,py)

        var colorVal = 0.0
        for(i <- 0 until numIters) {
          colorVal += histogram(i).toFloat / total.toFloat
        }

        val rgb = Color.HSBtoRGB(0.1f+colorVal.toFloat, 1.0f, colorVal.toFloat*colorVal.toFloat)
        g.setColor(new Color(rgb))
        g.drawLine(px, py, px, py)
      }
    }
  }
}