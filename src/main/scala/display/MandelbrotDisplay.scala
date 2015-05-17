package display

import javax.swing.{JPanel, JFrame}
import java.awt.{Graphics, Color, Dimension}
import scala.collection.mutable

class MandelbrotDisplay(points: mutable.Map[(Int, Int), Int], height: Int, width: Int, maxIterations: Int) extends JFrame {

  setContentPane(new MandelbrotePane(points, height, width, maxIterations))
  setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)
  setSize(new Dimension(width, height))
  setVisible(true)
}

class MandelbrotePane(points: mutable.Map[(Int, Int), Int], height: Int, width: Int, maxIterations: Int) extends JPanel {

  override def paintComponent(g: Graphics) {
    var histogram: Array[Int] = new Array[Int](maxIterations)

    for (px <- 0 until width) {
      for (py <- 0 until height) {
        val numIters = points(px, py)
        histogram(numIters - 1) += 1
      }
    }
    var total = 0
    for (i <- 0 until maxIterations) {
      total += histogram(i)
    }

    for (px <- 0 until width) {
      for (py <- 0 until height) {
        val numIters = points(px, py)

        var colorVal = 0.0
        for (i <- 0 until numIters) {
          colorVal += histogram(i).toFloat / total.toFloat
        }

        val rgb = Color.HSBtoRGB(0.1f + colorVal.toFloat, 1.0f, colorVal.toFloat * colorVal.toFloat)
        g.setColor(new Color(rgb))
        g.drawLine(px, py, px, py)
      }
    }
  }
}