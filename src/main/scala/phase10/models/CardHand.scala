package phase10.models

import scala.util.Random

def randomStack(): List[Card] = {
  val randomNum = Random.nextInt(3)
  List.fill(10 - randomNum)(randomCard()) ++ randomBlackCards(randomNum)
}

case class CardHand(cards: List[Card] = randomStack()) {
  override def toString: String = cards.mkString(", ") + s" (${cards.length})"

  def removeCard(index: Int): CardHand = {
    val newCards = cards.slice(0, index)++ cards.slice(index + 1, cards.length)
    copy(newCards)
  }
}

