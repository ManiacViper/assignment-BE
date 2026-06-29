package ice.finance.domain

import cats.data.NonEmptyList

import scala.language.implicitConversions
import cats.syntax.either._
import cats.syntax.parallel._

import java.math.{MathContext, RoundingMode}

final case class CalculationBigDecimal(bigDecimal: BigDecimal) extends AnyVal
object CalculationBigDecimal {
  private val precision       = 12
  private val scale           = 6
  private val mc: MathContext = new MathContext(precision, RoundingMode.HALF_EVEN)
  implicit def calculationBigDecimal(value: BigDecimal): CalculationBigDecimal = {
    val scaledValue: BigDecimal = value.setScale(scale, math.BigDecimal.RoundingMode.HALF_EVEN)
    CalculationBigDecimal(new BigDecimal(scaledValue.bigDecimal, mc))
  }
}

case class ServiceDetails(id: Long, totalAmount: CalculationBigDecimal)
case class ServiceCommissionCalculated(
  id: Long,
  commissionAmount: CalculationBigDecimal
)
case class CommissionCalculator(totalAmount: CalculationBigDecimal) {

  import CalculationBigDecimal.calculationBigDecimal

  private val rate: CalculationBigDecimal = totalAmount.bigDecimal match {
    case value if value > 0 && value <= 1000 =>
      CalculationBigDecimal(10)
    case value if value > 1000 && value <= 3000 =>
      BigDecimal(5)
    case value if value > 3000 && value <= 1000000 =>
      BigDecimal(1)
    case _ =>
      BigDecimal(0)
  }
  def calculate: CalculationBigDecimal =
    rate.bigDecimal / CalculationBigDecimal(100).bigDecimal * totalAmount.bigDecimal
}
object CommissionCalculator {
  def fromTotalAmount(amount: CalculationBigDecimal): CommissionCalculator =
    CommissionCalculator(amount)
}

case class InputValidator(serviceId: String, totalAmount: String) {
  import CalculationBigDecimal.calculationBigDecimal
  private val validateTotalAmount: Either[NonEmptyList[String], CalculationBigDecimal] = for {
    amountConverted <- Either
      .catchNonFatal(BigDecimal(totalAmount))
      .leftMap(_ =>
        NonEmptyList.one(
          s"[serviceId=${serviceId}] totalAmount=$totalAmount, should be an integer"
        )
      )
    validatedAmount <- amountConverted match {
      case value if value < 0 =>
        Left(
          NonEmptyList.one(
            s"[serviceId=${serviceId}] $value is invalid, total amount should be in the range of 0 to a million"
          )
        )
      case value if value > 1000000 =>
        Left(
          NonEmptyList.one(
            s"[serviceId=${serviceId}] $value is invalid, total amount should be in the range of 0 to a million"
          )
        )
      case value =>
        Right(value)
    }
  } yield validatedAmount

  private val validateServiceId: Either[NonEmptyList[String], Long] = for {
    serviceIdConverted <- Either
      .catchNonFatal(serviceId.toLong)
      .leftMap(_ =>
        NonEmptyList.one(
          s"[serviceId=${serviceId}] serviceId should be an integer"
        )
      )
    validatedServiceId <- Either.cond(
      serviceIdConverted > 0,
      serviceIdConverted,
      NonEmptyList.one(
        s"[serviceId=$serviceIdConverted] serviceId should be a positive integer and non zero"
      )
    )
  } yield validatedServiceId

  def validate: Either[NonEmptyList[String], ServiceDetails] =
    (
      validateServiceId,
      validateTotalAmount
    ).parMapN { case (serviceId, totalAmount) =>
      ServiceDetails(serviceId, totalAmount.bigDecimal)
    }

}
