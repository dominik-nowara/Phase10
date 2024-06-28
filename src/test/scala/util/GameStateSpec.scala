package phase10.util

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers.*
import phase10.controller.GameControllerImpl.GameController

class GameStateSpec extends AnyWordSpec {
  "GameState" should {
    "be able to be run" in {
      val controller = GameController(List())
      controller.initGame(1)
      val gameState = TestGameState(0)

      class TestObserver(controller: GameController) extends Observer:
        controller.add(this)
        var drawBing = false

        def update(e: Event) = {
          e match {
            case Event.Draw => drawBing = true
          }
        }
        
      def notifyObservers(e: Event) = {
        controller.notifyObservers(e)
      }
      val result = gameState.run(controller, notifyObservers)
      
      result should be (None)
    }
  }
}

class TestGameState(pos: Int) extends GameState(pos)