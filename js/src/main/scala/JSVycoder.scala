package vyncode

import scala.scalajs.js
import scala.scalajs.js.annotation.{JSExport, JSExportTopLevel}
import scala.scalajs.js.JSConverters.*
given Conversion[Int, BigDecimal] with
  def apply(i: Int): BigDecimal = BigDecimal(i)

@JSExportTopLevel("Vyncode")
object JSVyncode:

  var version: Int = 2
  val versions: Seq[Int] = Seq(1, 2)
  var predictionObj: Predictions = Predictions()
  var initalised: Boolean = false

  @JSExport
  def setVersion(ver: Int): Unit =
    if ver == -1 then version = versions.max
    else if !versions.contains(ver) then throw new Exception("Invalid version")
    else version = ver
    predictionObj.initalise(version)
    initalised = true

  @JSExport
  def getVersion(): Int = version

  @JSExport
  def initalise(): Unit =
    predictionObj.initalise(version)
    initalised = true

  @JSExport
  def encode(code: String): String =
    if !initalised then throw new Exception("Vyncode not initalised")
    val coder = Coder()

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
    if !initalised then throw new Exception("Vyncode not initalised")
    val coder = Coder()

    val predictionFunction = predictionObj.weightedPositions(
      (x: BigDecimal) => BigDecimal("0.5").pow(x.toInt),
      32,
      128
    )
    // Read program from stdin

    val program = bits.map(_.asDigit).toSeq
    val decoded = coder.decode(program, predictionFunction)

    Codepage.intToVyxal(decoded).mkString
