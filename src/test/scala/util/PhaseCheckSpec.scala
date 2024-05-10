package phase10.util

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers.*
import phase10.models._
import phase10.util._

class PhaseCheckSpec extends AnyWordSpec {
  "A phase check" when {
    "double" should {
      val cardHand = CardHand.CardHand(List(
        Card.Card(Card.Colors.RED, Card.Numbers.ONE),
        Card.Card(Card.Colors.RED, Card.Numbers.ONE)
      ))
      val phase = Phase.Phase(List(Phase.PhaseTypes.DOUBLE))
      val player = Player("Player 1", cardHand, phase, false)
      "have a double" in {
        PhaseCheck.checkPhases(player) should be (true)
      }
    }
    "triple" should {
      val cardHand = CardHand.CardHand(List(
        Card.Card(Card.Colors.RED, Card.Numbers.ONE),
        Card.Card(Card.Colors.RED, Card.Numbers.ONE),
        Card.Card(Card.Colors.RED, Card.Numbers.ONE)
      ))
      val phase = Phase.Phase(List(Phase.PhaseTypes.TRIPLE))
      val player = Player("Player 1", cardHand, phase, false)
      "have a triple" in {
        PhaseCheck.checkPhases(player) should be(true)
      }
    }

    "quadrupel" should {
      val cardHand = CardHand.CardHand(List(
        Card.Card(Card.Colors.RED, Card.Numbers.ONE),
        Card.Card(Card.Colors.RED, Card.Numbers.ONE),
        Card.Card(Card.Colors.RED, Card.Numbers.ONE),
        Card.Card(Card.Colors.RED, Card.Numbers.ONE),
      ))
      val phase = Phase.Phase(List(Phase.PhaseTypes.QUADRUPLE))
      val player = Player("Player 1", cardHand, phase, false)
      "have a quadrupel" in {
        PhaseCheck.checkPhases(player) should be(true)
      }
    }

    "quintupel" should {
      val cardHand = CardHand.CardHand(List(
        Card.Card(Card.Colors.RED, Card.Numbers.ONE),
        Card.Card(Card.Colors.RED, Card.Numbers.ONE),
        Card.Card(Card.Colors.RED, Card.Numbers.ONE),
        Card.Card(Card.Colors.RED, Card.Numbers.ONE),
        Card.Card(Card.Colors.RED, Card.Numbers.ONE),
      ))
      val phase = Phase.Phase(List(Phase.PhaseTypes.QUINTUPLE))
      val player = Player("Player 1", cardHand, phase, false)
      "have a quintupel" in {
        PhaseCheck.checkPhases(player) should be(true)
      }
    }

    "fourrow" should {
      val cardHand = CardHand.CardHand(List(
        Card.Card(Card.Colors.RED, Card.Numbers.ONE),
        Card.Card(Card.Colors.RED, Card.Numbers.TWO),
        Card.Card(Card.Colors.RED, Card.Numbers.THREE),
        Card.Card(Card.Colors.RED, Card.Numbers.FOUR),
        Card.Card(Card.Colors.RED, Card.Numbers.ONE),
      ))
      val phase = Phase.Phase(List(Phase.PhaseTypes.FOURROW))
      val player = Player("Player 1", cardHand, phase, false)
      "have a fourrow" in {
        PhaseCheck.checkPhases(player) should be(true)
      }
    }

    "sevenrow" should {
      val cardHand = CardHand.CardHand(List(
        Card.Card(Card.Colors.RED, Card.Numbers.ONE),
        Card.Card(Card.Colors.RED, Card.Numbers.TWO),
        Card.Card(Card.Colors.RED, Card.Numbers.THREE),
        Card.Card(Card.Colors.RED, Card.Numbers.FOUR),
        Card.Card(Card.Colors.RED, Card.Numbers.FIVE),
        Card.Card(Card.Colors.RED, Card.Numbers.SIX),
        Card.Card(Card.Colors.RED, Card.Numbers.SEVEN),
      ))
      val phase = Phase.Phase(List(Phase.PhaseTypes.SEVENROW))
      val player = Player("Player 1", cardHand, phase, false)
      "have a sevenrow" in {
        PhaseCheck.checkPhases(player) should be(true)
      }
    }

    "eightrow" should {
      val cardHand = CardHand.CardHand(List(
        Card.Card(Card.Colors.RED, Card.Numbers.ONE),
        Card.Card(Card.Colors.RED, Card.Numbers.TWO),
        Card.Card(Card.Colors.RED, Card.Numbers.THREE),
        Card.Card(Card.Colors.RED, Card.Numbers.FOUR),
        Card.Card(Card.Colors.RED, Card.Numbers.FIVE),
        Card.Card(Card.Colors.RED, Card.Numbers.SIX),
        Card.Card(Card.Colors.RED, Card.Numbers.SEVEN),
        Card.Card(Card.Colors.RED, Card.Numbers.EIGHT)
      ))
      val phase = Phase.Phase(List(Phase.PhaseTypes.EIGHTROW))
      val player = Player("Player 1", cardHand, phase, false)
      "have a eightrow" in {
        PhaseCheck.checkPhases(player) should be(true)
      }
    }

    "ninerow" should {
      val cardHand = CardHand.CardHand(List(
        Card.Card(Card.Colors.RED, Card.Numbers.ONE),
        Card.Card(Card.Colors.RED, Card.Numbers.TWO),
        Card.Card(Card.Colors.RED, Card.Numbers.THREE),
        Card.Card(Card.Colors.RED, Card.Numbers.FOUR),
        Card.Card(Card.Colors.RED, Card.Numbers.FIVE),
        Card.Card(Card.Colors.RED, Card.Numbers.SIX),
        Card.Card(Card.Colors.RED, Card.Numbers.SEVEN),
        Card.Card(Card.Colors.RED, Card.Numbers.EIGHT),
        Card.Card(Card.Colors.RED, Card.Numbers.NINE)
      ))
      val phase = Phase.Phase(List(Phase.PhaseTypes.NINEROW))
      val player = Player("Player 1", cardHand, phase, false)
      "have a ninerow" in {
        PhaseCheck.checkPhases(player) should be(true)
      }
    }

    "color" should {
      val cardHand = CardHand.CardHand(List(
        Card.Card(Card.Colors.RED, Card.Numbers.ONE),
        Card.Card(Card.Colors.RED, Card.Numbers.TWO),
        Card.Card(Card.Colors.RED, Card.Numbers.THREE),
        Card.Card(Card.Colors.RED, Card.Numbers.FOUR),
        Card.Card(Card.Colors.RED, Card.Numbers.FIVE),
        Card.Card(Card.Colors.RED, Card.Numbers.SIX),
        Card.Card(Card.Colors.RED, Card.Numbers.SEVEN),
        Card.Card(Card.Colors.RED, Card.Numbers.EIGHT)
      ))
      val phase = Phase.Phase(List(Phase.PhaseTypes.COLOR))
      val player = Player("Player 1", cardHand, phase, false)
      "have a quintupel" in {
        PhaseCheck.checkPhases(player) should be(true)
      }
    }

    "double double" should {
      val cardHand = CardHand.CardHand(List(
        Card.Card(Card.Colors.RED, Card.Numbers.ONE),
        Card.Card(Card.Colors.RED, Card.Numbers.ONE),
        Card.Card(Card.Colors.RED, Card.Numbers.TWO),
        Card.Card(Card.Colors.RED, Card.Numbers.TWO),
      ))
      val phase = Phase.Phase(List(Phase.PhaseTypes.DOUBLE, Phase.PhaseTypes.DOUBLE))
      val player = Player("Player 1", cardHand, phase, false)
      "have two doubles" in {
        PhaseCheck.checkPhases(player) should be(true)
      }
    }

    "double triple" should {
      val cardHand = CardHand.CardHand(List(
        Card.Card(Card.Colors.RED, Card.Numbers.ONE),
        Card.Card(Card.Colors.RED, Card.Numbers.ONE),
        Card.Card(Card.Colors.RED, Card.Numbers.TWO),
        Card.Card(Card.Colors.RED, Card.Numbers.TWO),
        Card.Card(Card.Colors.RED, Card.Numbers.TWO),
      ))
      val phase = Phase.Phase(List(Phase.PhaseTypes.DOUBLE, Phase.PhaseTypes.TRIPLE))
      val player = Player("Player 1", cardHand, phase, false)
      "have a double and a triple" in {
        PhaseCheck.checkPhases(player) should be(true)
      }
    }

    "double quadruple" should {
      val cardHand = CardHand.CardHand(List(
        Card.Card(Card.Colors.RED, Card.Numbers.ONE),
        Card.Card(Card.Colors.RED, Card.Numbers.ONE),
        Card.Card(Card.Colors.RED, Card.Numbers.TWO),
        Card.Card(Card.Colors.RED, Card.Numbers.TWO),
        Card.Card(Card.Colors.RED, Card.Numbers.TWO),
        Card.Card(Card.Colors.RED, Card.Numbers.TWO),
      ))
      val phase = Phase.Phase(List(Phase.PhaseTypes.DOUBLE, Phase.PhaseTypes.QUADRUPLE))
      val player = Player("Player 1", cardHand, phase, false)
      "have a double and a quadruple" in {
        PhaseCheck.checkPhases(player) should be(true)
      }
    }

    "double quintuple" should {
      val cardHand = CardHand.CardHand(List(
        Card.Card(Card.Colors.RED, Card.Numbers.ONE),
        Card.Card(Card.Colors.RED, Card.Numbers.ONE),
        Card.Card(Card.Colors.RED, Card.Numbers.TWO),
        Card.Card(Card.Colors.RED, Card.Numbers.TWO),
        Card.Card(Card.Colors.RED, Card.Numbers.TWO),
        Card.Card(Card.Colors.RED, Card.Numbers.TWO),
        Card.Card(Card.Colors.RED, Card.Numbers.TWO),
      ))
      val phase = Phase.Phase(List(Phase.PhaseTypes.DOUBLE, Phase.PhaseTypes.QUINTUPLE))
      val player = Player("Player 1", cardHand, phase, false)
      "have a double and a quintuple" in {
        PhaseCheck.checkPhases(player) should be(true)
      }
    }

    "fail on invalid second phase" should {
      val cardHand = CardHand.CardHand(List(
        Card.Card(Card.Colors.RED, Card.Numbers.ONE),
        Card.Card(Card.Colors.RED, Card.Numbers.ONE),
        Card.Card(Card.Colors.RED, Card.Numbers.TWO),
        Card.Card(Card.Colors.RED, Card.Numbers.TWO),
        Card.Card(Card.Colors.RED, Card.Numbers.TWO),
        Card.Card(Card.Colors.RED, Card.Numbers.TWO),
        Card.Card(Card.Colors.RED, Card.Numbers.TWO),
      ))
      val phase = Phase.Phase(List(Phase.PhaseTypes.DOUBLE, Phase.PhaseTypes.SEVENROW))
      val player = Player("Player 1", cardHand, phase, false)
      "fail" in {
        PhaseCheck.checkPhases(player) should be(false)
      }
    }
  }
}
