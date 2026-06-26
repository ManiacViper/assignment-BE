package ice.finance.domain

import org.scalatest.matchers.must.Matchers.convertToAnyMustWrapper
import org.scalatest.matchers.should.Matchers._
import org.scalatest.wordspec.AnyWordSpec

class CommissionChargedSpec extends AnyWordSpec {

  "commission charged" should {
    "calculate the commission correctly" when {
      "its equal to or under 1000" in {
        val underThousand = EqualOrUnderThousand(900).calculate
        val equalToThousand = EqualOrUnderThousand(1000).calculate

        underThousand mustBe 90.00
        equalToThousand mustBe 100.00
      }

      "its more than 1000 and equal or under 3000" in {
        val moreThanThousand = EqualOrUnderThreeThousand(2001).calculate
        val equalToThreeThousand = EqualOrUnderThreeThousand(3000).calculate

        moreThanThousand mustBe 100.05
        equalToThreeThousand mustBe 150.00
      }

      "its more than 3000 and equal or under a million" in {
        val moreThanThousand = AboveThreeThousand(3001).calculate
        val equalToThreeThousand = AboveThreeThousand(1000000).calculate

        moreThanThousand mustBe 30.01
        equalToThreeThousand mustBe 10000.00
      }
    }
  }

  "converting from service rendered" should {
    "return the correct commission charged" when {
      "under or equal to 1000 but still a positive number" in {
        val Right(resultForThousand) = CommissionCharged.fromTotalAmount(1000)
        val Right(resultForUnderThousand) = CommissionCharged.fromTotalAmount(10)

        resultForThousand mustBe EqualOrUnderThousand(1000)
        resultForUnderThousand mustBe EqualOrUnderThousand(10)
      }
    }

    "return an error" when {
      "there is a negative number" in {
        val Left(errorResult) = CommissionCharged.fromTotalAmount(-1)
        errorResult mustBe "-1 is invalid, amount should be in the range of 0 to a million"
      }
      "its more than a million" in {
        val Left(errorResult) = CommissionCharged.fromTotalAmount(1000001)
        errorResult mustBe "1000001 is invalid, amount should be in the range of 0 to a million"
      }
    }
  }

}
