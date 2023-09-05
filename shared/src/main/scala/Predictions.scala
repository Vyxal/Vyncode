package vyncode

import scala.collection.mutable.ListBuffer

/**
  * @param version unused for now, but may be used in the future
  */
class Predictions(version: Int = 2):
  private val frequencies: ListBuffer[BigDecimal] = ListBuffer.fill(256)(0)
  private val maxDistance: BigDecimal = 8
  private val positions: ListBuffer[ListBuffer[ListBuffer[BigDecimal]]] =
    ListBuffer.fill(maxDistance.toInt)(
      ListBuffer.fill(257)(ListBuffer.fill(256)(0))
    )

  for row <- Data.getData(version) do // This may change based on version
    for i <- row.indices do
      frequencies(row(i.toInt).toInt) += 1
      for j <- 0 until maxDistance.toInt do
        if (i - j - 1) >= 0 then
          positions(j.toInt)(row((i - j - 1).toInt).toInt)(
            row(i.toInt).toInt
          ) += 1
        else positions(j.toInt)(256)(row(i.toInt).toInt) += 1

  def weightedPositions(
      distanceWeight: BigDecimal => BigDecimal,
      alpha: BigDecimal,
      beta: BigDecimal,
  )(lst: Seq[BigDecimal]): Seq[BigDecimal] =
    var out = Seq.fill(256)(BigDecimal(0))
    for i <- (0 until maxDistance.toInt).reverse do
      if i < lst.length then
        out = positions(i.toInt)(lst(lst.length - i.toInt - 1).toInt)
          .zip(out)
          .map((a, b) => distanceWeight(BigDecimal(i)) * a + b)
          .toSeq
      else if i == 0 then
        out = positions(i.toInt)(256)
          .zip(out)
          .map((a, b) => distanceWeight(BigDecimal(i)) * a + b)
          .toSeq

    out = out
      .zip(frequencies)
      .map((x, y) => x * (frequencies.sum + 256 * alpha) + beta * (y + alpha))
    out
  end weightedPositions
end Predictions
