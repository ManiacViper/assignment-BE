package ice.finance

import cats.effect.IOApp
import ice.finance.service.RowValidatorService

object Main extends IOApp.Simple {
  def run =
    StreamingApp.stream(RowValidatorService())
}
