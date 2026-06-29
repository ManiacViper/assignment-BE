package ice.finance.repository

import weaver._
import cats.instances.list._

object ServicesFileReaderRepositorySpec extends SimpleIOSuite {
  test("should read the lines in the file") {
    val fileName   = "test-services.csv"
    val fileReader = FileReaderRepository()
    for {
      result <- fileReader.getLines(fileName).compile.count
    } yield expect.eql(result, 2)
  }

  test("return error when file does not exist") {
    val fileName   = "non-existing-file.csv"
    val fileReader = FileReaderRepository()

    fileReader
      .getLines(fileName)
      .compile
      .toList
      .attempt
      .map {
        case Right(_) =>
          failure("file reader did not fail which is incorrect")
        case Left(error) =>
          expect.eql(
            error.getMessage,
            "Resource non-existing-file.csv not found"
          )
      }
  }

  test("return error when file is malformed") {
    val fileName   = "malformed.csv"
    val fileReader = FileReaderRepository()

    fileReader
      .getLines(fileName)
      .compile
      .toList
      .map {
        case List(Left(error), Left(errorTwo), Left(errorThree)) =>
          expect.eql(
            List(error, errorTwo, errorThree),
            List(s"$fileName is malformed", s"$fileName is malformed", s"$fileName is malformed")
          )
        case _ =>
          failure("file reader did not fail which is incorrect")
      }
  }

}
