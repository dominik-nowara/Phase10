package phase10.controller

import com.google.inject.Inject
import phase10.models.*
import phase10.models.CardComponent.IGameCard
import phase10.models.PlayerComponent.IPlayer
import phase10.util.*

import scala.util.{Failure, Success, Try}

class GameController @Inject() (var player: List[IPlayer]) extends Observable, IGameController {
  override val undoManager = new UndoManager[List[IPlayer]]
  def players(): List[IPlayer] = player

  def initGame(count: Int): Unit = {
    player = List.tabulate(count)(i => GameFactories.createPlayer(s"Player ${i + 1}"))
    notifyObservers(Event.Draw)
  }
  def doAndPublish(state: GameState): Unit = {
    state.run(this, notifyObservers) match {
      case Some(newPlayer) => player = newPlayer
      case None => ()
    }
  }

  def win(): Try[String] = {
    player(GameManager.current).checkPhase() match {
      case Success(text) =>
        notifyObservers(Event.Win)
        Success(text)

      case Failure(exception) => Failure(exception)
    }
  }

  def undo(): Unit = {
    player = undoManager.undoStep(player)
    notifyObservers(Event.Draw)
  }
  def redo(): Unit = {
    player = undoManager.redoStep(player)
    notifyObservers(Event.Draw)
  }
  def quitGame(): Unit = notifyObservers(Event.Quit)
}

object GameManager {
  var stack: Option[List[IGameCard]] = None
  var current: Int = 0
  var swap: Boolean = true

  def putOnStack(card: IGameCard): Unit = {
    stack = Some(stack.getOrElse(List()) :+ card)
  }

  def removeFromStack(): Unit = {
    if (stack.get.length == 1) {
      stack = None
    } else {
      stack = Some(stack.get.dropRight(stack.get.length - 1))
    }
  }

  def nextPlayer(amountPlayer: Int, amount: Int): Unit = {
    swap = true
    current = (current + amount) % amountPlayer
  }

  def previousPlayer(amountPlayer: Int, amount: Int): Unit = {
    swap = true
    current = (current - amount + amountPlayer) % amountPlayer
  }
}