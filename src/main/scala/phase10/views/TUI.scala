package phase10.views

import phase10.controller.{GameController, GameManager}
import phase10.util.*

import scala.io.AnsiColor.*
import scala.io.StdIn.readLine

import scala.util.Failure

class TUI (val controller: GameController) extends Observer:
  controller.add(this)
  var continue = true
  var init = false

  override def update(e: Event): Unit =
    e match
      case Event.Quit => continue = false
      case Event.Draw => nextRound()
      case Event.Win => win()
      case Event.Swap => printRound()

  def initialize(): Unit = {
    println(s"${BLACK_B}${BOLD}" +
      s"${BLACK_B}  ${RED}______ ${BLUE} _                      ${YELLOW}__  ${RED}___    ${RESET}\n" +
      s"${BLACK_B}  ${RED}|  __ \\${BLUE}| |                    ${YELLOW}/_ |${RED}/ _ \\   ${RESET}\n" +
      s"${BLACK_B}  ${RED}| |__) ${BLUE}| |__   ${YELLOW}__ _ ${GREEN}___  ${BLUE}___   ${YELLOW}| | ${RED}| | |  ${RESET}\n" +
      s"${BLACK_B}  ${RED}|  ___/${BLUE}| '_ \\ ${YELLOW}/ _` ${GREEN}/ __|${BLUE}/ _ \\  ${YELLOW}| |${RED} | | |  ${RESET}\n" +
      s"${BLACK_B}  ${RED}| |    ${BLUE}| | | |${YELLOW} (_| ${GREEN}\\__ \\${BLUE}  __/  ${YELLOW}| |${RED} |_| |  ${RESET}\n" +
      s"${BLACK_B}  ${RED}|_|    ${BLUE}|_| |_|${YELLOW}\\__,_${GREEN}|___/${BLUE}\\___|  ${YELLOW}|_|${RED}\\___/   ${RESET}\n" +
    s"${BLACK_B}                                            ${RESET}\n")
    println(s"${BLUE}Press 'q' to quit the game")
    println(s"Press 'h' to show the help menu and game instructions${RESET}\n")

    ReadNumber()
  }

  def printLine(): Unit = print("----------|")
  def printSpace(): Unit = print("\n" * 50)

  def ReadNumber(): Unit = {
    println(s"${BOLD}${BLACK_B} Please enter the amount of players: ${RESET}")
    val playerCount = readLine()
    if (!init) {
      if (!playerCount.forall(_.isDigit)) {
        ReadNumber()
        return
      }

      printSpace()
      controller.initGame(playerCount.toInt)
    }
    else {
      println("Switched from GUI to TUI! Please repeat your input:")
    }
    inputLoop()
  }

  def nextRound(): Unit = {
    init = true
    printSpace()
    println(s"${BLUE}${BOLD}Player swap!${RESET}\n${GREEN}${BLACK_B} Is player ${GameManager.current + 1} ready? (Press 'y' if you're ready) ${RESET}")
  }

  def printRound(): Unit = {
    printSpace()
    val player = controller.player(GameManager.current)
    println(s"${WHITE}Current Phase: ${BOLD}${player.phase}${RESET}")
    if (GameManager.stack.isDefined) {
      val stack = GameManager.stack
      if (stack.get.length == 1) {
        println(s"${BLUE}Current card on stack: ${RESET}${GameManager.stack.get.head.toString}${RESET}\n")
      }
      else {
        println(s"${BLUE}Current card on stack: ${RESET}${GameManager.stack.get.last.toString}${RESET}\n")
      }
    }
    else {
      println(s"${BLUE}Current card on stack: ${BOLD}None${RESET}\n")
    }
    printLine()
    println(player.createLine())
    print(player.name + ": | ")
    println(player.cardsToString())
    println(s"\n${WHITE}Number of card to exchange ${BOLD}OR${RESET}${WHITE} draw from Stack via \"s[Number]\" (Press 'h' for more information)${RESET}")
  }

  def win (): Unit = {
    continue = false
    println(s"${GREEN}${BOLD}Player ${GameManager.current} won!${RESET}\n${RESET}")
  }

  def inputLoop(): Unit =
    analyseInput(readLine) match
      case None =>
      case Some(state) => controller.doAndPublish(state)
    if (continue) inputLoop()

  def analyseInput(input: String): Option[GameState] =
    input match
      case "q" => controller.quitGame(); None
      case "y" =>
        if (GameManager.swap) {
          return Some(SwapState(0))
        }
        None
      case "h" =>
        println(s"${BOLD}-----MANUAL-----\n\n\n${YELLOW}${BOLD}Goal of Phase 10:\n${WHITE}In case you never played Phase 10 in your life: You get one or two 'Phases' and 10 random cards.\nIf you succeed in collecting cards to satisfy your phases you win.\n\n${BLUE}${BOLD}Basic Commands:${RESET}${WHITE}\nPress ${RESET}${BOLD}'q'${WHITE} to quit the game\nPress ${RESET}${BOLD}'h'${WHITE} for help\n\n${BLUE}${BOLD}Swapping:${RESET}${WHITE}\nIf your're currently swapping players please press ${RESET}${BOLD}'y'${WHITE} to start your round\n\n${BLUE}${BOLD}Round:${RESET}${WHITE}\nPress ${RESET}${BOLD}'w'${WHITE} to check your phases and win the round\nWrite a ${RESET}${BOLD}'number'${WHITE} to swap the card at given position or write ${RESET}${BOLD}'s[number]'${WHITE} (example: s1) to swap given card with last card on stack.${RESET}")
        None
      case "w" =>
        val winResult = controller.win()
        winResult match {
          case Failure(exception) => println(s"${RED_B}${BOLD} ${exception.getMessage} ${RESET}")
          case _ => ()
        }
        None
      case "u" => controller.undo(); None
      case "r" => controller.redo(); None
      case "" => None
      case null => None
      case _ =>
        if (GameManager.swap) {
          println(s"${RED_B}${BOLD}You can't play while swapping the players! ${RESET}")
          return None
        }
        if (input.contains("s")) {
          val strippedInput = input.replace("s", "")
          if (strippedInput.forall(_.isDigit)) {
            return Some(StackState(strippedInput.toInt - 1))
          }
        }
        else if (input.forall(_.isDigit)) {
          return Some(PlayingState(input.toInt - 1))
        }
        None