package phase10.util

import phase10.controller.{GameController, GameManager, PlayCommand, SwapCommand}
import phase10.models.Player

trait GameState(val position: Int) {
  def run(controller: GameController, notifyObservers: Event => Unit): Option[List[Player]] = None
}

case class PlayingState(override val position: Int) extends GameState(position: Int) {
  override def run(controller: GameController, notifyObservers: Event => Unit): Option[List[Player]] = {
    val players = controller.player
    val player = controller.undoManager.doStep(players, PlayCommand(players, position, controller.player.length))
    notifyObservers(Event.Draw)
    Some(player)
  }
}
case class StackState(override val position: Int) extends GameState(position: Int) {
  override def run(controller: GameController, notifyObservers: Event => Unit): Option[List[Player]] = {
    val players = controller.player
    val player = controller.undoManager.doStep(players, SwapCommand(players, position, controller.player.length))
    notifyObservers(Event.Draw)
    Some(player)
  }
}
case class SwapState(override val position: Int) extends GameState(position: Int) {
  override def run(controller: GameController, notifyObservers: Event => Unit): Option[List[Player]] = {
    GameManager.swap = false
    notifyObservers(Event.Swap)
    None
  }
}