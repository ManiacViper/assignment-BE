package ice.finance.service

import ice.finance.domain.ServiceDetails
import cats.syntax.either._

trait RowValidatorService {
  def validateRow(
    clientId: String,
    serviceId: String,
    totalAmount: String
  ): Either[String, ServiceDetails]
}

object RowValidatorService {
  def apply() = new RowValidatorService {
    override def validateRow(
      clientId: String,
      serviceId: String,
      totalAmount: String
    ): Either[String, ServiceDetails] =
      (for {
        serviceIdConverted <- Either
          .catchNonFatal(serviceId.toLong)
          .leftMap(_ => s"serviceId=$serviceId, should be an integer")
        validatedServiceId <- checkNegativeNumber(serviceIdConverted, "serviceId")
        amountConverted <- Either
          .catchNonFatal(totalAmount.toInt)
          .leftMap(_ => s"totalAmount=$totalAmount, should be an integer")
        validatedAmount <- checkNegativeNumber(amountConverted, "totalAmount")
        serviceDetails = ServiceDetails(clientId, validatedServiceId, amountConverted)
      } yield serviceDetails)
  }

  private def checkNegativeNumber(value: Long, fieldName: String) =
    Either.cond(value > 0, value, s"$fieldName=$value, should be a positive integer and non zero")
}
