package phase10.models

import scala.io.AnsiColor.*
import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers.*

import scala.util.Random

class CardSpec extends AnyWordSpec {
  "A Phase 10 Card" when {
    "random stack" should {
      val cardHand = CardHand.CardHand()
      "have ten cards at generation" in {
        cardHand.cards.size should be(10)
      }
      "have max 3 black cards" in {
        cardHand.cards.count(_.color == Card.Colors.BLACK) should be <= 3
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
        val cardHand = CardHand.CardHand(List(
          Card.Card(Card.Colors.RED, Card.Numbers.ONE),
          Card.Card(Card.Colors.YELLOW, Card.Numbers.TWO),
          Card.Card(Card.Colors.GREEN, Card.Numbers.THREE),
          Card.Card(Card.Colors.BLUE, Card.Numbers.FOUR),
          Card.Card(Card.Colors.RED, Card.Numbers.FIVE),
          Card.Card(Card.Colors.RED, Card.Numbers.SIX),
          Card.Card(Card.Colors.RED, Card.Numbers.SEVEN),
          Card.Card(Card.Colors.RED, Card.Numbers.EIGHT),
          Card.Card(Card.Colors.RED, Card.Numbers.NINE),
          Card.Card(Card.Colors.RED, Card.Numbers.TEN),
          Card.Card(Card.Colors.RED, Card.Numbers.ELEVEN),
          Card.Card(Card.Colors.RED, Card.Numbers.TWELVE),
          Card.Card(Card.Colors.BLACK, Card.Numbers.BLOCK),
          Card.Card(Card.Colors.BLACK, Card.Numbers.JOKER),
        ))
        cardHand.toString should be(s"${RED}1${RESET} | ${YELLOW}2${RESET} | ${GREEN}3${RESET} | ${BLUE}4${RESET} | ${RED}5${RESET} | ${RED}6${RESET} | ${RED}7${RESET} | ${RED}8${RESET} | ${RED}9${RESET} | ${RED}10${RESET} | ${RED}11${RESET} | ${RED}12${RESET} | ${BLACK}B${RESET} | ${BLACK}J${RESET}    (14)")
      }
      "line should look like" in {
        val cardHand = CardHand.CardHand(List(
          Card.Card(Card.Colors.RED, Card.Numbers.ONE),
          Card.Card(Card.Colors.RED, Card.Numbers.SIX),
          Card.Card(Card.Colors.RED, Card.Numbers.SEVEN),
          Card.Card(Card.Colors.RED, Card.Numbers.EIGHT),
          Card.Card(Card.Colors.RED, Card.Numbers.NINE),
          Card.Card(Card.Colors.RED, Card.Numbers.TEN),
          Card.Card(Card.Colors.RED, Card.Numbers.ELEVEN),
          Card.Card(Card.Colors.RED, Card.Numbers.TWELVE),
          Card.Card(Card.Colors.BLACK, Card.Numbers.BLOCK),
          Card.Card(Card.Colors.BLACK, Card.Numbers.JOKER),
        ))
        cardHand.createLine() should be(s" 1 | 2 | 3 | 4 | 5 | 6  | 7  | 8  | 9 | 10 |")
      }
    }
    "is black" should {
      val black = Card.randomBlackCard()
      "number be over 12" in {
        black.number.ordinal should be > 11
      }
    }
    "is colored" should {
      val colors = Card.Colors.values
      val color = colors(Random.nextInt(colors.length))
      val colored = Card.randomColorCard(color)
      "number be between 1 and 12" in {
        colored.number.ordinal should be < 12
      }
    }
    "a card" should {
      val card = Card.Card(Card.Colors.RED, Card.Numbers.ONE)
      "be equal to" in {
        card.toString should be(s"${RED}1${RESET}")
      }
      "has no extra space" in {
        card.extraSpace() should be ("")
      }
      "has extra space" in {
        val card2 = Card.Card(Card.Colors.RED, Card.Numbers.ELEVEN)
        card2.extraSpace() should be (" ")
      }
    }
  }
  "if two random black cards" should {
    "have be two cards" in {
      val blackCards = Card.randomBlackCards(2)
      blackCards.size should be(2)
    }
    "have be black" in {
      val blackCards = Card.randomBlackCards(2)
      blackCards.forall(_.color == Card.Colors.BLACK) should be(true)
    }
  }
}