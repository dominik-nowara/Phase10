package phase10.controller

import phase10.models.*
import phase10.util.*
import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers.*
import phase10.controller.GameControllerImpl.{GameController, GameManager}
import phase10.models.CardComponent.GameCardImpl.GameCard
import phase10.models.PhaseComponent.GamePhaseImpl.GamePhase
import phase10.models.PlayerComponent.PlayerImpl
import phase10.models.PlayerComponent.PlayerImpl.Player
import phase10.util.FileIO.FIleIOImpl.JsonFileIO

import scala.util.{Failure, Success}

class GameControllerSpec extends AnyWordSpec {
  "A game controller" when {
    val controller = GameController(List())
    controller.initGame(2)
    "initialized" should {
      "have a game with 4 players" in {
        controller.initGame(4)
        controller.player.length should be (4)
      }
    }
    "notify its observers on change" in {
      class TestObserver(controller: GameController) extends Observer:
        controller.add(this)
        var bing = false
        def update(e: Event) = bing = true

      val testObserver = TestObserver(controller)
      testObserver.bing should be(false)
      controller.quitGame()
      testObserver.bing should be(true)
    }
    "don't notify its observers on remove" in {
      class TestObserver(val controller: GameController) extends Observer:
        controller.add(this)
        var bing = false

        def update(e: Event) = bing = true

      val testObserver = TestObserver(controller)
      testObserver.controller.remove(testObserver)
      testObserver.bing should be(false)
      controller.quitGame()
      testObserver.bing should be(false)
    }
    "save and load with json should work" in {
      val jsonFileIO = JsonFileIO()
      val player = controller.players()
      jsonFileIO.save(controller, "")
      jsonFileIO.load(controller, "game")
      controller.players().toString() should be (player.toString())

      GameManager.stack = Some(List(GameCard(Card.Colors.RED, Card.Numbers.ONE)))
      jsonFileIO.save(controller, "")
      jsonFileIO.load(controller, "game")
      controller.players().toString() should be(player.toString())
    }
    "save and load with xml should work" in {
      val xmlFileIO = FileIO.FIleIOImpl.XmlFileIO()
      val player = controller.players()
      xmlFileIO.save(controller, "")
      xmlFileIO.load(controller, "game")
      controller.players().toString() should be (player.toString())

      GameManager.stack = Some(List(GameCard(Card.Colors.RED, Card.Numbers.ONE)))
      xmlFileIO.save(controller, "")
      xmlFileIO.load(controller, "game")
      GameManager.stack should be(Some(List(GameCard(Card.Colors.RED, Card.Numbers.ONE))))
      controller.players().toString() should be (player.toString())
    }
    "game controller save should work" in {
      class TestObserver(controller: GameController) extends Observer:
        controller.add(this)
        var bing = false

        def update(e: Event) = {
          e match {
            case Event.Save => bing = true
            case Event.Draw => bing = true
          }
        }

      val testObserver = TestObserver(controller)

      testObserver.bing should be(false)
      controller.save("")
      testObserver.bing should be (true)
    }
    "game controller load should work" in {
      class TestObserver(controller: GameController) extends Observer:
        controller.add(this)
        var bing = false

        def update(e: Event) = {
          e match {
            case Event.Draw => bing = true
            case _ => ()
          }
        }

      val testObserver = TestObserver(controller)

      testObserver.bing should be(false)
      controller.save("")
      controller.load("game")
      testObserver.bing should be(true)
    }
    "game manager xml should work" in {
      GameManager.stack = Some(List(GameCard(Card.Colors.RED, Card.Numbers.ONE)))
      val xml = GameManager.toXml
      GameManager.fromXml(xml)
      GameManager.current should be(0)
      GameManager.stack should be(Some(List(GameCard(Card.Colors.RED, Card.Numbers.ONE))))

      GameManager.stack = None
      val xml2 = GameManager.toXml
      GameManager.fromXml(xml2)
      GameManager.current should be(0)
      GameManager.stack should be(None)
    }
    "do and publish should work" in {
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
      val players = List(PlayerImpl.Player("Player 1", cards, phases))
      val customControllers = GameController(players)

      GameManager.stack = None
      GameManager.swap = true
      GameManager.current = 0

      class TestObserver(controller: GameController) extends Observer:
        controller.add(this)
        var drawBing = false
        var swapBing = false

        def update(e: Event) = {
          e match {
            case Event.Draw => drawBing = true
            case Event.Swap => swapBing = true
          }
        }

      val testObserver = TestObserver(customControllers)


      testObserver.drawBing should be(false)
      customControllers.doAndPublish(PlayingState(0))
      GameManager.stack.isDefined should be(true)
      testObserver.drawBing should be(true)

      testObserver.drawBing = false

      testObserver.drawBing should be(false)
      val stackCard = GameManager.stack.get.head
      customControllers.doAndPublish(StackState(1))
      GameManager.stack.isDefined should be (true)
      GameManager.stack.get.head should be (GameCard(Card.Colors.BLUE, Card.Numbers.ONE))
      customControllers.player.head.cards(1) should be (GameCard(Card.Colors.RED, Card.Numbers.ONE))
      testObserver.drawBing should be(true)


      testObserver.swapBing should be (false)
      GameManager.swap should be (true)
      customControllers.doAndPublish(SwapState(1))
      GameManager.swap should be (false)
      testObserver.swapBing should be (true)
    }
    "win should work" in {
      val cards = List(
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
      val phases = GamePhase(List(Phase.PhaseTypes.QUINTUPLE))
      val phases2 = GamePhase(List(Phase.PhaseTypes.DOUBLE))
      val players = List(PlayerImpl.Player("Player 1", cards, phases), PlayerImpl.Player("Player 2", cards, phases2))

      val customController = GameController(players)

      class TestObserver(controller: GameController) extends Observer:
        controller.add(this)
        var bing = false

        def update(e: Event) = {
          e match {
            case Event.Win => bing = true
          }
        }
      val testObserver = TestObserver(customController)

      GameManager.current = 0
      testObserver.bing should be (false)
      val failWin = customController.win()
      testObserver.bing should be (false)
      failWin match {
        case Success(_) => fail("Should not be success")
        case Failure(e) => e.getMessage should be("Phase not completed")
      }

      GameManager.current = 1

      testObserver.bing should be(false)
      val win = customController.win()
      testObserver.bing should be(true)
      win should be (Success("Phase completed"))
    }
    "undo on play command" in {
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
      val players = List(PlayerImpl.Player("Player 1", cards, phases))
      val customController = GameController(players)

      class TestObserver(controller: GameController) extends Observer:
        controller.add(this)
        var drawBing = false

        def update(e: Event) = {
          e match {
            case Event.Draw => drawBing = true
          }
        }

      GameManager.current = 0
      GameManager.stack = None

      val testObserver = TestObserver(customController)
      testObserver.drawBing should be(false)
      customController.doAndPublish(PlayingState(0))
      GameManager.stack.isDefined should be(true)
      GameManager.stack.get.head should be(GameCard(Card.Colors.RED, Card.Numbers.ONE))
      testObserver.drawBing should be(true)

      testObserver.drawBing = false

      testObserver.drawBing should be(false)
      customController.undo()
      GameManager.stack.isDefined should be(false)
      testObserver.drawBing should be(true)
    }
    "undo on stack swap command" in {
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
      val players = List(PlayerImpl.Player("Player 1", cards, phases))
      val customController = GameController(players)

      customController.undo()
      players should be(customController.player)

      class TestObserver(controller: GameController) extends Observer:
        controller.add(this)
        var swapBing = false

        def update(e: Event) = {
          e match {
            case Event.Draw => swapBing = true
          }
        }

      GameManager.current = 0
      GameManager.stack = Some(List(
        GameCard(Card.Colors.GREEN, Card.Numbers.TEN)
      ))

      val testObserver = TestObserver(customController)
      testObserver.swapBing should be(false)
      customController.doAndPublish(StackState(0))
      GameManager.stack.isDefined should be(true)
      GameManager.stack.get.head should be(GameCard(Card.Colors.RED, Card.Numbers.ONE))
      customController.player.head.cards.head should be(GameCard(Card.Colors.GREEN, Card.Numbers.TEN))
      testObserver.swapBing should be(true)

      testObserver.swapBing = false

      testObserver.swapBing should be(false)
      customController.undo()
      customController.player.head.cards.head should be(GameCard(Card.Colors.RED, Card.Numbers.ONE))
      GameManager.stack.get.head should be(GameCard(Card.Colors.GREEN, Card.Numbers.TEN))
      testObserver.swapBing should be(true)
    }
    "redo on play command" in {
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
      val players = List(PlayerImpl.Player("Player 1", cards, phases))
      val customController = GameController(players)

      customController.redo()
      players should be(customController.player)


      class TestObserver(controller: GameController) extends Observer:
        controller.add(this)
        var drawBing = false

        def update(e: Event) = {
          e match {
            case Event.Draw => drawBing = true
          }
        }

      GameManager.current = 0
      GameManager.stack = None

      val testObserver = TestObserver(customController)
      testObserver.drawBing should be(false)
      customController.doAndPublish(PlayingState(0))
      GameManager.stack.isDefined should be(true)
      GameManager.stack.get.head should be(GameCard(Card.Colors.RED, Card.Numbers.ONE))
      testObserver.drawBing should be(true)

      testObserver.drawBing = false

      testObserver.drawBing should be(false)
      customController.undo()
      GameManager.stack.isDefined should be(false)
      testObserver.drawBing should be(true)

      testObserver.drawBing = false

      testObserver.drawBing should be(false)
      customController.redo()
      GameManager.stack.isDefined should be(true)
      GameManager.stack.get.head should be(GameCard(Card.Colors.RED, Card.Numbers.ONE))
      testObserver.drawBing should be(true)
    }
    "redo on stack swap command" in {
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
      val players = List(PlayerImpl.Player("Player 1", cards, phases))
      val customController = GameController(players)

      class TestObserver(controller: GameController) extends Observer:
        controller.add(this)
        var swapBing = false

        def update(e: Event) = {
          e match {
            case Event.Draw => swapBing = true
          }
        }

      GameManager.current = 0
      GameManager.stack = Some(List(
        GameCard(Card.Colors.GREEN, Card.Numbers.TEN)
      ))

      val testObserver = TestObserver(customController)
      testObserver.swapBing should be(false)
      customController.doAndPublish(StackState(0))
      GameManager.stack.isDefined should be(true)
      GameManager.stack.get.head should be(GameCard(Card.Colors.RED, Card.Numbers.ONE))
      customController.player.head.cards.head should be(GameCard(Card.Colors.GREEN, Card.Numbers.TEN))
      testObserver.swapBing should be(true)

      testObserver.swapBing = false

      testObserver.swapBing should be(false)
      customController.undo()
      customController.player.head.cards.head should be(GameCard(Card.Colors.RED, Card.Numbers.ONE))
      GameManager.stack.get.head should be(GameCard(Card.Colors.GREEN, Card.Numbers.TEN))
      testObserver.swapBing should be(true)

      testObserver.swapBing = false

      testObserver.swapBing should be(false)
      customController.redo()
      GameManager.stack.isDefined should be(true)
      GameManager.stack.get.head should be(GameCard(Card.Colors.RED, Card.Numbers.ONE))
      customController.player.head.cards.head should be(GameCard(Card.Colors.GREEN, Card.Numbers.TEN))
      testObserver.swapBing should be(true)
    }
  }
}
