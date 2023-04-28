package vycoder

import scala.io.StdIn
import scala.collection.mutable.ListBuffer

given Conversion[Int, BigDecimal] with
  def apply(i: Int): BigDecimal = BigDecimal(i)

object Main:
  def main(args: Array[String]): Unit =
    CLI.run(args)
