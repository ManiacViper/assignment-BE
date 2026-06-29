package ice.finance.service

import ice.finance.domain.{InputValidator, ServiceDetails}
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
      InputValidator(clientId, serviceId, totalAmount).validate
  }

}
