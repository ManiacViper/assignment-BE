package ice.finance.repository

import cats.effect.IO
import cats.implicits.toBifunctorOps
import fs2.{Stream, text}
import ice.finance.repository.FileReaderRepository.RawRow

import scala.util.Try

trait FileReaderRepository {
  def getLines(path: String): Stream[IO, Either[String, RawRow]]
}

object FileReaderRepository {
  case class RawRow(serviceId: String, totalAmount: String)

  def apply(): FileReaderRepository = new FileReaderRepository {
    override def getLines(path: String): Stream[IO, Either[String, RawRow]] = {
      fs2.io
        .readClassLoaderResource[IO](path)
        .through(text.utf8.decode)
        .through(text.lines)
        .filter(_.nonEmpty)
        .map { row =>
          Try(row.split(",")).toEither
            .leftMap { throwable =>
              throwable.getMessage
            } match {
            case Right(Array(serviceId, totalAmount)) =>
              Right(RawRow(serviceId, totalAmount))
            case _ =>
              Left(s"$path is malformed")
          }

        }
    }
  }
}
