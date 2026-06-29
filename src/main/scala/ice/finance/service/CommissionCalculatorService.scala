package ice.finance.service

import ice.finance.domain.{CommissionCalculator, ServiceCommissionCalculated, ServiceDetails}
trait CommissionCalculatorService {
  def getCalculation(services: ServiceDetails): ServiceCommissionCalculated
}

object CommissionCalculatorService {
  def apply(): CommissionCalculatorService = new CommissionCalculatorService {
    override def getCalculation(
      service: ServiceDetails
    ): ServiceCommissionCalculated =
      ServiceCommissionCalculated(
        service.id,
        CommissionCalculator
          .fromTotalAmount(service.totalAmount)
          .calculate
          .bigDecimal
      )
  }
}
