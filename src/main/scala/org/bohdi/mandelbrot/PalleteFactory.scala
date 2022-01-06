package org.bohdi.mandelbrot

import java.awt.Color

//import com.sun.xml.internal.bind.WhiteSpaceProcessor

trait Pallete {
  def apply(n: Int): Color
}

class CyclePallete(max: Int) extends Pallete {
  var big = 0
  val black = new Color(0, 0, 0)

  private val p = (0 to (256*256)-1).map(color _)

  def apply(n: Int) = {
    if (max == n) black
    else p(n%(256*256))
  }

  def color(n: Int) = {
    //println(s"$n ${n/64} ${n%256}")
    new Color(n/256, n%256, 0)
  }
}

class OddEvenPallete extends Pallete {
  var big = 0

  private val white = new Color(0, 0, 0)
  private val black = new Color(255, 255, 255)


  def apply(n: Int) = {
    if (n % 2 == 0) white
    else black
  }
}
  class ExactPallete extends Pallete {
    var big = 0

    private val white = new Color(0, 0, 0)
    private val red = new Color(255, 0, 0)
    private val black = new Color(255, 255, 255)



    def apply(n: Int) = {
      if (n == 5) white
        else if (n == 1001) red
      else black
    }

}



