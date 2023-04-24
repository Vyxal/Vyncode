package vycoder

given Conversion[Int, BigDecimal] with
  def apply(i: Int): BigDecimal = BigDecimal(i)
