package phase10.models

import phase10.models.CardComponent.IGameCard

import scala.annotation.tailrec
import phase10.models.CardComponent.IGameCard

object Phase {
  enum PhaseTypes(val phaseNumber: Int) {
    case DOUBLE extends PhaseTypes(2)
    case TRIPLE extends PhaseTypes(3)
    case QUADRUPLE extends PhaseTypes(4)
    case QUINTUPLE extends PhaseTypes(5)

    case FOURROW extends PhaseTypes(4)
    case COLOR extends PhaseTypes(8)

    case SEVENROW extends PhaseTypes(7)
    case EIGHTROW extends PhaseTypes(8)
    case NINEROW extends PhaseTypes(9)
  }

  trait Check {
    def check(cardHand: List[IGameCard], amount: Int): Boolean
  }

  class CheckMultiple extends Check {
    override def check(cards: List[IGameCard], amount: Int): Boolean = {
      for (i <- cards.indices) {
        if (cards.count(card =>
          (card.number == cards(i).number
            && card.color == cards(i).color)
            || card.number == Card.Numbers.JOKER)
          == amount) {
          return true
        }
      }
      false
    }
  }

  class CheckColor extends Check {
    override def check(cards: List[IGameCard], amount: Int): Boolean = {
      for (color <- Card.Colors.values) {
        if (cards.count(card => card.color == color || card.number == Card.Numbers.JOKER) >= 5) {
          return true
        }
      }
      false
    }
  }

  class CheckRows extends Check {
    override def check(cards: List[IGameCard], amount: Int): Boolean = {
      val sortedCards = cards.sortBy(_.number.ordinal)

      @tailrec
      def checkConsecutive(cards: List[IGameCard], previous: IGameCard, count: Int): Boolean = {
        cards match {
          case current :: tail =>
            if (current.number.ordinal == previous.number.ordinal + 1) {
              if (count + 1 >= amount) true // Found amount consecutive cards
              else checkConsecutive(tail, current, count + 1)
            }
            else if (current.number == Card.Numbers.JOKER) {
              if (count + 1 >= amount) true // Found amount consecutive cards
              else checkConsecutive(tail, current, count + 1)
            }
            else {
              checkConsecutive(tail, current, count)
            }
          case Nil =>
            false // Reached the end of the list without finding amount of consecutive cards
        }
      }

      checkConsecutive(sortedCards.tail, sortedCards.head, 1)
    }
  }
}



