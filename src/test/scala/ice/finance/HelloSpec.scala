package ice.finance

import weaver._
import cats.syntax.all._

object HelloSpec extends FunSuite {
  test("it should greet") {
    expect("Hello world" === Greeter.greet)
  }
}
