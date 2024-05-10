package phase10.util

import phase10.models.*

import scala.annotation.tailrec

object PhaseCheck {
  def checkPhases(player: Player): Boolean = {
    val cardHand = player.cardHand
    val phases = player.phase.phase

    val firstRound = phases.head match {
      case Phase.PhaseTypes.DOUBLE => checkMultiple(cardHand, 2)
      case Phase.PhaseTypes.TRIPLE => checkMultiple(cardHand, 3)
      case Phase.PhaseTypes.QUADRUPLE => checkMultiple(cardHand, 4)
      case Phase.PhaseTypes.QUINTUPLE => checkMultiple(cardHand, 5)
      case Phase.PhaseTypes.COLOR => checkColor(cardHand)
      case Phase.PhaseTypes.FOURROW => checkRows(cardHand, 4)
      case Phase.PhaseTypes.SEVENROW => checkRows(cardHand, 7)
      case Phase.PhaseTypes.EIGHTROW => checkRows(cardHand, 8)
      case Phase.PhaseTypes.NINEROW => checkRows(cardHand, 9)
    }

    if (!firstRound || phases.size == 1 || phases.head.ordinal > 5) {
      firstRound
    } else {
      val secondRound = phases(1) match {
        case Phase.PhaseTypes.DOUBLE => checkMultiple(cardHand, 2)
        case Phase.PhaseTypes.TRIPLE => checkMultiple(cardHand, 3)
        case Phase.PhaseTypes.QUADRUPLE => checkMultiple(cardHand, 4)
        case Phase.PhaseTypes.QUINTUPLE => checkMultiple(cardHand, 5)
        case _ => false
      }

      firstRound && secondRound
    }
  }

  def checkMultiple(cardHand: CardHand.CardHand, amount: Int): Boolean = {
    for (i <- cardHand.cards.indices) {
      if (cardHand.cards.count(card =>
        card.number == cardHand.cards(i).number
          && card.color == cardHand.cards(i).color)
        == amount) {
        return true
      }
    }
    false
  }

  def checkColor(cardHand: CardHand.CardHand): Boolean = {
    for (i <- cardHand.cards.indices) {
      if (cardHand.cards.count(card =>
        card.color == cardHand.cards(i).color)
        == 8) {
        return true
      }
    }
    false
  }

  def checkRows(cardHand: CardHand.CardHand, amount: Int): Boolean = {
    val sortedCards = cardHand.cards.sortBy(_.number.ordinal)

    @tailrec
    def checkConsecutive(cards: List[Card.Card], previous: Card.Card, count: Int): Boolean = {
      cards match {
        case current :: tail =>
          if (current.number.ordinal == previous.number.ordinal + 1) {
            if (count + 1 == amount) true // Found 4 consecutive cards
            else checkConsecutive(tail, current, count + 1)
          } else {
            checkConsecutive(tail, current, 1)
          }
        case Nil =>
          false // Reached the end of the list without finding 4 consecutive cards
      }
    }

    checkConsecutive(sortedCards.tail, sortedCards.head, 1)
  }
}
