package ice.finance.domain

import cats.data.NonEmptyList
import org.scalatest.matchers.must.Matchers.convertToAnyMustWrapper
import org.scalatest.matchers.should.Matchers._
import org.scalatest.wordspec.AnyWordSpec

class InputValidatorSpec extends AnyWordSpec {
  "RowValidatorService" should {
    val clientId = "some-client-id"

    "convert to a service detail" when {
      "all fields are correct types" in {
        val Right(result) = InputValidator(clientId, "1111", "20000").validate
        result mustBe ServiceDetails(clientId, 1111, 20000)
      }
      "total amount is 0" in {
        val Right(result) = InputValidator(clientId, "1111", "0").validate
        result mustBe ServiceDetails(clientId, 1111, 0)
      }
    }
    "return errors" when {
      "service id is an invalid type" in {
        val Left(error) = InputValidator(clientId, "not a valid type", "20000").validate
        error.toList should contain theSameElementsAs List(
          "serviceId=not a valid type, should be an integer"
        )
      }

      "service id is zero" in {
        val Left(error) = InputValidator(clientId, "0", "20000").validate
        error.toList should contain theSameElementsAs List(
          "serviceId=0, should be a positive integer and non zero"
        )
      }

      "service id is negative integer" in {
        val Left(error) = InputValidator(clientId, "-1", "20000").validate
        error.toList should contain theSameElementsAs List(
          "serviceId=-1, should be a positive integer and non zero"
        )
      }

      "total amount is negative" in {
        val Left(error) = InputValidator(clientId, "1234", "-20000").validate
        error.toList should contain theSameElementsAs List(
          "-20000 is invalid, total amount should be in the range of 0 to a million"
        )
      }

      "total amount is more than a million" in {
        val Left(error) = InputValidator(clientId, "2222", "1000001").validate
        error.toList should contain theSameElementsAs List(
          "1000001 is invalid, total amount should be in the range of 0 to a million"
        )
      }
    }
  }
}
