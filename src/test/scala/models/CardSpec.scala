package phase10.models

import scala.io.AnsiColor.*
import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers.*
import scala.util.Random

import phase10.util.GameFactories
import phase10.models.Card
import phase10.models.CardComponent.GameCard

class CardSpec extends AnyWordSpec {
  "A Phase 10 Card" when {
    "random stack" should {
      val cards = GameFactories.generateStack("Player 1", 10, List(0, 1, 2, 3, 4, 5, 6, 7, 8, 9))
      "have ten cards at generation" in {
        cards.size should be(10)
      }
      "have max 3 black cards" in {
        cards.count(_.color == Card.Colors.BLACK) should be <= 3
      }
    }
    "a card" should {
      val card = GameCard(Card.Colors.RED, Card.Numbers.ONE)
      "be equal to" in {
        card.toString should be(s"${RED_B}${BLACK} 1 ${RESET}")
      }
      "has no extra space" in {
        card.extraSpace() should be ("")
      }
      "has extra space" in {
        val card2 = GameCard(Card.Colors.RED, Card.Numbers.ELEVEN)
        card2.extraSpace() should be (" ")
      }
      "print correctly" in {
        val card1 = GameCard(Card.Colors.RED, Card.Numbers.ONE)
        val card2 = GameCard(Card.Colors.BLUE, Card.Numbers.TWO)
        val card3 = GameCard(Card.Colors.GREEN, Card.Numbers.THREE)
        val card4 = GameCard(Card.Colors.YELLOW, Card.Numbers.FOUR)
        val card5 = GameCard(Card.Colors.RED, Card.Numbers.FIVE)
        val card6 = GameCard(Card.Colors.RED, Card.Numbers.SIX)
        val card7 = GameCard(Card.Colors.RED, Card.Numbers.SEVEN)
        val card8 = GameCard(Card.Colors.RED, Card.Numbers.EIGHT)
        val card9 = GameCard(Card.Colors.RED, Card.Numbers.NINE)
        val card10 = GameCard(Card.Colors.RED, Card.Numbers.TEN)
        val card11 = GameCard(Card.Colors.RED, Card.Numbers.ELEVEN)
        val card12 = GameCard(Card.Colors.RED, Card.Numbers.TWELVE)
        val jokerCard = GameCard(Card.Colors.BLACK, Card.Numbers.JOKER)
        val blockCard = GameCard(Card.Colors.BLACK, Card.Numbers.BLOCK)

        card1.toString should be(s"${RED_B}${BLACK} 1 ${RESET}")
        card2.toString should be(s"${BLUE_B}${BLACK} 2 ${RESET}")
        card3.toString should be(s"${GREEN_B}${BLACK} 3 ${RESET}")
        card4.toString should be(s"${YELLOW_B}${BLACK} 4 ${RESET}")
        card5.toString should be(s"${RED_B}${BLACK} 5 ${RESET}")
        card6.toString should be(s"${RED_B}${BLACK} 6 ${RESET}")
        card7.toString should be(s"${RED_B}${BLACK} 7 ${RESET}")
        card8.toString should be(s"${RED_B}${BLACK} 8 ${RESET}")
        card9.toString should be(s"${RED_B}${BLACK} 9 ${RESET}")
        card10.toString should be(s"${RED_B}${BLACK} 10 ${RESET}")
        card11.toString should be(s"${RED_B}${BLACK} 11 ${RESET}")
        card12.toString should be(s"${RED_B}${BLACK} 12 ${RESET}")
        jokerCard.toString should be(s"${BLACK_B} J ${RESET}")
        blockCard.toString should be(s"${BLACK_B} B ${RESET}")
      }
    }
  }
}