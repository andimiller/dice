import cats.implicits._
import cats.data._
import com.monovore.decline._

object CLI {

  enum CLICommand:
    case RollCmd(rolls: NonEmptyList[Roll])
    case AnalyseCmd(rolls: NonEmptyList[Roll])


  given Argument[Roll] = Argument.from("1d6") { s =>
    Roll.roll.parseAll(s).leftMap(es => es.show).toValidatedNel
  }

  val rolls = Opts.arguments[Roll]("1d6")

  val roll = Opts.subcommand("roll", "Roll some dice", true)(rolls).map(CLICommand.RollCmd(_))
  val analyse = Opts.subcommand("analyse", "Analyse some dice", true)(rolls).map(CLICommand.AnalyseCmd(_))

  val cli = Command("dice", "a utility for dice", true)(roll.orElse(analyse))

}
