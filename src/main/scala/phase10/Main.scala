package phase10

import javafx.application.Application
import phase10.controller.GameController

import scala.util.Random
import phase10.models.*
import phase10.views.{GUI, TUI}

import java.io.{FileOutputStream, PrintStream}

object Main {
  def main(args: Array[String]): Unit = {
    val errStream = new PrintStream(new FileOutputStream("error.log"))
    System.setErr(errStream)
    val controller = GameController(List())
    val tui = TUI(controller)
    new GuiThread(controller).start()
    tui.initialize()
  }

  class GuiThread(gameController: GameController) extends Thread {
    override def run(): Unit = {
      GUI(gameController).main(Array())
    }
  }
}