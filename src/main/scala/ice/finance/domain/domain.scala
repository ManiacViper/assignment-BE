package ice.finance.domain

import cats.data.NonEmptyList

import scala.language.implicitConversions
import scala.math.BigDecimal.RoundingMode.HALF_EVEN
import cats.syntax.either._
import cats.syntax.parallel._

object BigDecimalConfig {
  implicit def bigDecimal(bigDecimal: BigDecimal): BigDecimal = bigDecimal.setScale(16, HALF_EVEN)
}

case class ServiceDetails(clientId: String, id: Long, totalAmount: BigDecimal)
case class ServiceCommissionCalculated(clientId: String, id: Long, commissionAmount: BigDecimal)
case class CommissionCalculator(totalAmount: BigDecimal) {
  private val rate = totalAmount match {
    case value if value <= 1000 =>
      BigDecimal(10)
    case value if value > 1000 && value <= 3000 =>
      BigDecimal(5)
    case value if value > 3000 && value <= 1000000 =>
      BigDecimal(1)
  }
  def calculate: BigDecimal =
    rate / 100 * totalAmount
}
object CommissionCalculator {
  def fromTotalAmount(amount: BigDecimal): CommissionCalculator =
    CommissionCalculator(amount)
}

case class InputValidator(clientId: String, serviceId: String, totalAmount: String) {
  private val validateTotalAmount: Either[NonEmptyList[String], BigDecimal] = for {
    amountConverted <- Either
      .catchNonFatal(BigDecimal(totalAmount))
      .leftMap(_ => NonEmptyList.one(s"totalAmount=$totalAmount, should be an integer"))
    validatedAmount <- amountConverted match {
      case value if value < 0 =>
        Left(
          NonEmptyList.one(
            s"$value is invalid, total amount should be in the range of 0 to a million"
          )
        )
      case value if value > 1000000 =>
        Left(
          NonEmptyList.one(
            s"$value is invalid, total amount should be in the range of 0 to a million"
          )
        )
      case value =>
        Right(value)
    }
  } yield validatedAmount

  private val validateServiceId: Either[NonEmptyList[String], Long] = for {
    serviceIdConverted <- Either
      .catchNonFatal(serviceId.toLong)
      .leftMap(_ => NonEmptyList.one(s"serviceId=$serviceId, should be an integer"))
    validatedServiceId <- Either.cond(
      serviceIdConverted > 0,
      serviceIdConverted,
      NonEmptyList.one(s"serviceId=$serviceIdConverted, should be a positive integer and non zero")
    )
  } yield validatedServiceId

  def validate: Either[NonEmptyList[String], ServiceDetails] =
    (
      validateServiceId,
      validateTotalAmount
    ).parMapN { case (serviceId, totalAmount) =>
      ServiceDetails(clientId, serviceId, totalAmount)
    }

}
