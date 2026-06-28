package ice.finance

import cats.effect.IO
import cats.effect.IOApp

object Main extends IOApp.Simple {
  def run = IO.println(Stream.stream)
}
