package ice.finance.domain

import org.scalatest.matchers.must.Matchers.convertToAnyMustWrapper
import org.scalatest.matchers.should.Matchers._
import org.scalatest.wordspec.AnyWordSpec
import CalculationBigDecimal.calculationBigDecimal

class CommissionCalculatorSpec extends AnyWordSpec {

  "commission calculator" should {
    "calculate the commission correctly" when {
      "its equal to or under 1000 it should apply 10% commission" in {
        val zero      = CommissionCalculator(BigDecimal(0)).calculate.bigDecimal
        val resultOne = CommissionCalculator(BigDecimal(900)).calculate.bigDecimal
        val resultTwo = CommissionCalculator(BigDecimal(1000)).calculate.bigDecimal

        zero mustBe 0.0000000
        resultOne mustBe 90.000000
        resultTwo mustBe 100.000000
      }

      "its more than 1000 and equal or under 3000 it should apply 5% commission" in {
        val resultOne = CommissionCalculator(BigDecimal(2001)).calculate.bigDecimal
        val resultTwo = CommissionCalculator(BigDecimal(2999.123456)).calculate.bigDecimal

        resultOne mustBe 100.050000
        resultTwo mustBe 149.956173
      }

      "its more than 3000 and equal or under a million it should apply 1% commission" in {
        val resultOne = CommissionCalculator(BigDecimal(3001)).calculate.bigDecimal
        val resultTwo = CommissionCalculator(BigDecimal(1000000)).calculate.bigDecimal

        resultOne mustBe 30.010000
        resultTwo mustBe 10000.000000
      }

      "amount is a negative number, apply 0% commission" in {
        val result = CommissionCalculator(BigDecimal(-1)).calculate.bigDecimal
        result mustBe 0
      }
    }

    "return 13 digits of precision and 6 scale" when {
      "it is used" in {
        val result: CalculationBigDecimal = BigDecimal(999999.123456)
        result.bigDecimal.precision mustBe 12
        result.bigDecimal.scale mustBe 6
      }
      "half even is applied to increase the scale cut off decimal digit" when {
        "its more than or equal to 5" in {
          val resultOne: CalculationBigDecimal = BigDecimal(999999.6934415)
          val resultTwo: CalculationBigDecimal = BigDecimal(999999.1234566)
          resultOne.bigDecimal mustBe 999999.693442
          resultTwo.bigDecimal mustBe 999999.123457

        }
      }
      "decreases when half even is applied" when {
        "its less than 5" in {
          val resultOne: CalculationBigDecimal = BigDecimal(999999.1233443)
          resultOne.bigDecimal mustBe 999999.123344
        }
      }
    }
  }

  "converting from total amount" should {
    "return the commission calculator" when {
      "it is passed" in {
        val resultForTen = CommissionCalculator.fromTotalAmount(BigDecimal(10))
        resultForTen mustBe CommissionCalculator(BigDecimal(10))
      }
    }
  }

}
