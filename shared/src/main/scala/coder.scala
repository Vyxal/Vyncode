package vycoder

import scala.collection.mutable.ListBuffer

case class Coder():
  def binList(x: Int, len: Int): ListBuffer[Int] =
    // Scala equivalent of the following python:
    // return [int(bool(x & (2**n))) for n in reversed(range(len))]

    len - 1 to 0 by -1 map (i => x >> i & 1)

  def fromBin(x: ListBuffer[Int]): BigDecimal =
    // Scala equivalent on the following python:
    // return int("".join(str(b) for b in x), base=2) if len(x) else 0

    if x.length == 0 then 0 else BigDecimal(BigInt(x.mkString, 2))
