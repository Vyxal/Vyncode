package vyncode
import scopt.OParser
object CLI:
  enum Method:
    case Encode, Decode
  case class CLIConfig(
      method: Method = Method.Encode,
      version: Int = 2,
      input: Option[String] = None
  )

  private val builder = OParser.builder[CLIConfig]
  private val parser =
    import builder.*
    OParser.sequence(
      programName("Vyncoder"),
      head("Vyncoder", "2.0"),
      opt[Unit]('h', "help")
        .action((_, cfg) =>
          println("Vyncoder - A Vyxal encoder/decoder")
          cfg
        )
        .text("Prints this message and exits")
        .optional(),
      opt[String]('m', "method")
        .action((m, cfg) =>
          m match
            case "encode" => cfg.copy(method = Method.Encode)
            case "decode" => cfg.copy(method = Method.Decode)
            case _ =>
              println("Invalid method")
              cfg
        )
        .text("The method to use (encode/decode). Default = encode"),
      opt[String]('v', "version")
        .action((v, cfg) => cfg.copy(version = v.toInt))
        .text("The version to use. Default = 1")
        .optional(),
      opt[String]('p', "program")
        .action((p, cfg) => cfg.copy(input = Some(p)))
        .text("The program to encode/decode")
    )

  def run(args: Array[String]): Unit =
    OParser.parse(parser, args, CLIConfig()) match
      case Some(config) =>
        val predictionObject = Predictions()
        predictionObject.initalise(config.version)
        val coder = Coder()
        val predictionFunction = predictionObject.weightedPositions(
          (x: BigDecimal) => BigDecimal("0.5").pow(x.toInt),
          32,
          128
        )
        val program = config.input.get
        config.method match
          case Method.Encode =>
            val encoded = coder.encode(
              Codepage.vyxalToInt(program),
              predictionFunction
            )
            println(
              s"input size: ${config.input.get.length * 8} bits (${config.input.get.length} bytes), output size: ${encoded.length} bits (${encoded.length / 8f} bytes), ratio: ${encoded.length / (config.input.get.length * 8f)}"
            )
            println(encoded.mkString)
          case Method.Decode =>
            val bits = program.split("").map(_.toInt).toIndexedSeq
            val decoded = coder.decode(
              bits,
              predictionFunction
            )
            println(Codepage.intToVyxal(decoded).mkString)
      case None => ???
