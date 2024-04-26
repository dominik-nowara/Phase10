package phase10.models

import scala.io.AnsiColor.*
import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers.*

class PlayerSpec extends AnyWordSpec {
  "Player" should {
    "be output right" in {
      val cardHand = CardHand(List(
        Card(Colors.RED, Numbers.ONE)
      ))
      val player = Player("Player 1", cardHand, randomPhases(), false)
      player.name should be("Player 1")
      player.cardHand.cards.length should be (1)
      player.toString should be (s"Player 1: ${RED}1${RESET}    (1)")
    }
  }
}
