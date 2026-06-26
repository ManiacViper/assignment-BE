package ice.finance.service

import ice.finance.domain.{CommissionCalculated, ServiceRendered}

trait CommissionCalculatorService {

  def getCalculation(services: List[ServiceRendered]): List[CommissionCalculated]

}
