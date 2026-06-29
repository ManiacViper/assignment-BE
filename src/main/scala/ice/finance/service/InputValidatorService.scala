package ice.finance.service

import ice.finance.domain.ServiceDetails
import cats.syntax.either._

trait InputValidatorService {
  def validateRow(
    clientId: String,
    serviceId: String,
    totalAmount: String
  ): Either[String, ServiceDetails]
}

object InputValidatorService {
  def apply() = new InputValidatorService {
    override def validateRow(
      clientId: String,
      serviceId: String,
      totalAmount: String
    ): Either[String, ServiceDetails] =
      (for {
        serviceIdConverted <- Either
          .catchNonFatal(serviceId.toLong)
          .leftMap(_ => s"serviceId=$serviceId, should be an integer")
        validatedServiceId <- Either.cond(
          serviceIdConverted > 0,
          serviceIdConverted,
          s"serviceId=$serviceIdConverted, should be a positive integer and non zero"
        )
        amountConverted <- Either
          .catchNonFatal(BigDecimal(totalAmount))
          .leftMap(_ => s"totalAmount=$totalAmount, should be an integer")
        _ <- checkRangeOfTotalAmount(amountConverted)
        serviceDetails = ServiceDetails(clientId, validatedServiceId, amountConverted)
      } yield serviceDetails)
  }
  private def checkRangeOfTotalAmount(totalAmount: BigDecimal) =
    totalAmount match {
      case value if value < 0 =>
        Left(s"$value is invalid, amount should be in the range of 0 to a million")
      case value if value > 1000000 =>
        Left(s"$value is invalid, amount should be in the range of 0 to a million")
      case _ =>
        Right(totalAmount)
    }

}
