package ice.finance.repository

import cats.effect.IO
import weaver._

import java.io.FileNotFoundException

object ServicesFileReaderRepositorySpec extends SimpleIOSuite {

  test("should read the lines in the file") {
    val fileReader = FileReaderRepository("test-services.csv")
    for {
      result <- fileReader.getLines().compile.count
    } yield expect.eql(result, 3)
  }

  test("return friendly error when file does not exist") {
    val fileName   = "non-existing-file.csv"
    val fileReader = FileReaderRepository(fileName)

    fileReader
      .getLines()
      .compile
      .toList
      .attempt
      .map {
        case Right(_) =>
          failure("file reader did not fail which is incorrect")
        case Left(error) =>
          expect.eql(error.getMessage, "non-existing-file.csv not found")
      }
  }

}
