package vycoder

import scala.collection.mutable.ListBuffer

case class Coder():
  def binList(x: Int, len: Int): ListBuffer[Int] =
    // Scala equivalent of the following python:
    // return [int(bool(x & (2**n))) for n in reversed(range(len))]

    val temp = len - 1 to 0 by -1 map (i => x >> i & 1)
    ListBuffer(temp: _*)

  def fromBin(x: ListBuffer[Int]): BigDecimal =
    // Scala equivalent on the following python:
    // return int("".join(str(b) for b in x), base=2) if len(x) else 0

    if x.length == 0 then 0 else BigDecimal(BigInt(x.mkString, 2))

  def encode(
      program: ListBuffer[Int],
      prediction: ListBuffer[BigDecimal] => ListBuffer[BigDecimal],
      minBits: BigInt = 16
  ): ListBuffer[Int] =
    val out: ListBuffer[Int] = ListBuffer()

    var top: BigInt = 0
    var bottom: BigInt = 0

    var bits: BigInt = 0

    for i <- program.indices do
      var bitsToAdd: BigInt =
        minBits - (top + 1 - bottom).bitLength + 1
      if bitsToAdd < 0 then bitsToAdd = 0

      bottom *= bitsToAdd.>>(1.toInt)
      top = (top + 1) * bitsToAdd.>>(1.toInt) - 1

      bits += bitsToAdd

      val ranges =
        prediction(program.map(BigDecimal(_)).slice(0, i))
          .scanLeft(BigDecimal(0))(_ + _)
          .drop(1) // cumulative sum

      val intedRanges = ranges.map(
        _.toBigInt * (top + 1 - bottom) / ranges.last.toBigInt + bottom
      )

      intedRanges.insert(0, bottom)

      bottom = intedRanges(program(i).toInt)
      top = intedRanges(program(i).toInt + 1) - 1

      val differentBits = (top.^(bottom)).bitLength
      val bitsToStore = bits - differentBits

      out ++= binList(top.toInt, bitsToStore.toInt).slice(0, bitsToStore.toInt)

      bits = differentBits
      bottom &= bits.>>(1.toInt) - 1
      top &= bits.>>(1.toInt) - 1

    if bottom == 0 then
      if top + 1 != bits.>>(1.toInt) then out += 0
    else
      out += 1
      for i <- 0 until (bits - (top - bottom + 1)).bitLength do out += 0
    out
