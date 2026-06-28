package ice.finance

import cats.effect.IO
import cats.effect.IOApp
import ice.finance.service.RowValidatorService

object Main extends IOApp.Simple {
  def run =
    Stream.stream(RowValidatorService())
}
