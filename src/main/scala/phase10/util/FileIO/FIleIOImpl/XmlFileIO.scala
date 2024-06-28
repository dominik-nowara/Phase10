package phase10.util.FileIO.FIleIOImpl

import phase10.controller.GameControllerImpl.GameManager
import phase10.controller.IGameController
import phase10.models.PlayerComponent.PlayerImpl.Player
import phase10.util.FileIO.IFileIO

import scala.xml.Node

class XmlFileIO extends IFileIO {
  override def save(gameController: IGameController, path: String): Unit = {
    val data = <game>{GameManager.toXml}<players>{gameController.players().map(p => p.toXml)}</players></game>
    scala.xml.XML.save(path + "game.xml", data)
  }

  override def load(gameController: IGameController, path: String): Unit = {
    val data: Node = scala.xml.XML.loadFile(path + ".xml")
    (data \ "game" \ "GameManager").foreach(gm => GameManager.fromXml(gm))
    val players = (data \ "players" \ "player").map(p => Player.fromXml(p))
    gameController.setPlayers(players.toList)
    GameManager.swap = true
  }
}
