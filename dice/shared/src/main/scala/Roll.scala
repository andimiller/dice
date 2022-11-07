import cats.data.NonEmptyList
import cats.parse._

case class Roll(count: Int, sides: Int) {
  def max: Int = count*sides
  def min: Int = 1*count
  def average: Int = count*((1 to sides).sum / sides)
}
object Roll {
  private val digits = Numbers.digits.flatMap {s =>
    s.toIntOption match {
      case Some(value) => Parser.pure(value)
      case None => Parser.failWith(s"'$s' is not a valid integer'")
    }}
  val roll: Parser[Roll] = for {
    c <- digits
    _ <- Parser.char('d')
    s <- digits
  } yield Roll(c, s)
  private val whitespace = Parser.charsWhile(_.isWhitespace)

  val parser: Parser[NonEmptyList[Roll]] = roll.repSep(whitespace)
}
