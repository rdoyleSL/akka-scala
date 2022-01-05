Mandelbrot akka-scala
==========

An introduction to the Akka framework using the Scala language.

This repository contains a simple example of the Akka framework. It uses Akka actors to
calculate whether complex numbers lie in the [Mandelbrot Set](http://en.wikipedia.org/wiki/Mandelbrot_set).
Through delegating the computation for ranges of complex numbers to different actors, we can perform
the calculations concurrently and decrease the time it takes to calculate the complete range of complex numbers.

As of 2022 does not build. :-(