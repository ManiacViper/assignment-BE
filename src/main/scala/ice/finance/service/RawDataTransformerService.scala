package ice.finance.service

import ice.finance.domain.ServiceDetails

import scala.util.Try

trait RawDataTransformerService {
  def convertFrom(rawServiceDetails: String): Either[String, ServiceDetails]
}

object RawDataTransformerService {
  def apply() = new RawDataTransformerService {

    private def basicTransformResult(serviceId: String, amount: String) = for {
      serviceId <- Try(serviceId.toLong)
      amount    <- Try(amount.toInt)
      serviceDetails = ServiceDetails(serviceId, amount)
    } yield serviceDetails

    override def convertFrom(rawServiceDetails: String): Either[String, ServiceDetails] =
      rawServiceDetails.split(",").toList match {
        case List(clientId, serviceId, amount) =>
          basicTransformResult(serviceId, amount)
            .fold(
              error => Left(s"${error.getMessage} for serviceId=${serviceId}"),
              details => Right(details)
            )
        case _ =>
          Left("3 columns not found")
      }
  }
}
