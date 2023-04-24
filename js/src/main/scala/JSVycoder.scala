package vycoder

given Conversion[Int, BigInt] with
  def apply(i: Int): BigInt = BigInt(i)
