package phase10.models.PlayerComponent

import phase10.models.CardComponent.IGameCard
import phase10.models.PhaseComponent.IGamePhase
import scala.util.Try

trait IPlayer(val name: String, val cards: List[IGameCard], val phase: IGamePhase) {
  override def toString: String
  def cardsToString(): String
  def createLine(): String
  def nextPlayer(card: IGameCard, amountPlayer: Int): Unit
  def previousPlayer(amountPlayer: Int): Unit
  def doExchange(position: Int, amountPlayer: Int): IPlayer
  def undoExchange(position: Int, amountPlayer: Int): IPlayer
  def doStackSwap(position: Int, amountPlayer: Int): IPlayer
  def undoStackSwap(position: Int, amountPlayer: Int): IPlayer
  def swapFromStack(position: Int): IPlayer
  def checkPhase(): Try[String]
}
