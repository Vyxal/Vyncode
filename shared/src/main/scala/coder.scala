package vycoder

import scala.collection.mutable.ListBuffer

case class Coder():
  def binList(x: Int, len: Int): ListBuffer[Int] =
    // Scala equivalent of the following python:
    // return [int(bool(x & (2**n))) for n in reversed(range(len))]

    var out: ListBuffer[Int] = ListBuffer()
    for i <- (0 to len).reverse do
      if (x & (math.pow(2, i)).intValue()) == 0 then out += 0 else out += 1
    out

  def fromBin(x: ListBuffer[Int]): BigDecimal =
    // Scala equivalent on the following python:
    // return int("".join(str(b) for b in x), base=2) if len(x) else 0

    if x.length == 0 then 0 else BigDecimal(BigInt(x.mkString, 2))
