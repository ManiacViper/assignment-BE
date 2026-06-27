package ice.finance.service

import ice.finance.domain.ServiceDetails

trait RawDataTransformerService {
  def convertFrom(services: List[String]): List[ServiceDetails]
}

object RawDataTransformerService {
}
