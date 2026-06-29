package ice.finance.service

import ice.finance.domain.{CommissionCalculator, ServiceCommissionCalculated, ServiceDetails}
import ice.finance.domain.BigDecimalConfig._
trait CommissionCalculatorService {
  def getCalculation(services: ServiceDetails): ServiceCommissionCalculated
}

object CommissionCalculatorService {
  def apply() = new CommissionCalculatorService {
    override def getCalculation(
      service: ServiceDetails
    ): ServiceCommissionCalculated =
      ServiceCommissionCalculated(
        service.clientId,
        service.id,
        CommissionCalculator
          .fromTotalAmount(service.totalAmount)
          .calculate
      )
  }
}
