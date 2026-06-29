package ice.finance

import cats.effect.IO
import fs2.io.file.{Files, Path}
import ice.finance.repository.FileReaderRepository
import ice.finance.service.{CommissionCalculatorService, RowValidatorService}
import fs2.{Stream, text}

object StreamingApp {
  def stream(
    inputFilePath: String,
    resultsFilePath: String,
    fileReaderRepository: FileReaderRepository,
    rowValidatorService: RowValidatorService,
    commissionCalculatorService: CommissionCalculatorService
  ): IO[Unit] = {
    val calculatedCommissionsStream: Stream[IO, String] = fileReaderRepository
      .getLines(inputFilePath)
      .map { row =>
        rowValidatorService
          .validateRow(row.clientId, row.serviceId, row.totalAmount)
      }
      .flatMap {
        case Left(error: String) =>
          Stream.exec(IO.println(s"File row error=$error"))
        case Right(value) =>
          Stream.emit(value)
      }
      .map(commissionCalculatorService.getCalculation)
      .flatMap {
        case Left(error: String) =>
          Stream.exec(IO.println(s"Calculation error=$error"))
        case Right(value) =>
          Stream.emit(value)
      }
      .map { calculated =>
        s"${calculated.id},${calculated.commissionAmount}"
      }
      .intersperse("\n")

    calculatedCommissionsStream
      .through(text.utf8.encode)
      .through(Files[IO].writeAll(Path(resultsFilePath)))
      .compile
      .drain
  }

}
