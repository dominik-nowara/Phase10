package phase10.controller

import phase10.models.Player
import phase10.util.Command

class PlayCommand(players: List[Player], position: Int, amountPlayer: Int) extends Command[List[Player]] {
  override def noStep(t: List[Player]): List[Player] = players
  override def doStep(player: List[Player]): List[Player] = {
    val current = GameManager.current
    val newPlayer = player(current).doExchange(position, amountPlayer)
    players.updated(current, newPlayer)
  }
  override def undoStep(player: List[Player]): List[Player] = {
    val current = GameManager.current
    val newPlayer = player(current).undoExchange(position, amountPlayer)
    players.updated(current, newPlayer)
  }
  override def redoStep(player: List[Player]): List[Player] = {
    val current = GameManager.current
    val newPlayer = player(current).doExchange(position, amountPlayer)
    players.updated(current, newPlayer)
  }
}