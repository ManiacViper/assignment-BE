package ice.finance.service

import ice.finance.domain.{ServiceCommissionsCalculated, ServiceDetails}

trait CommissionCalculatorService {

  def getCalculation(services: List[ServiceDetails]): List[ServiceCommissionsCalculated]

}
