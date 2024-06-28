package phase10.models

import scala.io.AnsiColor.*
import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers.*
import phase10.controller.GameControllerImpl.GameManager
import phase10.models.CardComponent.GameCardImpl.GameCard
import phase10.models.PhaseComponent.GamePhaseImpl.GamePhase
import phase10.models.PlayerComponent.{IPlayer, PlayerImpl}
import phase10.models.PlayerComponent.PlayerImpl.Player
import phase10.util.GameFactories
import play.api.libs.json.Json

import scala.util.{Failure, Success}

class PlayerSpec extends AnyWordSpec {
  "Player" should {
    "PlayerFactory should create player correctly" in {
      val player = GameFactories.createPlayer("Player 1")
      player.name should be("Player 1")
      player.cards.length should be (10)
    }
    "be output right" in {
      val cards = List(
        GameCard(Card.Colors.RED, Card.Numbers.ONE)
      )
      val player = PlayerImpl.Player("Player 1", cards, GameFactories.generatePhases("Player 1", 1))
      player.name should be("Player 1")
      player.cards.length should be (1)
      player.toString should be (s"Player 1: ${RED_B}${BLACK} 1 ${RESET}")
    }
    "create line correctly" in {
      val cards = List(GameCard(Card.Colors.RED, Card.Numbers.ONE), GameCard(Card.Colors.RED, Card.Numbers.TWO))
      val player = PlayerImpl.Player("Player 1", cards, GameFactories.generatePhases("Player 1", 1))
      player.createLine() should be ("  1  |  2  |")
    }
    "card exchange should" in {
      val cards = List(GameCard(Card.Colors.RED, Card.Numbers.ONE), GameCard(Card.Colors.RED, Card.Numbers.TWO))
      val player = PlayerImpl.Player("Player 1", cards, GameFactories.generatePhases("Player 1", 1))
      player.doExchange(0, 2)

      player.cards.length should be (2)
      GameManager.stack.get.length should be (2)
    }
    "card undo exchange should" in {
      GameManager.stack = Some(List(
        GameCard(Card.Colors.BLUE, Card.Numbers.THREE),
        GameCard(Card.Colors.YELLOW, Card.Numbers.ONE)
      ))
      val cards = List(GameCard(Card.Colors.RED, Card.Numbers.ONE), GameCard(Card.Colors.RED, Card.Numbers.TWO))
      val player = PlayerImpl.Player("Player 1", cards, GameFactories.generatePhases("Player 1", 1))

      val newPlayerFirst = player.undoExchange(0, 2)
      newPlayerFirst.cards.length should be (2)
      GameManager.stack.get.length should be (1)

      val newPlayer = player.undoExchange(0, 2)
      newPlayer.cards.length should be (2)
      GameManager.stack.isDefined should be (false)
      newPlayer.cards.head should be (GameCard(Card.Colors.BLUE, Card.Numbers.THREE))
    }
    "swap from stack should" in {
      GameManager.stack = Some(List(GameCard(Card.Colors.RED, Card.Numbers.THREE)))
      val cards = List(GameCard(Card.Colors.RED, Card.Numbers.ONE), GameCard(Card.Colors.RED, Card.Numbers.TWO))
      val player = PlayerImpl.Player("Player 1", cards, GameFactories.generatePhases("Player 1", 1))
      player.swapFromStack(1)

      player.cards.length should be (2)
      GameManager.stack.get.length should be (1)
      GameManager.stack.get.head should be (GameCard(Card.Colors.RED, Card.Numbers.TWO))
    }
    "next player add amount from current count" in {
      val cards = List(GameCard(Card.Colors.RED, Card.Numbers.ONE), GameCard(Card.Colors.RED, Card.Numbers.TWO))
      val player = PlayerImpl.Player("Player 1", cards, GameFactories.generatePhases("Player 1", 1))
      val card = GameCard(Card.Colors.RED, Card.Numbers.THREE)

      GameManager.current = 0

      GameManager.current should be (0)
      player.nextPlayer(card, 4)
      GameManager.current should be (1)

      val blockCard = GameCard(Card.Colors.BLACK, Card.Numbers.BLOCK)
      player.nextPlayer(blockCard, 4)
      GameManager.current should be(3)

      player.nextPlayer(blockCard, 4)
      GameManager.current should be(1)
    }
    "do swap from stack should" in {
      val cards = List(GameCard(Card.Colors.RED, Card.Numbers.ONE), GameCard(Card.Colors.RED, Card.Numbers.TWO))
      val player = PlayerImpl.Player("Player 1", cards, GameFactories.generatePhases("Player 1", 1))
      val card = GameCard(Card.Colors.RED, Card.Numbers.THREE)

      GameManager.stack = Some(List(GameCard(Card.Colors.RED, Card.Numbers.THREE)))
      GameManager.current = 0

      GameManager.current should be(0)
      val newPlayer = player.doStackSwap(0, 4)
      GameManager.current should be(1)

      player.cards.length should be(2)
      GameManager.stack.get.length should be(1)
      GameManager.stack.get.head should be(GameCard(Card.Colors.RED, Card.Numbers.ONE))
      newPlayer.cards.head should be(GameCard(Card.Colors.RED, Card.Numbers.THREE))
    }
    "undo swap from stack should" in {
      val cards = List(GameCard(Card.Colors.RED, Card.Numbers.ONE), GameCard(Card.Colors.RED, Card.Numbers.TWO))
      val player = PlayerImpl.Player("Player 1", cards, GameFactories.generatePhases("Player 1", 1))
      val card = GameCard(Card.Colors.RED, Card.Numbers.THREE)

      GameManager.stack = Some(List(GameCard(Card.Colors.RED, Card.Numbers.THREE)))
      GameManager.current = 0

      GameManager.current should be(0)
      val newPlayer = player.undoStackSwap(0, 4)
      GameManager.current should be(3)

      player.cards.length should be(2)
      GameManager.stack.get.length should be(1)
      GameManager.stack.get.head should be(GameCard(Card.Colors.RED, Card.Numbers.ONE))
      newPlayer.cards.head should be(GameCard(Card.Colors.RED, Card.Numbers.THREE))

      GameManager.stack = Some(List(GameCard(Card.Colors.BLACK, Card.Numbers.BLOCK)))
      GameManager.current should be(3)
      player.previousPlayer(4)
      GameManager.current should be(1)
    }
    "check phase should do on one phase" in {
      val cards = List(
        GameCard(Card.Colors.RED, Card.Numbers.ONE),
        GameCard(Card.Colors.RED, Card.Numbers.ONE),
        GameCard(Card.Colors.RED, Card.Numbers.THREE),
        GameCard(Card.Colors.RED, Card.Numbers.FOUR),
        GameCard(Card.Colors.RED, Card.Numbers.FIVE),
        GameCard(Card.Colors.RED, Card.Numbers.SIX),
        GameCard(Card.Colors.RED, Card.Numbers.SEVEN),
        GameCard(Card.Colors.RED, Card.Numbers.EIGHT),
        GameCard(Card.Colors.RED, Card.Numbers.NINE),
        GameCard(Card.Colors.RED, Card.Numbers.TEN)
      )
      val phases = GamePhase(List(Phase.PhaseTypes.DOUBLE))
      val player = PlayerImpl.Player("Player 1", cards, phases)
      val checkPhase = player.checkPhase()
      checkPhase should be (Success("Phase completed"))

      val phases2 = GamePhase(List(Phase.PhaseTypes.QUINTUPLE))
      val player2 = PlayerImpl.Player("Player 2", cards, phases2)
      val checkPhase2 = player2.checkPhase()
      checkPhase2 match {
        case Success(_) => fail("Should not be success")
        case Failure(e) => e.getMessage should be("Phase not completed")
      }
    }

    "check phase with double and triple" in {
      val cards = List(
        GameCard(Card.Colors.RED, Card.Numbers.ONE),
        GameCard(Card.Colors.RED, Card.Numbers.ONE),
        GameCard(Card.Colors.RED, Card.Numbers.THREE),
        GameCard(Card.Colors.RED, Card.Numbers.FOUR),
        GameCard(Card.Colors.RED, Card.Numbers.FIVE),
        GameCard(Card.Colors.RED, Card.Numbers.SIX),
        GameCard(Card.Colors.RED, Card.Numbers.SEVEN),
        GameCard(Card.Colors.BLUE, Card.Numbers.TEN),
        GameCard(Card.Colors.BLUE, Card.Numbers.TEN),
        GameCard(Card.Colors.BLUE, Card.Numbers.TEN)
      )
      val phases = GamePhase(List(Phase.PhaseTypes.DOUBLE, Phase.PhaseTypes.TRIPLE))
      val player = PlayerImpl.Player("Player 1", cards, phases)
      val checkPhase = player.checkPhase()
      checkPhase should be (Success("Phase completed"))

      val phases2 = GamePhase(List(Phase.PhaseTypes.DOUBLE, Phase.PhaseTypes.QUADRUPLE))
      val player2 = PlayerImpl.Player("Player 2", cards, phases2)
      val checkPhase2 = player2.checkPhase()
      checkPhase2 match {
        case Success(_) => fail("Should not be success")
        case Failure(e) => e.getMessage should be ("Phase not completed")
      }
    }
    "heck phase with double and color" in {
      val cards = List(
        GameCard(Card.Colors.RED, Card.Numbers.ONE),
        GameCard(Card.Colors.RED, Card.Numbers.ONE),
        GameCard(Card.Colors.RED, Card.Numbers.TWO),
        GameCard(Card.Colors.RED, Card.Numbers.THREE),
        GameCard(Card.Colors.RED, Card.Numbers.FOUR),
        GameCard(Card.Colors.RED, Card.Numbers.FIVE),
        GameCard(Card.Colors.RED, Card.Numbers.SIX),
        GameCard(Card.Colors.RED, Card.Numbers.SEVEN),
        GameCard(Card.Colors.RED, Card.Numbers.EIGHT),
        GameCard(Card.Colors.RED, Card.Numbers.NINE)
      )
      val phases = GamePhase(List(Phase.PhaseTypes.DOUBLE, Phase.PhaseTypes.COLOR))
      val player = PlayerImpl.Player("Player 1", cards, phases)
      val checkPhase = player.checkPhase()
      checkPhase should be (Success("Phase completed"))

      val cards2 = List(
        GameCard(Card.Colors.RED, Card.Numbers.ONE),
        GameCard(Card.Colors.RED, Card.Numbers.ONE),
        GameCard(Card.Colors.RED, Card.Numbers.TWO),
        GameCard(Card.Colors.BLUE, Card.Numbers.THREE),
        GameCard(Card.Colors.BLUE, Card.Numbers.FOUR),
        GameCard(Card.Colors.BLUE, Card.Numbers.FIVE),
        GameCard(Card.Colors.GREEN, Card.Numbers.SIX),
        GameCard(Card.Colors.GREEN, Card.Numbers.SEVEN),
        GameCard(Card.Colors.YELLOW, Card.Numbers.EIGHT),
        GameCard(Card.Colors.YELLOW, Card.Numbers.NINE)
      )
      val phases2 = GamePhase(List(Phase.PhaseTypes.DOUBLE, Phase.PhaseTypes.COLOR))
      val player2 = PlayerImpl.Player("Player 1", cards2, phases)
      val checkPhase2 = player2.checkPhase()
      checkPhase2 match {
        case Success(_) => fail("Should not be success")
        case Failure(e) => e.getMessage should be("Phase not completed")
      }
    }
    "check phase with double and fourrow" in {
      val cards = List(
        GameCard(Card.Colors.RED, Card.Numbers.ONE),
        GameCard(Card.Colors.RED, Card.Numbers.ONE),
        GameCard(Card.Colors.RED, Card.Numbers.TWO),
        GameCard(Card.Colors.RED, Card.Numbers.THREE),
        GameCard(Card.Colors.RED, Card.Numbers.FOUR),
        GameCard(Card.Colors.RED, Card.Numbers.FIVE),
        GameCard(Card.Colors.RED, Card.Numbers.SIX),
        GameCard(Card.Colors.RED, Card.Numbers.SEVEN),
        GameCard(Card.Colors.RED, Card.Numbers.EIGHT),
        GameCard(Card.Colors.RED, Card.Numbers.NINE)
      )

      val phases = GamePhase(List(Phase.PhaseTypes.DOUBLE, Phase.PhaseTypes.FOURROW))
      val player = PlayerImpl.Player("Player 1", cards, phases)
      val checkPhase = player.checkPhase()
      checkPhase should be (Success("Phase completed"))
    }
    "check phase with double and joker" in {
      val cards = List(
        GameCard(Card.Colors.RED, Card.Numbers.ONE),
        GameCard(Card.Colors.RED, Card.Numbers.TWO),
        GameCard(Card.Colors.RED, Card.Numbers.THREE),
        GameCard(Card.Colors.RED, Card.Numbers.ONE),
        GameCard(Card.Colors.RED, Card.Numbers.ONE),
        GameCard(Card.Colors.RED, Card.Numbers.ONE),
        GameCard(Card.Colors.RED, Card.Numbers.ONE),
        GameCard(Card.Colors.RED, Card.Numbers.ONE),
        GameCard(Card.Colors.RED, Card.Numbers.TWO),
        GameCard(Card.Colors.BLACK, Card.Numbers.JOKER)
      )

      val phases = GamePhase(List(Phase.PhaseTypes.DOUBLE, Phase.PhaseTypes.FOURROW))
      val player = PlayerImpl.Player("Player 1", cards, phases)
      val checkPhase = player.checkPhase()
      checkPhase should be (Success("Phase completed"))

      val cards2 = List(
        GameCard(Card.Colors.RED, Card.Numbers.ONE),
        GameCard(Card.Colors.RED, Card.Numbers.TWO),
        GameCard(Card.Colors.RED, Card.Numbers.ONE),
        GameCard(Card.Colors.RED, Card.Numbers.ONE),
        GameCard(Card.Colors.RED, Card.Numbers.ONE),
        GameCard(Card.Colors.RED, Card.Numbers.ONE),
        GameCard(Card.Colors.RED, Card.Numbers.ONE),
        GameCard(Card.Colors.RED, Card.Numbers.ONE),
        GameCard(Card.Colors.RED, Card.Numbers.ONE),
        GameCard(Card.Colors.BLACK, Card.Numbers.JOKER)
      )

      val phases2 = GamePhase(List(Phase.PhaseTypes.DOUBLE, Phase.PhaseTypes.FOURROW))
      val player2 = PlayerImpl.Player("Player 1", cards2, phases)
      val checkPhase2 = player2.checkPhase()
      checkPhase2 match {
        case Success(_) => fail("Should not be success")
        case Failure(e) => e.getMessage should be ("Phase not completed")
      }


      val cards3 = List(
        GameCard(Card.Colors.RED, Card.Numbers.ONE),
        GameCard(Card.Colors.RED, Card.Numbers.TWO),
        GameCard(Card.Colors.RED, Card.Numbers.ONE),
        GameCard(Card.Colors.RED, Card.Numbers.ONE),
        GameCard(Card.Colors.RED, Card.Numbers.ONE),
        GameCard(Card.Colors.RED, Card.Numbers.ONE),
        GameCard(Card.Colors.RED, Card.Numbers.ONE),
        GameCard(Card.Colors.RED, Card.Numbers.ONE),
        GameCard(Card.Colors.RED, Card.Numbers.ONE),
        GameCard(Card.Colors.BLACK, Card.Numbers.BLOCK)
      )

      val phases3 = GamePhase(List(Phase.PhaseTypes.DOUBLE, Phase.PhaseTypes.FOURROW))
      val player3 = PlayerImpl.Player("Player 1", cards3, phases)
      val checkPhase3 = player3.checkPhase()
      checkPhase3 match {
        case Success(_) => fail("Should not be success")
        case Failure(e) => e.getMessage should be("Phase not completed")
      }
    }
    "convert to json" in {
      val cards = List(
        GameCard(Card.Colors.RED, Card.Numbers.ONE),
        GameCard(Card.Colors.RED, Card.Numbers.TWO),
        GameCard(Card.Colors.RED, Card.Numbers.THREE),
        GameCard(Card.Colors.RED, Card.Numbers.FOUR),
        GameCard(Card.Colors.RED, Card.Numbers.FIVE),
        GameCard(Card.Colors.RED, Card.Numbers.SIX),
        GameCard(Card.Colors.RED, Card.Numbers.SEVEN),
        GameCard(Card.Colors.RED, Card.Numbers.EIGHT),
        GameCard(Card.Colors.RED, Card.Numbers.NINE),
        GameCard(Card.Colors.RED, Card.Numbers.TEN)
      )
      val phases = GamePhase(List(Phase.PhaseTypes.DOUBLE))
      val player: IPlayer = PlayerImpl.Player("Player 1", cards, phases)
      val json = Json.toJson(player)
      json.toString should be(
        s"""{"name":"Player 1","cards":[{"color":"RED","number":"ONE"},{"color":"RED","number":"TWO"},{"color":"RED","number":"THREE"},{"color":"RED","number":"FOUR"},{"color":"RED","number":"FIVE"},{"color":"RED","number":"SIX"},{"color":"RED","number":"SEVEN"},{"color":"RED","number":"EIGHT"},{"color":"RED","number":"NINE"},{"color":"RED","number":"TEN"}],"phase":{"phases":["DOUBLE"]}}"""
      )
    }
    "convert from json" in {
      val cards = List(
        GameCard(Card.Colors.RED, Card.Numbers.ONE),
        GameCard(Card.Colors.RED, Card.Numbers.TWO),
        GameCard(Card.Colors.RED, Card.Numbers.THREE),
        GameCard(Card.Colors.RED, Card.Numbers.FOUR),
        GameCard(Card.Colors.RED, Card.Numbers.FIVE),
        GameCard(Card.Colors.RED, Card.Numbers.SIX),
        GameCard(Card.Colors.RED, Card.Numbers.SEVEN),
        GameCard(Card.Colors.RED, Card.Numbers.EIGHT),
        GameCard(Card.Colors.RED, Card.Numbers.NINE),
        GameCard(Card.Colors.RED, Card.Numbers.TEN)
      )
      val phases = GamePhase(List(Phase.PhaseTypes.DOUBLE))
      val player: IPlayer = PlayerImpl.Player("Player 1", cards, phases)
      val json = Json.toJson(player)
      val obj = Json.fromJson[Player](json)
      obj.get.toString should be(player.toString)
    }
    "convert to XML" in {
      val cards = List(
        GameCard(Card.Colors.RED, Card.Numbers.ONE),
        GameCard(Card.Colors.RED, Card.Numbers.TWO),
        GameCard(Card.Colors.RED, Card.Numbers.THREE),
        GameCard(Card.Colors.RED, Card.Numbers.FOUR),
        GameCard(Card.Colors.RED, Card.Numbers.FIVE),
        GameCard(Card.Colors.RED, Card.Numbers.SIX),
        GameCard(Card.Colors.RED, Card.Numbers.SEVEN),
        GameCard(Card.Colors.RED, Card.Numbers.EIGHT),
        GameCard(Card.Colors.RED, Card.Numbers.NINE),
        GameCard(Card.Colors.RED, Card.Numbers.TEN)
      )
      val phases = GamePhase(List(Phase.PhaseTypes.DOUBLE))
      val player: IPlayer = PlayerImpl.Player("Player 1", cards, phases)
      val xml = <player><name>Player 1</name><cards>{cards.map(_.toXml)}</cards><playerphases><phases><phase>DOUBLE</phase></phases></playerphases></player>
      player.toXml.toString should be(xml.toString)
    }
    "convert from XML" in {
      val cards = List(
        GameCard(Card.Colors.RED, Card.Numbers.ONE),
        GameCard(Card.Colors.RED, Card.Numbers.TWO),
        GameCard(Card.Colors.RED, Card.Numbers.THREE),
        GameCard(Card.Colors.RED, Card.Numbers.FOUR),
        GameCard(Card.Colors.RED, Card.Numbers.FIVE),
        GameCard(Card.Colors.RED, Card.Numbers.SIX),
        GameCard(Card.Colors.RED, Card.Numbers.SEVEN),
        GameCard(Card.Colors.RED, Card.Numbers.EIGHT),
        GameCard(Card.Colors.RED, Card.Numbers.NINE),
        GameCard(Card.Colors.RED, Card.Numbers.TEN)
      )
      val phases = GamePhase(List(Phase.PhaseTypes.DOUBLE))
      val player: IPlayer = PlayerImpl.Player("Player 1", cards, phases)
      val xml = <player><name>Player 1</name><cards>{cards.map(_.toXml)}</cards><playerphases><phases><phase>DOUBLE</phase></phases></playerphases></player>
      val obj = Player.fromXml(xml)
      obj.toString should be(player.toString)
    }
  }
}
