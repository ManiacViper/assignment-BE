package ice.finance.service

import ice.finance.domain.ServiceDetails
import org.scalatest.matchers.must.Matchers.convertToAnyMustWrapper
import org.scalatest.wordspec.AnyWordSpec

class RowValidatorServiceSpec extends AnyWordSpec {
  "RowValidatorService" should {
    val clientId = "some-client-id"

    "convert to a service detail" when {
      "all fields are correct types" in {
        val Right(result) = RowValidatorService().validateRow(clientId, "1111", "20000")
        result mustBe ServiceDetails(clientId, 1111, 20000)
      }
    }
    "return errors" when {
      "service id is an invalid type" in {
        val Left(error) = RowValidatorService().validateRow(clientId, "not a valid type", "20000")
        error mustBe "serviceId=not a valid type, should be an integer"
      }

      "service id is zero" in {
        val Left(error) = RowValidatorService().validateRow(clientId, "0", "20000")
        error mustBe "serviceId=0, should be a positive integer and non zero"
      }

      "service id is negative integer" in {
        val Left(error) = RowValidatorService().validateRow(clientId, "-1", "20000")
        error mustBe "serviceId=-1, should be a positive integer and non zero"
      }

      "total amount is an invalid type" in {
        val Left(error) = RowValidatorService().validateRow(clientId, "1234", "-20000")
        error mustBe "totalAmount=-20000, should be a positive integer and non zero"
      }

      "total amount is zero" in {
        val Left(error) = RowValidatorService().validateRow(clientId, "1234", "0")
        error mustBe "totalAmount=0, should be a positive integer and non zero"
      }
    }
  }
}
