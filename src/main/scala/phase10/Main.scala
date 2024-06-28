package phase10

import com.google.inject.{Guice, Injector}
import phase10.controller.GameControllerImpl.GameController
import phase10.controller.IGameController
import phase10.views.{GUI, TUI}

import java.io.{FileOutputStream, PrintStream}

object Main {
  val injector: Injector = Guice.createInjector(new Phase10Module)
  val controller = injector.getInstance(classOf[IGameController])

  def main(args: Array[String]): Unit = {
    //val errStream = new PrintStream(new FileOutputStream("error.log"))
    //System.setErr(errStream)
    val tui = TUI(controller)
    new GuiThread(controller).start()
    tui.initialize()
  }

  class GuiThread(gameController: IGameController) extends Thread {
    override def run(): Unit = {
      GUI(gameController).main(Array())
    }
  }
}