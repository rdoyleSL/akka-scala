package org.bohdi.mandelbrot

import java.awt.Color

import org.scalatest._

class PalletFactoryTest extends FlatSpec with Matchers {

  behavior of "A CyclePallet Object"


  it should "blagr" in {
    val p = new CyclePallete()

    color(0, 1, 0) should equal(p(1))
    color(0, 100, 0) should equal(p(100))
    color(3, 232, 0) should equal(p(1000))


  }

  def color(r: Int, g: Int, b: Int) = new Color(r, g, b)
}
