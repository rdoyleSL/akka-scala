package org.bohdi.mandelbrot

import java.awt.{Color, Dimension, Graphics}
import javax.swing.{JFrame, JPanel}

import scala.collection.immutable.{SortedMap, TreeMap}
import scala.collection.mutable

object Display {

  def histogram(points: Map[(Int, Int), Int]): Map[Int, Int] = {
   points.values.groupBy(identity).mapValues(_.size)
  }

  def cumulativeHistogram(points: Map[(Int, Int), Int]): SortedMap[Int, Int] = {
    val sorted = TreeMap[Int, Int]() ++ histogram(points)

    var total = 0
    val cum = for ((k,v) <- sorted) yield {
      total += v
      (k, total)
    }
    cum
  }

  def pallete(maxIterations: Int,
              points: Map[(Int, Int), Int]): Map[Int, Color] = {

    val cumulative = cumulativeHistogram(points) - maxIterations
    val total = cumulative(cumulative.lastKey)

    val p = for {
      x <- cumulative
      ratio = (x._2.toFloat)/(total)
      _ <- 1 to 1
      _ = println(s"${x._1}, ${x._2}, $total, Ratio $ratio")
      rgb = Color.HSBtoRGB(0.1f + ratio, 1.0f, ratio*ratio)
    } yield (x._1, new Color(rgb))

    p.updated(maxIterations, Color.BLACK)
  }



}

class Display(points: Map[(Int, Int), Int], height: Int, width: Int, maxIterations: Int) extends JFrame {

  setContentPane(new MandelbrotePanel(points, height, width, maxIterations))
  setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)
  setSize(new Dimension(width, height))
  setVisible(true)
}

class MandelbrotePanel(points: Map[(Int, Int), Int], height: Int, width: Int, maxIterations: Int) extends JPanel {

  import Display._

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

    println(s"original total $total")

    val pallete1 = pallete(maxIterations, points)

    for (px <- 0 until width) {
      for (py <- 0 until height) {
        val numIters = points(px, py)

        var colorVal = 0.0
        for (i <- 0 until numIters) {
          colorVal += histogram(i).toFloat / total.toFloat
        }

        g.setColor(pallete1(numIters))
        g.drawLine(px, py, px, py)
      }
    }
  }

  def xcreatePallete(points: mutable.Map[(Int, Int), Int], n: Int): Array[Color] = {
    println(s"createPallet maxInterations $n, max in map ${points.values.max}")
    val accum = new Array[Int](n+1)
    // this is wrong we need the running count
    val histogram = points.values.foldLeft(accum)((z, iterations) => {z(iterations) += 1; z})
    val total = histogram.sum
    println(s"new total $total")
    histogram.map(_.toFloat/total.toFloat).map(f => Color.HSBtoRGB(0.1f + f, 1.0f, f * f)).map(new Color(_))


  }
}