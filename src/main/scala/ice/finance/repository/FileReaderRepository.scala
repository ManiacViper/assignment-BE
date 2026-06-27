package ice.finance.repository

import cats.effect.IO
import cats.effect.kernel.Resource
import fs2.{Stream, text}

import java.io.{FileNotFoundException, InputStream}

trait FileReaderRepository {
  def getLines(): Stream[IO, String]
}

object FileReaderRepository {
  def apply(path: String): FileReaderRepository = new FileReaderRepository {
    private val maybeInputStream: Option[InputStream] = Option(
      getClass.getResourceAsStream(s"/$path")
    )
    private val stream: Resource[IO, InputStream] =
      Resource.fromAutoCloseable(
        IO(maybeInputStream.getOrElse(throw new FileNotFoundException(s"${path} not found")))
      )
    override def getLines(): Stream[IO, String] =
      Stream
        .resource(stream)
        .flatMap { file =>
          fs2.io
            .readInputStream[IO](IO(file), chunkSize = 4096)
            .through(text.utf8.decode)
            .through(text.lines)
        }
  }
}
