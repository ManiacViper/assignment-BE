package ice.finance.service

import cats.data.NonEmptyList
import ice.finance.domain.{InputValidator, ServiceDetails}

trait InputValidatorService {
  def validateRow(
    clientId: String,
    serviceId: String,
    totalAmount: String
  ): Either[NonEmptyList[String], ServiceDetails]
}

object InputValidatorService {
  def apply() = new InputValidatorService {
    override def validateRow(
      clientId: String,
      serviceId: String,
      totalAmount: String
    ): Either[NonEmptyList[String], ServiceDetails] =
      InputValidator(clientId, serviceId, totalAmount).validate
  }

}
