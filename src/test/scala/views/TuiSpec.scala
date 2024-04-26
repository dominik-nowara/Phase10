package phase10.views

import phase10.models.*
import phase10.controller.*

import scala.io.AnsiColor.*
import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers.*

import java.io.{ByteArrayInputStream, ByteArrayOutputStream}

class TuiSpec extends AnyWordSpec {
  "A TUI" when {
    val round = initRound(2)
    val controller = GameController(round)
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
    "next round look like" in {
      val outCapture = ByteArrayOutputStream()
      Console.withOut(outCapture) {
        tui.nextRound()
      }
      outCapture.toString should be(("\n" * 50) + s"${BLUE}${BOLD}Player swap!${RESET}\n${GREEN}Is the player ${controller.round.current + 1} ready? (y/n)${RESET}\r\n")
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
      "return None on y" in {
        tui.analyseInput("y") should be(None)
      }
      "return help on h" in {
        tui.analyseInput("h") should be(Some("help"))
      }
      "return number on number" in {
        tui.analyseInput("1") should be(None)
      }
      "return None on everything else" in {
        tui.analyseInput("test") should be(None)
      }
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
          outCapture.toString should be (s"${GREEN}${BOLD}Help menu${RESET}\n${BLUE}Press 'q' to quit the game\nPress 'y' accept the player swap\nPress a number to play change a card${RESET}\r\n")
        }
        "next round on number" in {
          val inStream = new ByteArrayInputStream("1".getBytes)
          val outStream = new ByteArrayOutputStream()
          val outCapture = ByteArrayOutputStream()

          Console.withOut(outCapture) {
            Console.withIn(inStream) {
              tui.inputLoop()
            }
          }
          outCapture.toString should be(("\n" * 50) + s"${BLUE}${BOLD}Player swap!${RESET}\n${GREEN}Is the player ${controller.round.current + 1} ready? (y/n)${RESET}\r\n")
        }
      }
    }
  }
}
