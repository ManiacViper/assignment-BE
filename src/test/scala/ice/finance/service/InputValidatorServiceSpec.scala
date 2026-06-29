package ice.finance.service

import ice.finance.domain.ServiceDetails
import org.scalatest.matchers.must.Matchers.convertToAnyMustWrapper
import org.scalatest.wordspec.AnyWordSpec

class InputValidatorServiceSpec extends AnyWordSpec {
  "RowValidatorService" should {
    val clientId = "some-client-id"

    "convert to a service detail" when {
      "all fields are correct types" in {
        val Right(result) = InputValidatorService().validateRow(clientId, "1111", "20000")
        result mustBe ServiceDetails(clientId, 1111, 20000)
      }
      "total amount is 0" in {
        val Right(result) = InputValidatorService().validateRow(clientId, "1111", "0")
        result mustBe ServiceDetails(clientId, 1111, 0)
      }
    }
    "return errors" when {
      "service id is an invalid type" in {
        val Left(error) = InputValidatorService().validateRow(clientId, "not a valid type", "20000")
        error mustBe "serviceId=not a valid type, should be an integer"
      }

      "service id is zero" in {
        val Left(error) = InputValidatorService().validateRow(clientId, "0", "20000")
        error mustBe "serviceId=0, should be a positive integer and non zero"
      }

      "service id is negative integer" in {
        val Left(error) = InputValidatorService().validateRow(clientId, "-1", "20000")
        error mustBe "serviceId=-1, should be a positive integer and non zero"
      }

      "total amount is an invalid type" in {
        val Left(error) = InputValidatorService().validateRow(clientId, "1234", "-20000")
        error mustBe "-20000 is invalid, amount should be in the range of 0 to a million"
      }
    }
  }
}
