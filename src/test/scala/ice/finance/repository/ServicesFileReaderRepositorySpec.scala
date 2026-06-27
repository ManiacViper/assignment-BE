package ice.finance.repository

import weaver._

object ServicesFileReaderRepositorySpec extends SimpleIOSuite {

  test("should read the lines in the file") {
    val fileReader = FileReaderRepository("test-services.csv")
    for {
      result <- fileReader.getLines().compile.count
    } yield expect.eql(result, 3)
  }

}
