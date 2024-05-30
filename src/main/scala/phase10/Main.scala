package phase10

import phase10.controller.GameController

import scala.util.Random
import phase10.models.*
import phase10.views.TUI

object Main {
  def main(args: Array[String]): Unit = {
    val controller = GameController(List())
    val tui = TUI(controller)
    tui.initialize()
  }
}