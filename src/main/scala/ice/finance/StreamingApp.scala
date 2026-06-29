package ice.finance

import cats.effect.IO
import fs2.io.file.{Files, Path}
import ice.finance.repository.FileReaderRepository
import ice.finance.service.{CommissionCalculatorService, InputValidatorService}
import fs2.{Stream, text}

object StreamingApp {
  def stream(
    inputFilePath: String,
    resultsFilePath: String,
    fileReaderRepository: FileReaderRepository,
    inputValidatorService: InputValidatorService,
    commissionCalculatorService: CommissionCalculatorService
  ): IO[Unit] = {
    val calculatedCommissionsStream: Stream[IO, String] = fileReaderRepository
      .getLines(inputFilePath)
      .map { row =>
        inputValidatorService
          .validateRow(row.clientId, row.serviceId, row.totalAmount)
      }
      .flatMap {
        case Left(errors) =>
          Stream.exec(IO.println(s"File row errors=${errors.toList.mkString(",")}"))
        case Right(value) =>
          Stream.emit(value)
      }
      .map(commissionCalculatorService.getCalculation)
      .map { calculated =>
        s"${calculated.id},${calculated.commissionAmount.bigDecimal}"
      }
      .intersperse("\n")

    calculatedCommissionsStream
      .through(text.utf8.encode)
      .through(Files[IO].writeAll(Path(resultsFilePath)))
      .compile
      .drain
  }

}
