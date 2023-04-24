package vycoder

given Conversion[Int, BigDecimal] with
  def apply(i: Int): BigDecimal = BigDecimal(i)

object Main:
  def main(args: Array[String]): Unit =
    println("Hello, world!")
