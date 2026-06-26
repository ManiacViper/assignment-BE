package ice.finance.domain

import org.scalatest.matchers.must.Matchers.convertToAnyMustWrapper
import org.scalatest.matchers.should.Matchers._
import org.scalatest.wordspec.AnyWordSpec

class CommissionChargedSpec extends AnyWordSpec {

  "commission charged" should {
    "calculate the commission correctly" when {
      "its equal to or under 1000" in {
        val zero = CommissionCalculator(0).calculate
        val underThousand = CommissionCalculator(900).calculate
        val equalToThousand = CommissionCalculator(1000).calculate

        zero mustBe 0.0
        underThousand mustBe 90.00
        equalToThousand mustBe 100.00
      }

      "its more than 1000 and equal or under 3000" in {
        val moreThanThousand = CommissionCalculator(2001).calculate
        val equalToThreeThousand = CommissionCalculator(3000).calculate

        moreThanThousand mustBe 100.05
        equalToThreeThousand mustBe 150.00
      }

      "its more than 3000 and equal or under a million" in {
        val moreThanThousand = CommissionCalculator(3001).calculate
        val equalToThreeThousand = CommissionCalculator(1000000).calculate

        moreThanThousand mustBe 30.01
        equalToThreeThousand mustBe 10000.00
      }
    }
  }

  "converting from service rendered" should {

    "return an error" when {
      "the amount is a negative number" in {
        val Left(errorResult) = CommissionCalculator.fromTotalAmount(-1)
        errorResult mustBe "-1 is invalid, amount should be in the range of 0 to a million"
      }
      "its more than a million" in {
        val Left(errorResult) = CommissionCalculator.fromTotalAmount(1000001)
        errorResult mustBe "1000001 is invalid, amount should be in the range of 0 to a million"
      }
    }

    "return the correct commission charged" when {
      "under or equal to 1000 but still a positive number" in {
        val Right(resultForZero) = CommissionCalculator.fromTotalAmount(0)
        val Right(resultForTen) = CommissionCalculator.fromTotalAmount(10)
        val Right(resultForMillion) = CommissionCalculator.fromTotalAmount(1000000)


        resultForZero mustBe CommissionCalculator(0)
        resultForTen mustBe CommissionCalculator(10)
        resultForMillion mustBe CommissionCalculator(1000000)
      }
    }
  }

}
