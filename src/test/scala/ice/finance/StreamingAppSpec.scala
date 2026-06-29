package ice.finance

import cats.effect.IO
import fs2.io.file.{Files, Path}
import ice.finance.repository.FileReaderRepository
import ice.finance.service.{CommissionCalculatorService, RowValidatorService}
import weaver._

object StreamingAppSpec extends SimpleIOSuite {

  private val resultsFile = "src/test/resources/test-calculated-output.csv"

  test("should read raw data and write commission results") {
    for {
      _ <- StreamingApp
        .stream(
          "test-services.csv",
          resultsFile,
          FileReaderRepository(),
          RowValidatorService(),
          CommissionCalculatorService()
        )
      results <- StreamingAppSpec.readResults(resultsFile).compile.toList
    } yield {
      val expected =
        List(ActualResults(1L, BigDecimal(90.00)), ActualResults(2L, BigDecimal(100.00)))
      expect.same(results, expected)
    }

  }

  private case class ActualResults(serviceId: Long, totalAmount: BigDecimal)
  private def readResults(path: String) = {
    Files[IO]
      .readUtf8Lines(Path(path))
      .map { line: String =>
        val Array(id, commissionAmount) = line.split(",")
        ActualResults(id.toLong, BigDecimal(commissionAmount))
      }
      .evalTap { _ =>
        Files[IO].deleteIfExists(Path(path))
      }
  }
}
