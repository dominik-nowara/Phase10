package phase10.views

import phase10.models.*

import scala.io.AnsiColor.*
import scala.io.StdIn.readLine

object TUI {
  def Initialize(): Unit = {
    println(s"${GREEN}${BOLD}Welcome to Phase 10!${RESET}")
    println(s"${BLUE}Press 'q' to quit the game")
    println(s"Press 'h' to show the help menu${RESET}\n")
    println("Please enter your name:")

    val playerName = readLine()

    val player = Player(playerName, CardHand())
    println(player)
  }
}
