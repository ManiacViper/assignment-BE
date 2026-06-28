package ice.finance.repository

import cats.effect.IO
import fs2.{Stream, text}
import ice.finance.repository.FileReaderRepository.RawRow

trait FileReaderRepository {
  def getLines(): Stream[IO, RawRow]
}

object FileReaderRepository {
  case class RawRow(clientId: String, serviceId: String, totalAmount: String)

  def apply(path: String): FileReaderRepository = new FileReaderRepository {
    override def getLines(): Stream[IO, RawRow] = {
      fs2.io
        .readClassLoaderResource[IO](path)
        .through(text.utf8.decode)
        .through((text.lines))
        .map { row =>
          val Array(clientId, serviceId, totalAmount) = row.split(",")
          RawRow(clientId, serviceId, totalAmount)
        }
        .drop(1)
    }
  }
}
