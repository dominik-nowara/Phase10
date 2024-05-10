package phase10.controller

import phase10.models._
import phase10.util._
import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers.*

class GameControllerSpec  extends AnyWordSpec {
  "A game controller" when {
    val controller = GameController(Round.initRound())
    "initialized" should {
      "have a game with 4 players" in {
        controller.initGame(4)
        controller.round.player.length should be (4)
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
    "first card should not be equal on exchange" in {
      val cardBefore = controller.round.player.head.cardHand.cards.head
      controller.drawNewCard(0)
      val cardAfter = controller.round.player.head.cardHand.cards.head
      cardAfter should not be (cardBefore)
    }
    "swap player correctly" in {
      val before = controller.round.swap
      controller.swap()
      val after = controller.round.swap
      after should not be (before)
    }
  }
}
