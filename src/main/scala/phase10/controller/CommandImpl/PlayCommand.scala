package phase10.controller.CommandImpl

import phase10.controller.GameControllerImpl.GameManager
import phase10.models.PlayerComponent.IPlayer
import phase10.util.Command

class PlayCommand(players: List[IPlayer], position: Int, amountPlayer: Int)
  extends Command[List[IPlayer]] {
  override def noStep(t: List[IPlayer]): List[IPlayer] = players
  override def doStep(player: List[IPlayer]): List[IPlayer] = {
    val current = GameManager.current
    val newPlayer = player(current).doExchange(position, amountPlayer)
    players.updated(current, newPlayer)
  }
  override def undoStep(player: List[IPlayer]): List[IPlayer] = {
    val current = GameManager.current
    val newPlayer = player(current).undoExchange(position, amountPlayer)
    players.updated(current, newPlayer)
  }
  override def redoStep(player: List[IPlayer]): List[IPlayer] = {
    val current = GameManager.current
    val newPlayer = player(current).doExchange(position, amountPlayer)
    players.updated(current, newPlayer)
  }
}