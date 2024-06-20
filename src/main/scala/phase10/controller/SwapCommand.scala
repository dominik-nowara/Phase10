package phase10.controller

import phase10.models.PlayerComponent.IPlayer
import phase10.util.Command

class SwapCommand(players: List[IPlayer], position: Int, playerAmount: Int) 
  extends Command[List[IPlayer]] {
  override def noStep(t: List[IPlayer]): List[IPlayer] = players
  override def doStep(player: List[IPlayer]): List[IPlayer] = {
    val current = GameManager.current
    val newPlayer = player(current).doStackSwap(position, playerAmount)
    player.updated(current, newPlayer)
  }
  override def undoStep(player: List[IPlayer]): List[IPlayer] = {
    val current = GameManager.current
    val newPlayer = player(current).undoStackSwap(position, playerAmount)
    player.updated(current, newPlayer)
  }
  override def redoStep(player: List[IPlayer]): List[IPlayer] = {
    val current = GameManager.current
    val newPlayer = player(current).doStackSwap(position, playerAmount)
    player.updated(current, newPlayer)
  }
}
