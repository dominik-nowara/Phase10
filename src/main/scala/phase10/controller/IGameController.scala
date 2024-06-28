package phase10.controller

import phase10.controller.GameControllerImpl.GameManager
import phase10.models.CardComponent.GameCardImpl.GameCard
import phase10.models.PlayerComponent.IPlayer
import phase10.util.{GameState, Observable, UndoManager}
import play.api.libs.json.{JsValue, Json, Writes}

import scala.util.Try
import scala.xml.Node

trait IGameController extends Observable {
  val undoManager = new UndoManager[List[IPlayer]]
  def players(): List[IPlayer]
  def setPlayers(newPlayer: List[IPlayer]): Unit
  def initGame(count: Int): Unit
  def doAndPublish(state: GameState): Unit
  def win(): Try[String]
  def undo(): Unit
  def redo(): Unit
  def quitGame(): Unit
  def save(path: String): Unit
  def load(path: String): Unit
}

object IGameController {
  implicit def gamePhaseWrites: Writes[IGameController] = new Writes[IGameController]:
    override def writes(o: IGameController): JsValue = Json.obj(
      "gamemanager" -> Json.obj(
        "stack" -> {
          GameManager.stack match {
            case Some(value) => Json.toJson(GameManager.stack)
            case None => Json.toJson(List[GameCard]())
          }
        },
        "current" -> Json.toJson(GameManager.current)
      ),
      "players" -> Json.toJson(o.players())
    )
}