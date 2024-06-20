package phase10.controller

import org.scalatest.matchers.should.Matchers._
import org.scalatest.wordspec.AnyWordSpec
import phase10.models.*
import phase10.models.CardComponent.GameCard
import phase10.models.PhaseComponent.GamePhase
import phase10.models.PlayerComponent.Player

class CommandSpec extends AnyWordSpec {
  "A Command" when {
    "is play command" should {
      "give back the same result on no step" in {
        val cards = List(
          GameCard(Card.Colors.RED, Card.Numbers.ONE),
          GameCard(Card.Colors.BLUE, Card.Numbers.ONE),
          GameCard(Card.Colors.RED, Card.Numbers.TWO),
          GameCard(Card.Colors.BLUE, Card.Numbers.THREE),
          GameCard(Card.Colors.BLUE, Card.Numbers.FOUR),
          GameCard(Card.Colors.BLUE, Card.Numbers.FIVE),
          GameCard(Card.Colors.GREEN, Card.Numbers.SIX),
          GameCard(Card.Colors.GREEN, Card.Numbers.SEVEN),
          GameCard(Card.Colors.YELLOW, Card.Numbers.EIGHT),
          GameCard(Card.Colors.YELLOW, Card.Numbers.NINE)
        )
        val phases = GamePhase(List(Phase.PhaseTypes.DOUBLE))
        val players = List(Player("Player 1", cards, phases))

        val command = PlayCommand(players, 0, 1)
        val stepResult = command.noStep(players)
        stepResult should be (players)
      }
    }
    "is swap command" should {
      "give back the same result on no step" in {
        val cards = List(
          GameCard(Card.Colors.RED, Card.Numbers.ONE),
          GameCard(Card.Colors.BLUE, Card.Numbers.ONE),
          GameCard(Card.Colors.RED, Card.Numbers.TWO),
          GameCard(Card.Colors.BLUE, Card.Numbers.THREE),
          GameCard(Card.Colors.BLUE, Card.Numbers.FOUR),
          GameCard(Card.Colors.BLUE, Card.Numbers.FIVE),
          GameCard(Card.Colors.GREEN, Card.Numbers.SIX),
          GameCard(Card.Colors.GREEN, Card.Numbers.SEVEN),
          GameCard(Card.Colors.YELLOW, Card.Numbers.EIGHT),
          GameCard(Card.Colors.YELLOW, Card.Numbers.NINE)
        )
        val phases = GamePhase(List(Phase.PhaseTypes.DOUBLE))
        val players = List(Player("Player 1", cards, phases))

        val command = SwapCommand(players, 0, 1)
        val stepResult = command.noStep(players)
        stepResult should be(players)
      }
    }
  }
}
