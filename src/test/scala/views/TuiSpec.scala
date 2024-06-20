package phase10.views

import phase10.models.*
import phase10.controller.*

import scala.io.AnsiColor.*
import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers.*
import phase10.util.{PlayingState, SwapState}

import java.io.{ByteArrayInputStream, ByteArrayOutputStream}

class TuiSpec extends AnyWordSpec {
  "A TUI" when {
    val controller = GameController(List())
    controller.initGame(2)
    val tui = TUI(controller)
    "be initialized with a game" should {
      "have right controller" in {
        tui.controller should be(controller)
      }

    }
    "print spaces should look like" in {
      val outCapture = ByteArrayOutputStream()
      Console.withOut(outCapture) {
        tui.printSpace()
      }
      outCapture.toString should be("\n" * 50)
    }

    "line should look like" in {
      val outCapture = ByteArrayOutputStream()
      Console.withOut(outCapture) {
        tui.printLine()
      }
      outCapture.toString should be ("----------|")
    }
    "analyze input should" should {
      "return None on quit" in {
        tui.analyseInput("q") should be(None)
      }
      "return SwapState on y" in {
        tui.analyseInput("y") should be(Some(SwapState(0)))
      }
      "return help on h" in {
        tui.analyseInput("h") should be(None)
      }
      "return None on everything else" in {
        tui.analyseInput("test") should be(None)
      }
      /*
      "input loop should" should {

        "send help on help" in {
          val inStream = new ByteArrayInputStream("h".getBytes)
          val outStream = new ByteArrayOutputStream()
          val outCapture = ByteArrayOutputStream()

          Console.withOut(outCapture) {
            Console.withIn(inStream) {
              tui.inputLoop()
            }
          }
          outCapture.toString should be (s"${GREEN}${BOLD}Help menu${RESET}\n${BLUE}Press 'q' to quit the game\nPress 'y' accept the player swap\nPress a number to play change a card${RESET}\n")
        }

      }*/
    }
  }
}
