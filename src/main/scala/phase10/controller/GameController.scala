package phase10.controller

import phase10.models.*
import phase10.util.*

class GameController(var round: Round) extends Observable {
  def initRound(count: Int): Unit = {
    round = firstRound(count)
    notifyObservers(Event.Start)
  }

  def quitRound(): Unit = {
    notifyObservers(Event.Quit)
  }

  def drawNewCard(position: Int): Unit = {
    round.player(round.current).cards.changeCard(position)
    notifyObservers(Event.Draw)
  }

  def swap(): Unit = {
    round.swapPlayer()
    notifyObservers(Event.Swap)
  }
}
