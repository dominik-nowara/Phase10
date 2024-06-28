package phase10.util.FileIO.FIleIOImpl

import phase10.controller.GameControllerImpl.{GameController, GameManager}
import phase10.controller.IGameController
import phase10.util.FileIO.IFileIO
import play.api.libs.json.Json

import java.io.{File, PrintWriter}
import scala.io.Source

class JsonFileIO extends IFileIO {
  override def save(gameController: IGameController, path: String): Unit = {
    val jsonData = Json.toJson(gameController).toString
    val pw = new PrintWriter(new File(path + "game.json"))
    pw.write(jsonData)
    pw.close()
  }
  override def load(gameController: IGameController, path: String): Unit = {
    val file = Source.fromFile(path + ".json").getLines.mkString
    val jsonData = Json.parse(file)
    val newGameController = jsonData.as[GameController]
    gameController.setPlayers(newGameController.players())
    GameManager.swap = true
  }
}