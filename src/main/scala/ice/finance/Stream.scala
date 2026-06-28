package ice.finance

import cats.effect.IO
import ice.finance.repository.FileReaderRepository

object Stream {
  def stream =
    FileReaderRepository("services.csv")
      .getLines()
//      .through()
      .compile
      .drain

}
