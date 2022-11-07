import cats.{Monad, Show}
import cats.data.{EitherT, NonEmptyList, StateT}
import cats.effect.ExitCode
import cats.implicits.*
import cats.effect.std.{Console, Random}
import cats.mtl.Raise
import cats.mtl.Stateful
import cats.parse.Parser

object Repl {

  val exit: Parser[Unit] = Parser.string("quit").as(())

  enum Mode:
    case Analyser, Roller

  given Show[Mode] =
    case Mode.Analyser => "analyse"
    case Mode.Roller   => "roll"

  given Parser[Mode] =
    Parser
      .string(".analyse")
      .as(Mode.Analyser)
      .orElse(Parser.string(".roll").as(Mode.Roller))

  enum Command:
    case SwitchMode(mode: Mode)
    case Quit
    case DoRoll(rolls: NonEmptyList[Roll])

  object Command:
    val parser =
      Parser
        .string(".quit")
        .as(Command.Quit)
        .orElse(implicitly[Parser[Mode]].map(Command.SwitchMode))
        .orElse(Roll.parser.map(Command.DoRoll))


  def logic[F[_]: Monad: Random: Console](using fail: Raise[F, ExitCode], ctx: Stateful[F, Mode]): F[Unit] =
    for {
      mode  <- ctx.get
      _     <- Console[F].print(show"$mode > ")
      input <- Console[F].readLine
      _     <- Command.parser.parseAll(input) match {
                 case Left(e)    => Console[F].println(e)
                 case Right(cmd) =>
                   cmd match {
                     case Command.SwitchMode(m) => ctx.set(m)
                     case Command.Quit          => fail.raise(ExitCode.Success)
                     case Command.DoRoll(rolls) => mode match {
                       case Mode.Analyser => Console[F].println(Logic.analyse(rolls))
                       case Mode.Roller   => Logic.roller[F](rolls).flatMap(Console[F].println(_))
                     }
                   }
               }
    } yield ()

  type Stack[F[_]] = [A] =>> EitherT[[B] =>> StateT[F, Mode, B], ExitCode, A]

  def apply[F[_]: Monad: Random: Console]: F[Either[ExitCode, Nothing]] = logic[Stack[F]].foreverM.value.runA(Mode.Roller)
}
