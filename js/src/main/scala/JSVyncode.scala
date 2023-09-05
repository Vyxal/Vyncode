package vyncode

import scala.scalajs.js
import scala.scalajs.js.annotation.{JSExport, JSExportTopLevel}

@JSExportTopLevel("Vyncode")
class JSVyncode(version: Int = 2):
  if !JSVyncode.versions.contains(version) then
    throw new IllegalArgumentException("Invalid version")

  private val predictionObj = Predictions(version)

  @JSExport
  def encode(code: String): String =
    val predictionFunction = predictionObj.weightedPositions(
      (x: BigDecimal) => BigDecimal("0.5").pow(x.toInt),
      32,
      128,
    )
    // Read program from stdin

    val program = Codepage.vyxalToInt(code)
    val encoded = Coder.encode(program, predictionFunction)

    encoded.mkString

  @JSExport
  def decode(bits: String): String =
    val predictionFunction = predictionObj.weightedPositions(
      (x: BigDecimal) => BigDecimal("0.5").pow(x.toInt),
      32,
      128,
    )
    // Read program from stdin

    val program = bits.map(_.asDigit).toSeq
    val decoded = Coder.decode(program, predictionFunction)

    Codepage.intToVyxal(decoded).mkString
end JSVyncode

object JSVyncode:
  val versions: Seq[Int] = Seq(1, 2)
