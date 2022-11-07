import cats._
import cats.implicits._
import cats.data._
import cats.effect.std.Random

object Logic:
  def analyse(rolls: NonEmptyList[Roll]): String =
    show"""min:     ${rolls.map(_.min).sumAll}
          |average: ${rolls.map(_.average).sumAll}
          |max:     ${rolls.map(_.max).sumAll}""".stripMargin

  def roller[F[_]: Random: Monad](rolls: NonEmptyList[Roll]): F[String] = for {
    values <- rolls.traverse { r => List.fill(r.count)(r.sides).traverse { sides => Random[F].betweenInt(1, sides) } }
    subtotals = values.map(_.sumAll)
    total = values.map(_.sumAll).sumAll
  } yield show"""values:    ${values.map(_.mkString_("(", " ", ")")).mkString_("(", " ", ")")}
                |subtotals: ${subtotals.mkString_("(", " ", ")")}
                |total:     $total""".stripMargin
