package ice.finance.domain

import org.scalatest.matchers.must.Matchers.convertToAnyMustWrapper
import org.scalatest.matchers.should.Matchers._
import org.scalatest.wordspec.AnyWordSpec

class InputValidatorSpec extends AnyWordSpec {
  "RowValidatorService" should {
    val clientId = "some-client-id"

    "convert to a service detail" when {
      "all fields are correct types" in {
        val Right(result) = InputValidator("1111", "20000").validate
        result mustBe ServiceDetails(1111, BigDecimal(20000))
      }
      "total amount is 0" in {
        val Right(result) = InputValidator("1111", "0").validate
        result mustBe ServiceDetails(1111, BigDecimal(0))
      }
    }
    "return errors" when {
      "service id is an invalid type" in {
        val Left(error) = InputValidator("not a valid type", "20000").validate
        error.toList should contain theSameElementsAs List(
          s"[clientId=$clientId,serviceId=not a valid type] serviceId should be an integer"
        )
      }

      "service id is zero" in {
        val Left(error) = InputValidator("0", "20000").validate
        error.toList should contain theSameElementsAs List(
          s"[clientId=$clientId,serviceId=0] serviceId should be a positive integer and non zero"
        )
      }

      "service id is negative integer" in {
        val Left(error) = InputValidator("-1", "20000").validate
        error.toList should contain theSameElementsAs List(
          s"[clientId=$clientId,serviceId=-1] serviceId should be a positive integer and non zero"
        )
      }

      "total amount is negative" in {
        val Left(error) = InputValidator("1234", "-20000").validate
        error.toList should contain theSameElementsAs List(
          s"[clientId=$clientId,serviceId=1234] -20000 is invalid, total amount should be in the range of 0 to a million"
        )
      }

      "total amount is more than a million" in {
        val Left(error) = InputValidator("2222", "1000001").validate
        error.toList should contain theSameElementsAs List(
          s"[clientId=$clientId,serviceId=2222] 1000001 is invalid, total amount should be in the range of 0 to a million"
        )
      }
    }
  }
}
