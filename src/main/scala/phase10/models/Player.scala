package phase10.models

import phase10.controller.GameManager

case class Player(name: String, cards: List[GameCard], phase: GamePhase) {
  override def toString: String = name + ": " + cardsToString()
  def cardsToString(): String = cards.mkString(" | ")

  def createLine(): String = {
    cards.zipWithIndex.map { case (card, index) => s"  ${index + 1}${card.extraSpace()}  |" }.mkString
  }

  def nextPlayer(card: GameCard, amountPlayer: Int): Unit = {
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

  def doExchange(position: Int, amountPlayer: Int): Player = {
    nextPlayer(cards(position), amountPlayer)
    val newCard = CardFactory.generateCard(name, System.currentTimeMillis(), position)
    val oldCard = cards(position)
    val beforeCards = cards.slice(0, position) :+ newCard
    val newCards = beforeCards ++ cards.slice(position + 1, cards.length)
    GameManager.putOnStack(oldCard)
    this.copy(cards = newCards)
  }

  def undoExchange(position: Int, amountPlayer: Int): Player = {
    previousPlayer(amountPlayer)
    val newCard = GameManager.stack.get.last
    val newCardBefore = cards.slice(0, position) :+ newCard;
    val newCards = newCardBefore ++ cards.slice(position + 1, cards.length)
    GameManager.removeFromStack()
    this.copy(cards = newCards)
  }

   def doStackSwap(position: Int, amountPlayer: Int): Player = {
     nextPlayer(cards(position), amountPlayer)
     swapFromStack(position)
   }

   def undoStackSwap(position: Int, amountPlayer: Int): Player = {
     previousPlayer(amountPlayer)
     swapFromStack(position)
   }

  def swapFromStack(position: Int): Player = {
    val cardOnStack = GameManager.stack.get.last
    val currentCard = cards(position)

    val newCardsBefore = cards.slice(0, position) :+ cardOnStack;
    val newCards = newCardsBefore ++ cards.slice(position + 1, cards.length)
    GameManager.stack = Some(GameManager.stack.get.slice(0, GameManager.stack.get.length - 1) :+ currentCard)

    this.copy(cards = newCards)
  }

  def checkPhase(): Boolean = {
    val firstCheck = phase.firstCheck.check(cards, phase.phases.head.phaseNumber)
    if (phase.secondCheck.isEmpty) {
      firstCheck
    }
    else {
      val secondCheck = phase.secondCheck.get.check(cards, phase.phases(1).phaseNumber)
      firstCheck && secondCheck
    }
  }
}

object PlayerFactory {
  def createPlayer(name: String): Player = {
    val cardPositions = List.range(0, 10)
    val time = System.currentTimeMillis()
    Player(name, CardFactory.generateStack(name, time, cardPositions),
      PhaseFactory.generatePhases(name, time))
  }
}