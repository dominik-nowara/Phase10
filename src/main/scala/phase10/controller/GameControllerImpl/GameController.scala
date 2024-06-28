package phase10.controller.GameControllerImpl

import com.google.inject.{Guice, Inject, Injector}
import phase10.Phase10Module
import phase10.controller.GameControllerImpl.GameManager.stack
import phase10.controller.IGameController
import phase10.models.CardComponent.GameCardImpl.GameCard
import phase10.models.CardComponent.IGameCard
import phase10.models.PlayerComponent.IPlayer
import phase10.models.PlayerComponent.PlayerImpl.Player
import phase10.util.*
import phase10.util.FileIO.IFileIO
import play.api.libs.json.{JsResult, JsSuccess, JsValue, Reads}

import scala.util.{Failure, Success, Try}
import scala.xml.Node

class GameController @Inject()(var player: List[IPlayer]) extends Observable, IGameController {
  val injector: Injector = Guice.createInjector(new Phase10Module)
  val fileIO = injector.getInstance(classOf[IFileIO])

  override val undoManager = new UndoManager[List[IPlayer]]

  def players(): List[IPlayer] = player

  override def setPlayers(newPlayer: List[IPlayer]): Unit = player = newPlayer

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

  def save(path: String): Unit = {
    fileIO.save(this, path)
    notifyObservers(Event.Save)
  }

  def load(path: String): Unit = {
    fileIO.load(this, path)
    notifyObservers(Event.Draw)
  }
}

object GameController {
  implicit def gameControllerReaders: Reads[GameController] = new Reads[GameController] {
    override def reads(json: JsValue): JsResult[GameController] = {
      val stack = (json \ "gamemanager" \ "stack").as[List[GameCard]]
      val current = (json \ "gamemanager" \ "current").as[Int]

      if (stack.isEmpty) {
        GameManager.stack = None
      }
      else {
        GameManager.stack = Some(stack)
      }
      GameManager.current = current

      val players = (json \ "players").as[List[Player]]
      JsSuccess[GameController](new GameController(players))
    }
  }
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

  def toXml: Node = {
    <GameManager>
      <current>{current}</current>
      {stack match {
      case Some(value) => <stack>{value.map(_.toXml)}</stack>
      case None => <stack></stack>
       }}
    </GameManager>
  }

  def fromXml(node: Node): Unit = {
    current = (node \ "current").text.toInt
    stack = if ((node \ "stack" \ "card").isEmpty) None else Some((node \ "stack" \ "card").map(c => GameCard.fromXml(c)).toList)
  }
}
