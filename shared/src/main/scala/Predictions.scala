package vyncode

import scala.collection.mutable.ListBuffer

class Predictions:
  var frequencies: ListBuffer[BigDecimal] = ListBuffer.fill(256)(0)
  var maxDistance: BigDecimal = 8
  var positions: ListBuffer[ListBuffer[ListBuffer[BigDecimal]]] =
    ListBuffer.fill(maxDistance.toInt)(
      ListBuffer.fill(257)(ListBuffer.fill(256)(0))
    )

  var initialised: Boolean = false
  var pairs: ListBuffer[ListBuffer[BigDecimal]] = ListBuffer()
  var version: Int = 2

  def initialise(ver: Int): Unit =
    initialised = true
    version = ver // unused for now, but may be used in the future
    val programs = Data.getData(version) // This may change based on version

    frequencies = ListBuffer.fill(256)(0)
    positions = ListBuffer.fill(maxDistance.toInt)(
      ListBuffer.fill(257)(ListBuffer.fill(256)(0))
    )

    for row <- programs do
      for i <- row.indices do
        frequencies(row(i.toInt).toInt) += 1
        for j <- 0 until maxDistance.toInt do
          if (i - j - 1) >= 0 then
            positions(j.toInt)(row((i - j - 1).toInt).toInt)(
              row(i.toInt).toInt
            ) += 1
          else positions(j.toInt)(256)(row(i.toInt).toInt) += 1
    pairs = positions(0)
  end initialise

  def weightedPositions(
      distanceWeight: BigDecimal => BigDecimal,
      alpha: BigDecimal,
      beta: BigDecimal,
  )(lst: Seq[BigDecimal]): ListBuffer[BigDecimal] =
    if !initialised then initialise(version)
    var out: ListBuffer[BigDecimal] = ListBuffer.fill(256)(0)
    for i <- (0 until maxDistance.toInt).reverse do
      if i < lst.length then
        out = positions(i.toInt)(lst(lst.length - i.toInt - 1).toInt)
          .zip(out)
          .map((a, b) => distanceWeight(BigDecimal(i)) * a + b)
      else if i == 0 then
        out = positions(i.toInt)(256)
          .zip(out)
          .map((a, b) => distanceWeight(BigDecimal(i)) * a + b)

    out = out
      .zip(frequencies)
      .map((x, y) => x * (frequencies.sum + 256 * alpha) + beta * (y + alpha))
    out
  end weightedPositions
end Predictions
