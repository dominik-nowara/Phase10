package phase10.models

import scala.util.Random

object CardHand {
  def randomStack(): List[Card.Card] = {
    val randomNum = Random.nextInt(3)
    List.fill(10 - randomNum)(Card.randomCard()) ++ Card.randomBlackCards(randomNum)
  }

  case class CardHand(cards: List[Card.Card] = randomStack()) {
    override def toString: String = cards.mkString(" | ") + s"    (${cards.length})"

    def createLine(): String = {
      cards.zipWithIndex.map { case (card, index) => s" ${index + 1}${card.extraSpace()} |" }.mkString
    }

    def removeCard(index: Int): CardHand = {
      val newCards = cards.slice(0, index) ++ cards.slice(index + 1, cards.length)
      copy(newCards)
    }

    def changeCard(index: Int): CardHand = {
      val newCards = cards.slice(0, index) ++ cards.slice(index + 1, cards.length) :+ Card.randomCard()
      copy(newCards)
    }
  }
}

