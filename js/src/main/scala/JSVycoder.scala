package vyncode

import scala.scalajs.js
import scala.scalajs.js.annotation.{JSExport, JSExportTopLevel}
import scala.scalajs.js.JSConverters.*

@JSExportTopLevel("Vyncode")
object JSVyncode:

  var version: Int = 2
  val versions: Seq[Int] = Seq(1, 2)
  var predictionObj: Predictions = Predictions()
  var initialised: Boolean = false

  @JSExport
  def setVersion(ver: Int): Unit =
    if ver == -1 then version = versions.max
    else if !versions.contains(ver) then throw new Exception("Invalid version")
    else version = ver
    predictionObj.initialise(version)
    initialised = true

  @JSExport
  def getVersion(): Int = version

  @JSExport
  def initialise(): Unit =
    predictionObj.initialise(version)
    initialised = true

  @JSExport
  def encode(code: String): String =
    if !initialised then throw new Exception("Vyncode not initialised")

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
    if !initialised then throw new Exception("Vyncode not initialised")

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
