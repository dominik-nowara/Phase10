package phase10.views

import phase10.models.*
import phase10.util.*
import phase10.controller.GameController

import scala.annotation.tailrec
import scala.io.AnsiColor.*
import scala.io.StdIn.readLine

class TUI (val controller: GameController) extends Observer:
  controller.add(this)

  override def update(e: Event): Unit =
    e match
      case Event.Quit => System.exit(0)
      case Event.Draw => println(controller.round.player.toString)
      case Event.Start => println(controller.round.player.toString)

  def initialize(): Unit = {
    println(s"${GREEN}${BOLD}Welcome to Phase 10!${RESET}")
    println(s"${BLUE}Press 'q' to quit the game")
    println(s"Press 'h' to show the help menu${RESET}\n")

    ReadNumber()
  }

  private def ReadNumber(): Unit = {
    println("Please enter the amount of players:")
    val playerCount = readLine()
    if (!playerCount.forall(_.isDigit)) {
      ReadNumber()
      return
    }

    controller.initRound(playerCount.toInt)
    inputLoop()
  }

  private def nextRound(): Unit = {
    println("\n" * 50)
    println(s"${GREEN}${BOLD}Player swap!${RESET}")
  }

  private def printCard(card: Card): Unit = {
    card.color match {
      case Colors.RED => print(s"${RED}${card.number}${RESET} ")
      case Colors.YELLOW => print(s"${YELLOW}${card.number}${RESET} ")
      case Colors.GREEN => print(s"${GREEN}${card.number}${RESET} ")
      case Colors.BLUE => print(s"${BLUE}${card.number}${RESET} ")
      case Colors.BLACK => print(s"${BLACK}${card.number}${RESET} ")
    }
  }

  @tailrec
  private def inputLoop(): Unit =
    analyseInput(readLine) match
      case None =>
      case Some(move) =>
    inputLoop()

  private def analyseInput(input: String) =
    input match
      case "q" => controller.quitRound(); None
      case "y" => {
        if (controller.round.swap) {
          controller.swap()
        }
        None
      }
      case _ => {
        Some("X")
      }

