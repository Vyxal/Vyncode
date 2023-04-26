package vycoder

import scala.collection.mutable.ListBuffer

case class Coder():
  def binList(x: Int, len: Int): Seq[Int] =
    // Scala equivalent of the following python:
    // return [int(bool(x & (2**n))) for n in reversed(range(len))]
    len - 1 to 0 by -1 map (i => x >> i & 1)

  def fromBin(x: Seq[Int]): BigDecimal =
    // Scala equivalent on the following python:
    // return int("".join(str(b) for b in x), base=2) if len(x) else 0

    if x.length == 0 then 0 else BigDecimal(BigInt(x.mkString, 2))

  // TODO Avoid doing bits.toInt just in case it exceeds Int bounds
  /** Raise 2 to the power of `bits` */
  def pow2(bits: Int): BigInt = BigInt(1 << bits.toInt)

  def encode(
      program: Seq[Int],
      prediction: Seq[BigDecimal] => ListBuffer[BigDecimal],
      minBits: Int = 16
  ): Seq[Int] =
    val out = ListBuffer[Int]()

    var top: BigInt = 0
    var bottom: BigInt = 0

    var bits: BigInt = 0

    for i <- program.indices do
      var bitsToAdd: Int =
        minBits - (top + 1 - bottom).bitLength + 1
      if bitsToAdd < 0 then bitsToAdd = 0

      bottom *= pow2(bitsToAdd)
      top = (top + 1) * pow2(bitsToAdd) - 1

      bits += bitsToAdd

      val ranges =
        prediction(program.map(BigDecimal(_)).slice(0, i))
          .scanLeft(BigDecimal(0))(_ + _)
          .drop(1) // cumulative sum
      val intedRanges = ranges.map(y =>
        y.toBigInt * (top + 1 - bottom) / ranges.last.toBigInt + bottom
      )

      intedRanges.insert(0, bottom)

      // println(intedRanges)

      bottom = intedRanges(program(i).toInt)
      top = intedRanges(program(i).toInt + 1) - 1

      val differentBits = (top ^ bottom).bitLength
      val bitsToStore = bits - differentBits

      // println(
      //  s"bottom: $bottom, top: $top, bits: $bits, bits_to_store: $bitsToStore"
      // )

      // println(binList(top.toInt, bits.toInt))
      out ++= binList(top.toInt, bits.toInt).slice(0, bitsToStore.toInt)

      // println(s"out: $out")

      bits = differentBits
      bottom &= pow2(bits.toInt) - 1
      top &= pow2(bits.toInt) - 1

      // println(
      //  s"AFTER: bottom: $bottom, top: $top, bits: $bits, bits_to_store: $bitsToStore"
      // )

    if bottom == 0 then
      if top + 1 != pow2(bits.toInt) then out += 0
    else
      out += 1
      for i <- 0 until (bits - (top - bottom + 1)).bitLength do out += 0

    out.toSeq
