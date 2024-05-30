package phase10.controller

import phase10.models.*
import phase10.util.Command

class SwapCommand(players: List[Player], position: Int, playerAmount: Int) extends Command[List[Player]] {
  override def noStep(t: List[Player]): List[Player] = players
  override def doStep(players: List[Player]): List[Player] = {
    val current = GameManager.current
    val newPlayer = players(current).doStackSwap(position, playerAmount)
    players.updated(current, newPlayer)
  }
  override def undoStep(player: List[Player]): List[Player] = {
    val current = GameManager.current
    val newPlayer = players(current).undoStackSwap(position, playerAmount)
    players.updated(current, newPlayer)
  }
  override def redoStep(player: List[Player]): List[Player] = {
    val current = GameManager.current
    val newPlayer = players(current).doStackSwap(position, playerAmount)
    players.updated(current, newPlayer)
  }
}
