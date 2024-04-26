package phase10.views

import phase10.models.*
import phase10.util.*
import phase10.controller.GameController

import scala.annotation.tailrec
import scala.io.AnsiColor.*
import scala.io.StdIn.readLine

class TUI (val controller: GameController) extends Observer:
  controller.add(this)
  var continue = false

  override def update(e: Event): Unit =
    e match
      case Event.Quit => continue = false
      case Event.Draw => nextRound()
      case Event.Swap => {
        printSpace()
        val player = controller.round.player(controller.round.current)
        println(s"${YELLOW}Current Phase: ${player.phase}${RESET}")
        printLine()
        println(player.cardHand.createLine())
        print(player.name  + ": | ")
        println(player.cardHand.toString)
        println("Please input number of the card you want to exchange:")
      }

  def initialize(): Unit = {
    println(s"${GREEN}${BOLD}Welcome to Phase 10!${RESET}")
    println(s"${BLUE}Press 'q' to quit the game")
    println(s"Press 'h' to show the help menu${RESET}\n")

    ReadNumber()
  }

  def printLine(): Unit = print("----------|")
  def printSpace(): Unit = print("\n" * 50)

  def ReadNumber(): Unit = {
    println("Please enter the amount of players:")
    val playerCount = readLine()
    if (!playerCount.forall(_.isDigit)) {
      ReadNumber()
      return
    }

    controller.initGame(playerCount.toInt)
    println(s"${GREEN}Is the first player ready? (y/n)${RESET}")
    inputLoop()
  }

  def nextRound(): Unit = {
    printSpace()
    println(s"${BLUE}${BOLD}Player swap!${RESET}\n${GREEN}Is the player ${controller.round.current + 1} ready? (y/n)${RESET}")
  }

  def inputLoop(): Unit =
    analyseInput(readLine) match
      case None =>
      case Some("help") => {
        println(s"${GREEN}${BOLD}Help menu${RESET}\n${BLUE}Press 'q' to quit the game\nPress 'y' accept the player swap\nPress a number to play change a card${RESET}")
      }
    if (continue) inputLoop()

  def analyseInput(input: String) =
    input match
      case "q" => controller.quitGame(); None
      case "y" => {
        if (controller.round.swap) {
          controller.swap()
        }
        None
      }
      case "h" => {
        Some("help")
      }
      case _ => {
        // Check if input is number
        if (!input.forall(_.isDigit)) {
          None
        } else {
          controller.drawNewCard(input.toInt - 1)
          None
        }
      }

