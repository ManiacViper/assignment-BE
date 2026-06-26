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
  def fromServiceRendered(serviceRendered: ServiceRendered): Either[String, CommissionCharged] =
    serviceRendered.totalAmount match {
      case value if value <= 1000 =>
        Right(EqualOrUnderThousand(value))
      case value if value > 1000 && value <= 3000 =>
        Right(EqualOrUnderThreeThousand(value))
      case value if value > 3000 && value <= 1000000 =>
        Right(AboveThreeThousand(value))
      case _ =>
        Left("amount is invalid")
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