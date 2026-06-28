package ice.finance

import cats.effect.IO
import ice.finance.repository.FileReaderRepository
import ice.finance.service.RowValidatorService
import fs2.Stream

object StreamingApp {
  def stream(rowValidatorService: RowValidatorService): IO[Unit] = {
    FileReaderRepository("services.csv")
      .getLines()
      .map(row => rowValidatorService.validateRow(row.clientId, row.serviceId, row.totalAmount))
      .flatMap {
        case Left(error: String) =>
          Stream.exec(IO.println(s"Error=$error"))
        case Right(value) =>
          Stream.emit(value)
      }
//      .through(text.utf8.encode)
//      .through(Files[IO].writeAll(Path("/")))
      .compile
      .drain
  }

}
