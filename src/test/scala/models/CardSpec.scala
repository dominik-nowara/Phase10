package phase10.models

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers.*

import scala.util.Random

class CardSpec extends AnyWordSpec {
  "A Phase 10 Card" when {
    "random stack" should {
      val cardHand = CardHand()
      "have ten cards at generation" in {
        cardHand.cards.size should be(10)
      }
      "have max 3 black cards" in {
        cardHand.cards.count(_.color == Colors.BLACK) should be <= 3
      }
      "have 9 cards if one card is removed" in {
        cardHand.removeCard(0).cards.size should be(9)
      }
      "have 10 cards if one is changed" in {
        cardHand.changeCard(0).cards.size should be(10)
      }
    }
    "is black" should {
      val black = randomBlackCard()
      "be over 10" in {
        black.number.ordinal should be > 11
      }
    }
    "is colored" should {
      val colors = Colors.values
      val color = colors(Random.nextInt(colors.length))
      val colored = randomColorCard(color)
      "be number between 0 and 12" in {
        colored.number.ordinal should be < 12
      }
    }
  }
}