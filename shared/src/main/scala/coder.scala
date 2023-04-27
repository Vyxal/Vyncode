package vycoder

import scala.collection.mutable.ListBuffer

case class Coder():
  def binList(x: BigInt, len: Int): Seq[Int] =
    // Scala equivalent of the following python:
    // return [int(bool(x & (2**n))) for n in reversed(range(len))]
    len - 1 to 0 by -1 map (i => (x.>>(i) & 1).toInt)

  def fromBin(x: Seq[Int]): BigDecimal =
    // Scala equivalent on the following python:
    // return int("".join(str(b) for b in x), base=2) if len(x) else 0

    if x.length == 0 then 0 else BigDecimal(BigInt(x.mkString, 2))

  // TODO Avoid doing bits.toInt just in case it exceeds Int bounds
  /** Raise 2 to the power of `bits` */
  def pow2(bits: Int): BigDecimal =
    if bits >= 0 then BigDecimal(BigInt(1) << bits.toInt)
    else BigDecimal(1.0) / BigDecimal(BigInt(1) << -bits.toInt)

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

      bottom *= pow2(bitsToAdd).toBigInt
      top = (top + 1) * pow2(bitsToAdd).toBigInt - 1

      bits += bitsToAdd

      val ranges =
        prediction(program.map(BigDecimal(_)).slice(0, i))
          .scanLeft(BigDecimal(0))(_ + _)
          .drop(1) // cumulative sum
      val intedRanges = bottom +: ranges.map(y =>
        y.toBigInt * (top + 1 - bottom) / ranges.last.toBigInt + bottom
      )

      // println(prediction(program.map(BigDecimal(_)).slice(0, i)))

      bottom = intedRanges(program(i).toInt)
      top = intedRanges(program(i).toInt + 1) - 1

      val differentBits = (top ^ bottom).bitLength
      val bitsToStore = bits - differentBits

      // println(binList(top.toInt, bits.toInt))
      out ++= binList(top.toInt, bits.toInt).slice(0, bitsToStore.toInt)

      // println(s"out: $out")

      bits = differentBits
      bottom &= pow2(bits.toInt).toBigInt - 1
      top &= pow2(bits.toInt).toBigInt - 1

    if bottom == 0 then
      if top + 1 != pow2(bits.toInt) then out += 0
    else
      out += 1
      for i <- 0 until (bits - (top - bottom + 1).bitLength) do out += 0

    out.toSeq

  def decode(
      program: Seq[Int],
      prediction: Seq[BigDecimal] => ListBuffer[BigDecimal],
      minBits: Int = 16
  ): Seq[Int] =
    val out = ListBuffer[Int]()

    var top: BigInt = 0
    var bottom: BigInt = 0

    var bits: BigInt = 0
    var acc: BigInt = 0
    var i: BigInt = 0
    var consumed: BigInt = 0

    while BigDecimal(top - bottom + 1) > pow2((i - program.length + 1).toInt) do

      var bitsToAdd = minBits - (top + 1 - bottom).bitLength + 1
      if bitsToAdd < 0 then bitsToAdd = 0

      bottom *= pow2(bitsToAdd).toBigInt // 2**bits_to_add
      top = (top + 1) * (pow2(bitsToAdd).toBigInt) - 1

      var l = (program.length - i).min(bitsToAdd).max(0)
      acc = acc * pow2(l.toInt).toBigInt + fromBin(
        program.slice(i.toInt, (i + l).toInt)
      ).toBigInt
      acc *= pow2((bitsToAdd - l).toInt).toBigInt

      i += bitsToAdd

      /*
              bits += bits_to_add

        ranges = list(accumulate(prediction(out)))
        ranges = [int(y) * (top+1 - bottom) // int(ranges[-1]) + bottom for y in ranges]
       */

      bits += bitsToAdd

      val ranges =
        prediction(out.toSeq.map(BigDecimal(_)))
          .scanLeft(BigDecimal(0))(_ + _)
          .drop(1) // cumulative sum
      val intedRanges = ranges.map(y =>
        y.toBigInt * (top + 1 - bottom) / ranges.last.toBigInt + bottom
      )

      // next(j for j in range(len(ranges)) if ranges[j] > acc)
      val x = ranges.indices.find(j => intedRanges(j) > acc).get
      out += x

      intedRanges.insert(0, bottom)

      bottom = intedRanges(x)
      top = intedRanges(x + 1) - 1

      val differentBits = (top ^ bottom).bitLength
      consumed += bits - differentBits

      bits = differentBits
      bottom &= pow2(bits.toInt).toBigInt - 1
      top &= pow2(bits.toInt).toBigInt - 1
      acc &= pow2(bits.toInt).toBigInt - 1
    out.toSeq
