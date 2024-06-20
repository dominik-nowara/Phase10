package phase10.models.PlayerComponent

import com.google.inject.Inject
import phase10.controller.GameManager
import phase10.models.Card
import phase10.models.CardComponent.IGameCard
import phase10.models.PhaseComponent.IGamePhase
import phase10.util.GameFactories

import scala.util.{Failure, Success, Try}

case class Player @Inject() (
   override val name: String,
   override val cards: List[IGameCard],
   override val phase: IGamePhase
 )
  extends IPlayer (
    name,
    cards,
    phase
  ) {
  override def toString: String = name + ": " + cardsToString()
  def cardsToString(): String = cards.mkString(" | ")

  def createLine(): String = {
    cards.zipWithIndex.map { case (card, index) => s"  ${index + 1}${card.extraSpace()}  |" }.mkString
  }

  def nextPlayer(card: IGameCard, amountPlayer: Int): Unit = {
    if (card.number == Card.Numbers.BLOCK) {
      GameManager.nextPlayer(amountPlayer, 2)
    }
    else {
      GameManager.nextPlayer(amountPlayer, 1)
    }
  }

  def previousPlayer(amountPlayer: Int): Unit = {
    if (GameManager.stack.isDefined && GameManager.stack.get.last.number == Card.Numbers.BLOCK) {
      GameManager.previousPlayer(amountPlayer, 2)
    }
    else {
      GameManager.previousPlayer(amountPlayer, 1)
    }
  }

  def doExchange(position: Int, amountPlayer: Int): IPlayer = {
    nextPlayer(cards(position), amountPlayer)
    val newCard = GameFactories.generateCard(name, System.currentTimeMillis(), position)
    val oldCard = cards(position)
    val beforeCards = cards.slice(0, position) :+ newCard
    val newCards = beforeCards ++ cards.slice(position + 1, cards.length)
    GameManager.putOnStack(oldCard)
    this.copy(cards = newCards)
  }

  def undoExchange(position: Int, amountPlayer: Int): IPlayer = {
    previousPlayer(amountPlayer)
    val newCard = GameManager.stack.get.last
    val newCardBefore = cards.slice(0, position) :+ newCard;
    val newCards = newCardBefore ++ cards.slice(position + 1, cards.length)
    GameManager.removeFromStack()
    this.copy(cards = newCards)
  }

  def doStackSwap(position: Int, amountPlayer: Int): IPlayer = {
    nextPlayer(cards(position), amountPlayer)
    swapFromStack(position)
  }

  def undoStackSwap(position: Int, amountPlayer: Int): IPlayer = {
    previousPlayer(amountPlayer)
    swapFromStack(position)
  }

  def swapFromStack(position: Int): IPlayer = {
    val cardOnStack = GameManager.stack.get.last
    val currentCard = cards(position)

    val newCardsBefore = cards.slice(0, position) :+ cardOnStack;
    val newCards = newCardsBefore ++ cards.slice(position + 1, cards.length)
    GameManager.stack = Some(GameManager.stack.get.slice(0, GameManager.stack.get.length - 1) :+ currentCard)

    this.copy(cards = newCards)
  }

  def checkPhase(): Try[String] = {
    val firstCheck = phase.firstCheck.check(cards, phase.phases.head.phaseNumber)
    if (phase.secondCheck.isEmpty) {
      if (firstCheck) {
        Success("Phase completed")
      }
      else {
        Failure(Exception("Phase not completed"))
      }
    }
    else {
      val secondCheck = phase.secondCheck.get.check(cards, phase.phases(1).phaseNumber)
      if (firstCheck && secondCheck) {
        Success("Phase completed")
      }
      else {
        Failure(Exception("Phase not completed"))
      }
    }
  }
}