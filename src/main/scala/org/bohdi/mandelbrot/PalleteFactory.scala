package org.bohdi.mandelbrot

import java.awt.Color

trait Pallete {
  def apply(n: Int): Color
}

class CyclePallete extends Pallete {
  private val p = (0 to 255).map((n: Int) => new Color(n, 0, 0))

  def apply(n: Int) = {
    p(n%256)
  }
}



