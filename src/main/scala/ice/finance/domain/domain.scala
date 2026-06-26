package ice.finance.domain
case class ServiceRendered(
                          id: Long,
                          totalAmount: Int)
case class CommissionCalculated(id: Long, amount: Int)
sealed trait CommissionCharged {
  val rate: Int
  val amount: Int
  def calculate: BigDecimal =
    BigDecimal(rate) / BigDecimal(100) * BigDecimal(amount)
}

object CommissionCharged {
  def fromTotalAmount(amount: Int): Either[String, CommissionCharged] =
    amount match {
      case value if value < 0 =>
        Left(s"$amount is invalid, amount should be in the range of 0 to a million")
      case value if value > 1000000 =>
        Left(s"$amount is invalid, amount should be in the range of 0 to a million")
      case value if value <= 1000 =>
        Right(EqualOrUnderThousand(value))
      case value if value > 1000 && value <= 3000 =>
        Right(EqualOrUnderThreeThousand(value))
      case value if value > 3000 && value <= 1000000 =>
        Right(AboveThreeThousand(value))
    }
}

case class EqualOrUnderThousand(amount: Int) extends CommissionCharged {
  val rate = 10
}

case class EqualOrUnderThreeThousand(amount: Int) extends CommissionCharged {
  val rate = 5
}

case class AboveThreeThousand(amount: Int) extends CommissionCharged {
  val rate = 1
}