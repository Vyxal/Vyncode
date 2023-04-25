package vycoder

import scala.io.StdIn
import scala.collection.mutable.ListBuffer

given Conversion[Int, BigDecimal] with
  def apply(i: Int): BigDecimal = BigDecimal(i)

object Main:
  def main(args: Array[String]): Unit =
    println(4.>>(1))
    val coder = Coder()
    val predictionObj = Predictions()
    predictionObj.initalise()

    val predicitionFunction = predictionObj.weightedPositions(
      (x: BigDecimal) => BigDecimal("0.5").pow(x.toInt),
      32,
      128
    )
    // Read program from stdin

    val program = Codepage.vyxalToInt(StdIn.readLine())
    val encoded = coder.encode(program, predicitionFunction)

    // print(f"input size: {len(int_lst)*8} bits ({len(int_lst)} bytes), output size: {len(bin_lst)} bits ({len(bin_lst)/8} bytes), ratio: {len(bin_lst)/(len(int_lst)*8)}")

    println(
      s"input size: ${program.length * 8} bits (${program.length} bytes), output size: ${encoded.length} bits (${encoded.length / 8f} bytes), ratio: ${encoded.length / (program.length * 8f)}"
    )

    println(encoded.mkString)
