package vycoder

given Conversion[Int, BigInt] with
  def apply(i: Int): BigInt = BigInt(i)

object Main:
  def main(args: Array[String]): Unit =
    println("Hello, world!")
