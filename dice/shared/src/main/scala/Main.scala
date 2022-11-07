import cats.effect._
import cats.effect.std.Random
import CLI.CLICommand

object Main extends IOApp {
  override def run(args: List[String]): IO[ExitCode] = for {
    r <- Random.scalaUtilRandom[IO]
    given Random[IO] = r
    exitCode <- CLI.cli.parse(args) match {
      case Left(h) => IO.println(h).as(ExitCode.Error)
      case Right(cmd) => cmd match {
        case CLICommand.RollCmd(rolls) => Logic.roller[IO](rolls).flatMap(IO.println).as(ExitCode.Success)
        case CLICommand.AnalyseCmd(rolls) => IO.println(Logic.analyse(rolls)).as(ExitCode.Success)
        case CLICommand.ReplCmd() => Repl[IO].as(ExitCode.Success)
      }
    }
  } yield exitCode
}
