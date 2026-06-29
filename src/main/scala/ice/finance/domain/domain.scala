package ice.finance.domain

import scala.language.implicitConversions
import scala.math.BigDecimal.RoundingMode.HALF_EVEN

object BigDecimalConfig {
  implicit def bigDecimal(bigDecimal: BigDecimal): BigDecimal = bigDecimal.setScale(16, HALF_EVEN)
}

case class ServiceDetails(clientId: String, id: Long, totalAmount: BigDecimal)
case class ServiceCommissionCalculated(clientId: String, id: Long, commissionAmount: BigDecimal)
case class CommissionCalculator(totalAmount: BigDecimal) {
  private val rate = totalAmount match {
    case value if value <= 1000 =>
      BigDecimal(10)
    case value if value > 1000 && value <= 3000 =>
      BigDecimal(5)
    case value if value > 3000 && value <= 1000000 =>
      BigDecimal(1)
  }
  def calculate: BigDecimal =
    rate / 100 * totalAmount
}
object CommissionCalculator {
  def fromTotalAmount(amount: BigDecimal): CommissionCalculator =
    CommissionCalculator(amount)
}
