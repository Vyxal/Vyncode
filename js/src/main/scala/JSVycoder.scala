package vycoder

import scala.scalajs.js
import scala.scalajs.js.annotation.{JSExport, JSExportTopLevel}
import scala.scalajs.js.JSConverters.*
given Conversion[Int, BigDecimal] with
  def apply(i: Int): BigDecimal = BigDecimal(i)

@JSExportTopLevel("Vycoder")
object JSVycoder:
  @JSExport
  def encode(code: String): String =
    val coder = Coder()
    val predictionObj = Predictions()
    predictionObj.initalise()

    val predictionFunction = predictionObj.weightedPositions(
      (x: BigDecimal) => BigDecimal("0.5").pow(x.toInt),
      32,
      128
    )
    // Read program from stdin

    val program = Codepage.vyxalToInt(code)
    val encoded = coder.encode(program, predictionFunction)

    encoded.mkString

  @JSExport
  def decode(bits: String): String =
    val coder = Coder()
    val predictionObj = Predictions()
    predictionObj.initalise()

    val predictionFunction = predictionObj.weightedPositions(
      (x: BigDecimal) => BigDecimal("0.5").pow(x.toInt),
      32,
      128
    )
    // Read program from stdin

    val program = bits.map(_.asDigit).toSeq
    val decoded = coder.decode(program, predictionFunction)

    Codepage.intToVyxal(decoded).mkString
