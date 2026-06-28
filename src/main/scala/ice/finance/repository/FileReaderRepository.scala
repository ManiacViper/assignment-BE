package ice.finance.repository

import cats.effect.IO
import fs2.{Stream, text}

trait FileReaderRepository {
  def getLines(): Stream[IO, String]
}

object FileReaderRepository {
  def apply(path: String): FileReaderRepository = new FileReaderRepository {
    override def getLines(): Stream[IO, String] = {
      fs2.io
        .readClassLoaderResource[IO](path)
        .through(text.utf8.decode)
        .through((text.lines))
        .drop(1)
    }
  }
}
