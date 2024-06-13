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
    "initialize screen should look like" in {
      val inStream = new ByteArrayInputStream("2".getBytes())
      val outStream = new ByteArrayOutputStream()
      val outCapture = ByteArrayOutputStream()

      tui.continue = false

      Console.withOut(outCapture) {
        Console.withIn(inStream) {
          tui.initialize()
        }
      }
      outCapture.toString should be(s"${BLACK_B}${BOLD}" +
        s"${BLACK_B}  ${RED}______ ${BLUE} _                      ${YELLOW}__  ${RED}___    ${RESET}\n" +
        s"${BLACK_B}  ${RED}|  __ \\${BLUE}| |                    ${YELLOW}/_ |${RED}/ _ \\   ${RESET}\n" +
        s"${BLACK_B}  ${RED}| |__) ${BLUE}| |__   ${YELLOW}__ _ ${GREEN}___  ${BLUE}___   ${YELLOW}| | ${RED}| | |  ${RESET}\n" +
        s"${BLACK_B}  ${RED}|  ___/${BLUE}| '_ \\ ${YELLOW}/ _` ${GREEN}/ __|${BLUE}/ _ \\  ${YELLOW}| |${RED} | | |  ${RESET}\n" +
        s"${BLACK_B}  ${RED}| |    ${BLUE}| | | |${YELLOW} (_| ${GREEN}\\__ \\${BLUE}  __/  ${YELLOW}| |${RED} |_| |  ${RESET}\n" +
        s"${BLACK_B}  ${RED}|_|    ${BLUE}|_| |_|${YELLOW}\\__,_${GREEN}|___/${BLUE}\\___|  ${YELLOW}|_|${RED}\\___/   ${RESET}\n" +
        s"${BLACK_B}                                            ${RESET}\n\n" +
        s"${BLUE}Press 'q' to quit the game\n" +
        s"Press 'h' to show the help menu and game instructions${RESET}\n\n" +
        s"${BOLD}${BLACK_B} Please enter the amount of players: ${RESET}\n" +
        "\n" * 50 +
        s"${GREEN}${BLACK_B} Is the first player ready? (Press 'y' if you're ready) ${RESET}\n")
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
      outCapture.toString should be(("\n" * 50) + s"${BLUE}${BOLD}Player swap!${RESET}\n${GREEN}${BLACK_B} Is player ${GameManager.current + 1} ready? (Press 'y' if you're ready) ${RESET}\n")
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
