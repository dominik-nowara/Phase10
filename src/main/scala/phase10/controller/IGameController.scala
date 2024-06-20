package phase10.controller

import phase10.models.PlayerComponent.IPlayer
import phase10.util.{GameState, Observable, UndoManager}

import scala.util.{Try}

trait IGameController extends Observable {
  val undoManager = new UndoManager[List[IPlayer]]
  def players(): List[IPlayer]
  def initGame(count: Int): Unit
  def doAndPublish(state: GameState): Unit
  def win(): Try[String]
  def undo(): Unit
  def redo(): Unit
  def quitGame(): Unit
}