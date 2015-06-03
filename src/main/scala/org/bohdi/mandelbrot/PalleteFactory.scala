package org.bohdi.mandelbrot

import java.awt.Color

import scala.collection.immutable.{TreeMap, SortedMap}


object PalleteFactory {
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

  def histoPallete(maxIterations: Int,
              points: Map[(Int, Int), Int]): Int => Color = {

    val cumulative = cumulativeHistogram(points) - maxIterations
    val total = cumulative(cumulative.lastKey)

    val p = for {
      x <- cumulative
      ratio = x._2.toFloat/total
      _ <- 1 to 1
      _ = println(s"${x._1}, ${x._2}, $total, Ratio $ratio")
      rgb = Color.HSBtoRGB(0.1f + ratio, 1.0f, ratio*ratio)
    } yield (x._1, new Color(rgb))

    p.updated(maxIterations, Color.BLACK)
  }

  def cyclePallete: Int => Color = {
    val p = (0 to 255).map((n: Int) => new Color(n, 0, 0))
    def f(n: Int) = {
      p(n%256)
    }
    f
  }

}
