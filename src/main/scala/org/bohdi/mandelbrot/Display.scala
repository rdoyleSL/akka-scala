package org.bohdi.mandelbrot

import java.awt.{Dimension, Graphics}
import javax.swing.{JFrame, JPanel}

object Display {

}

class Display(points: Map[(Int, Int), Int], height: Int, width: Int, maxIterations: Int) extends JFrame {

  setContentPane(new MandelbrotePanel(points, height, width, maxIterations))
  setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)
  setSize(new Dimension(width, height))
  setVisible(true)
}

class MandelbrotePanel(points: Map[(Int, Int), Int], height: Int, width: Int, maxIterations: Int) extends JPanel {

  override def paintComponent(g: Graphics) {
    println(s"About to draw")

    val pallete1 = PalleteFactory.cyclePallete

    for (px <- 0 until width) {
      for (py <- 0 until height) {
        val numIters = points(px, py)
        g.setColor(pallete1(numIters))
        g.drawLine(px, py, px, py)
      }
    }
    println(s"done to draw")
  }


}