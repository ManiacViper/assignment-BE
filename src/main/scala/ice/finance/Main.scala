package ice.finance

import cats.effect.IOApp
import ice.finance.repository.FileReaderRepository
import ice.finance.service.{CommissionCalculatorService, RowValidatorService}

object Main extends IOApp.Simple {
  def run =
    StreamingApp.stream(
      "services.csv",
      "calculated.csv",
      FileReaderRepository(),
      RowValidatorService(),
      CommissionCalculatorService()
    )
}
