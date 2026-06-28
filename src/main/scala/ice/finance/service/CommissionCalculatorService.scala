package ice.finance.service

import ice.finance.domain.{CommissionCalculator, ServiceCommissionCalculated, ServiceDetails}

trait CommissionCalculatorService {
  def getCalculation(services: ServiceDetails): Either[String, ServiceCommissionCalculated]
}

object CommissionCalculatorService {
  def apply() = new CommissionCalculatorService {
    override def getCalculation(
      service: ServiceDetails
    ): Either[String, ServiceCommissionCalculated] =
      CommissionCalculator
        .fromTotalAmount(service.totalAmount)
        .map { commissionCalculator =>
          ServiceCommissionCalculated(
            service.clientId,
            service.id,
            commissionCalculator.calculate
          )
        }
  }
}
