package vycoder

import scala.collection.mutable.ListBuffer

class Predictions:
  var frequencies: ListBuffer[BigDecimal] = ListBuffer.fill(256)(0)
  var maxDistance: BigDecimal = 8
  var positions: ListBuffer[ListBuffer[ListBuffer[BigDecimal]]] =
    ListBuffer.fill(maxDistance.toInt)(
      ListBuffer.fill(257)(ListBuffer.fill(256)(0))
    )

  var initalised: Boolean = false
  var pairs: ListBuffer[ListBuffer[BigDecimal]] = ListBuffer()

  def initalise(): Unit =
    initalised = true
    val programs = Data().getData()
    for row <- programs do
      for i <- row.indices do
        frequencies(row(i.toInt).toInt) += 1
        for j <- 0 until maxDistance.toInt do
          if i - j - 1 >= 0 then
            positions(j.toInt)((i - j - 1).toInt)(row(i.toInt).toInt) += 1
          else positions(j.toInt)(256)(row(i.toInt).toInt) += 1
    pairs = positions(0)

  def weightedPositions(
      distanceWeight: BigDecimal => BigDecimal,
      alpha: BigDecimal,
      beta: BigDecimal
  )(lst: Seq[BigDecimal]): ListBuffer[BigDecimal] =
    // println(lst)
    if !initalised then initalise()
    var out: ListBuffer[BigDecimal] = ListBuffer.fill(256)(0)
    for i <- (0 until maxDistance.toInt).reverse do
      if i < lst.length then
        out = positions(i.toInt)(lst(lst.length - i - 1).toInt)
          .zip(out)
          .map((a, b) => distanceWeight(BigDecimal(i)) * (a + b))
      else if i == 0 then
        out = positions(i.toInt)(256)
          .zip(out)
          .map((a, b) => distanceWeight(BigDecimal(i)) * (a + b))

    out = out
      .zip(frequencies)
      .map((x, y) => x * (frequencies.sum + 256 * alpha) + beta * (y + alpha))
    out
