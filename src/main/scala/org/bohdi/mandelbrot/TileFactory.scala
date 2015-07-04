
package org.bohdi.mandelbrot

object TileFactory {

  def generate(size: Int, chunk: Int): Seq[(Int, Int)] = {

    def iter(offset: Int, remaining: Int, accum: List[(Int, Int)]): List[(Int, Int)] = {
      if (remaining > chunk) (offset, chunk) :: iter(offset+chunk, remaining-chunk, accum)
      else (offset, remaining) :: accum
    }

    iter(0, size, List())
  }

  def cross(xs: Seq[(Int, Int)], ys: Seq[(Int, Int)]) = {
    for {
      x <- xs
      y <- ys
    } yield (Tile(x._1, y._1, x._2, y._2))
  }

  def create(width: Int, height: Int, n: Int) = cross(generate(width, width/n), generate(height, height/n))


}


