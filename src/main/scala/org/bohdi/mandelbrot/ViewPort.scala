package org.bohdi.mandelbrot

case class ViewPort(centerX: Double = 0.0,
                    centerY: Double = 0.0,
                    width: Double = 5.0,
                    height: Double = 5
                     ) {

  val screenWidth = 1000.toDouble
  val screenHeight = 800.toDouble

  def translate(x: Int, y: Int): (Double, Double) = {
    // Convert the pixels to x, y co-ordinates in
    // the range x = (-2.5, 1.0), y = (-1.0, 1.0)
    val x0: Double = centerX - width/2 + width*(x.toDouble/screenWidth.toDouble)
    val y0: Double = centerY - height/2 + height*(y.toDouble/screenHeight.toDouble)
    (x0, y0)
  }

  def zoom(f: Double) = {
    copy(width = width* f, height = height * f)
  }

  def center(x: Double, y: Double) = {
    copy(centerX = x, centerY = y)
  }

  def panLeft = center(centerX - width/10, centerY)
  def panRight = center(centerX + width/10, centerY)
  def panUp = center(centerX, centerY - height/10)
  def panDown = center(centerX, centerY + height/10)

}
