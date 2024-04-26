package phase10.models

import scala.io.AnsiColor.*
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
    "Hand" should {
      "look like" in {
        val cardHand = CardHand(List(
          Card(Colors.RED, Numbers.ONE),
          Card(Colors.YELLOW, Numbers.TWO),
          Card(Colors.GREEN, Numbers.THREE),
          Card(Colors.BLUE, Numbers.FOUR),
          Card(Colors.RED, Numbers.FIVE),
          Card(Colors.RED, Numbers.SIX),
          Card(Colors.RED, Numbers.SEVEN),
          Card(Colors.RED, Numbers.EIGHT),
          Card(Colors.RED, Numbers.NINE),
          Card(Colors.RED, Numbers.TEN),
          Card(Colors.RED, Numbers.ELEVEN),
          Card(Colors.RED, Numbers.TWELVE),
          Card(Colors.BLACK, Numbers.BLOCK),
          Card(Colors.BLACK, Numbers.JOKER),
        ))
        cardHand.toString should be(s"${RED}1${RESET} | ${YELLOW}2${RESET} | ${GREEN}3${RESET} | ${BLUE}4${RESET} | ${RED}5${RESET} | ${RED}6${RESET} | ${RED}7${RESET} | ${RED}8${RESET} | ${RED}9${RESET} | ${RED}10${RESET} | ${RED}11${RESET} | ${RED}12${RESET} | ${BLACK}B${RESET} | ${BLACK}J${RESET}    (14)")
      }
      "line should look like" in {
        val cardHand = CardHand(List(
          Card(Colors.RED, Numbers.ONE),
          Card(Colors.RED, Numbers.SIX),
          Card(Colors.RED, Numbers.SEVEN),
          Card(Colors.RED, Numbers.EIGHT),
          Card(Colors.RED, Numbers.NINE),
          Card(Colors.RED, Numbers.TEN),
          Card(Colors.RED, Numbers.ELEVEN),
          Card(Colors.RED, Numbers.TWELVE),
          Card(Colors.BLACK, Numbers.BLOCK),
          Card(Colors.BLACK, Numbers.JOKER),
        ))
        cardHand.createLine() should be(s" 1 | 2 | 3 | 4 | 5 | 6  | 7  | 8  | 9 | 10 |")
      }
    }
    "is black" should {
      val black = randomBlackCard()
      "number be over 12" in {
        black.number.ordinal should be > 11
      }
    }
    "is colored" should {
      val colors = Colors.values
      val color = colors(Random.nextInt(colors.length))
      val colored = randomColorCard(color)
      "number be between 1 and 12" in {
        colored.number.ordinal should be < 12
      }
    }
    "a card" should {
      val card = Card(Colors.RED, Numbers.ONE)
      "be equal to" in {
        card.toString should be(s"${RED}1${RESET}")
      }
      "has no extra space" in {
        card.extraSpace() should be ("")
      }
      "has extra space" in {
        val card2 = Card(Colors.RED, Numbers.ELEVEN)
        card2.extraSpace() should be (" ")
      }
    }
  }
  "if two random black cards" should {
    "have be two cards" in {
      val blackCards = randomBlackCards(2)
      blackCards.size should be(2)
    }
    "have be black" in {
      val blackCards = randomBlackCards(2)
      blackCards.forall(_.color == Colors.BLACK) should be(true)
    }
  }
}