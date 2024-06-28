package phase10.util

import phase10.controller.CommandImpl.{PlayCommand, SwapCommand}
import phase10.controller.GameControllerImpl.{GameController, GameManager}
import phase10.controller.IGameController
import phase10.models.PlayerComponent.IPlayer

trait GameState(val position: Int) {
  def run(controller: IGameController, notifyObservers: Event => Unit): Option[List[IPlayer]] = None
}

case class PlayingState(override val position: Int) extends GameState(position: Int) {
  override def run(controller: IGameController, notifyObservers: Event => Unit): Option[List[IPlayer]] = {
    val players = controller.players()
    val player = controller.undoManager.doStep(players, PlayCommand(players, position, players.length))
    notifyObservers(Event.Draw)
    Some(player)
  }
}
case class StackState(override val position: Int) extends GameState(position: Int) {
  override def run(controller: IGameController, notifyObservers: Event => Unit): Option[List[IPlayer]] = {
    val players = controller.players()
    val player = controller.undoManager.doStep(players, SwapCommand(players, position, players.length))
    notifyObservers(Event.Draw)
    Some(player)
  }
}
case class SwapState(override val position: Int) extends GameState(position: Int) {
  override def run(controller: IGameController, notifyObservers: Event => Unit): Option[List[IPlayer]] = {
    GameManager.swap = false
    notifyObservers(Event.Swap)
    None
  }
}