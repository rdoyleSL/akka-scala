package org.bohdi.mandelbrot

import org.scalatest._

class TileFactoryTest extends FlatSpec with Matchers {

  behavior of "A Tile Object"


  it should "should generate sequence with perfect divisor" in {
    List( (0, 50), (50, 50)) should equal(TileFactory.generate(100, 50))
  }

  it should "should generate sequence with inperfect divisors" in {
    List( (0, 49), (49, 49), (98, 2)) should equal(TileFactory.generate(100, 49))
  }

  it should "should create the cross product of two sequences" in {
    val expected = List(
      Tile(0, 0, 50, 16),
      Tile(0, 16, 50, 16),
      Tile(50, 0, 50, 16),
      Tile(50, 16, 50, 16)
    )

    expected should equal(TileFactory.cross(TileFactory.generate(100, 50), TileFactory.generate(32, 16)))
  }

  it should "should create a list of Tiles" in {
    val expected = List(
      Tile(0, 0, 50, 25),
      Tile(0, 25, 50, 25),
      Tile(50, 0, 50, 25),
      Tile(50, 25, 50, 25)
    )

    expected should equal(TileFactory.create(100, 50, 2))
  }

  it should "should create a list of Tiles 2" in {
    val expected = List(
      Tile(0, 0, 50, 25),
      Tile(0, 25, 50, 25),
      Tile(0, 50, 50, 1),
      Tile(50, 0, 50, 25),
      Tile(50, 25, 50, 25),
      Tile(50, 50, 50, 1),
      Tile(100, 0, 1, 25),
      Tile(100, 25, 1, 25),
      Tile(100, 50, 1, 1)
    )

    expected should equal(TileFactory.create(101, 51, 2))
  }
}
