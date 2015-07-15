package org.bohdi.mandelbrot

import com.typesafe.config.Config

class Settings(config: Config) {
  val maxIterations = config.getInt("mandelbrot.maxIterations")
  val height = config.getInt("mandelbrot.gui.height")
  val width = config.getInt("mandelbrot.gui.width")
  val workers = config.getInt("mandelbrot.workers")

}
