package ice.finance.domain
//TODO: totalAmount to be BigDecimal
case class ServiceDetails(clientId: String, id: Long, totalAmount: Int)
case class ServiceCommissionCalculated(clientId: String, id: Long, commissionAmount: BigDecimal)
case class CommissionCalculator(totalAmount: Int) {
  private val rate = totalAmount match {
    case value if value <= 1000 =>
      10
    case value if value > 1000 && value <= 3000 =>
      5
    case value if value > 3000 && value <= 1000000 =>
      1
  }
  def calculate: BigDecimal =
    BigDecimal(rate) / BigDecimal(100) * BigDecimal(totalAmount)
}
object CommissionCalculator {
  def fromTotalAmount(amount: Int): Either[String, CommissionCalculator] =
    amount match {
      case value if value < 0 =>
        Left(s"$amount is invalid, amount should be in the range of 0 to a million")
      case value if value > 1000000 =>
        Left(s"$amount is invalid, amount should be in the range of 0 to a million")
      case value =>
        Right(CommissionCalculator(value))
    }
}
