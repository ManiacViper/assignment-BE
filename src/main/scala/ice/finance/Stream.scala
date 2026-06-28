package ice.finance

import cats.effect.IO
import ice.finance.repository.FileReaderRepository
import ice.finance.service.RowValidatorService

object Stream {
  def stream(rowValidatorService: RowValidatorService) = {
    FileReaderRepository("services.csv")
      .getLines()
      .map(row => rowValidatorService.validateRow(row.clientId, row.serviceId, row.totalAmount))
      .compile
      .drain
  }

}
