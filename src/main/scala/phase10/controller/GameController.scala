package phase10.controller

import phase10.models.*
import phase10.util.*

class GameController(var round: Round) extends Observable {
  def initGame(count: Int): Unit = {
    round = initRound(count)
    notifyObservers(Event.Start)
  }

  def quitGame(): Unit = {
    notifyObservers(Event.Quit)
  }

  def drawNewCard(position: Int): Unit = {
    val cardHand = round.player(round.current).cardHand.changeCard(position)
    val player = round.player(round.current).copy(cardHand = cardHand)
    val players = round.player.updated(round.current, player)
    val nextRound = round.nextRound()
    round = nextRound.copy(player = players, swap = true)
    notifyObservers(Event.Draw)
  }

  def swap(): Unit = {
    round = round.swapPlayer()
    notifyObservers(Event.Swap)
  }
}
