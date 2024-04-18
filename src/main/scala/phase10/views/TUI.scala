package phase10.views

import scala.io.AnsiColor._

object TUI {
  def Initialize(): Unit = {
    println(s"${GREEN}${BOLD}Welcome to Phase 10!${RESET}")
    println(s"${BLUE}Press 'q' to quit the game")
    println(s"Press 'h' to show the help menu${RESET}")
  }
}
