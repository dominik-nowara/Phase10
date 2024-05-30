package phase10.models

import scala.annotation.tailrec
import phase10.models.GameCard

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
    def check(cardHand: List[GameCard], amount: Int): Boolean
  }

  class CheckMultiple extends Check {
    override def check(cards: List[GameCard], amount: Int): Boolean = {
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
    override def check(cards: List[GameCard], amount: Int): Boolean = {
      for (i <- cards.indices) {
        if (cards.count(card =>
          card.color == cards(i).color || card.number == Card.Numbers.JOKER)
          == 8) {
          return true
        }
      }
      false
    }
  }

  class CheckRows extends Check {
    override def check(cards: List[GameCard], amount: Int): Boolean = {
      val sortedCards = cards.sortBy(_.number.ordinal)

      @tailrec
      def checkConsecutive(cards: List[GameCard], previous: GameCard, count: Int): Boolean = {
        cards match {
          case current :: tail =>
            if (current.number.ordinal == previous.number.ordinal + 1) {
              if (count + 1 == amount) true // Found amount consecutive cards
              else checkConsecutive(tail, current, count + 1)
            } else {
              checkConsecutive(tail, current, 1)
            }
          case Nil =>
            if (count + cards.count(_.number == Card.Numbers.JOKER) >= amount) return true // Found amount consecutive cards
            false // Reached the end of the list without finding amount of consecutive cards
        }
      }

      checkConsecutive(sortedCards.tail, sortedCards.head, 1)
    }
  }
}

class GamePhase(val phases: List[Phase.PhaseTypes]) {
  override def toString: String = phases.mkString(", ")

  val firstCheck: Phase.Check = phases.head match {
    case Phase.PhaseTypes.DOUBLE | Phase.PhaseTypes.TRIPLE 
         | Phase.PhaseTypes.QUADRUPLE | Phase.PhaseTypes.QUINTUPLE => new Phase.CheckMultiple
    case Phase.PhaseTypes.FOURROW | Phase.PhaseTypes.SEVENROW 
         | Phase.PhaseTypes.EIGHTROW | Phase.PhaseTypes.NINEROW => new Phase.CheckRows
    case Phase.PhaseTypes.COLOR => new Phase.CheckColor
  }

  val secondCheck: Option[Phase.Check] = if (phases.size == 2) phases(1) match {
    case Phase.PhaseTypes.DOUBLE | Phase.PhaseTypes.TRIPLE 
         | Phase.PhaseTypes.QUADRUPLE | Phase.PhaseTypes.QUINTUPLE => Some(new Phase.CheckMultiple)
    case Phase.PhaseTypes.FOURROW | Phase.PhaseTypes.SEVENROW 
         | Phase.PhaseTypes.EIGHTROW | Phase.PhaseTypes.NINEROW => Some(new Phase.CheckRows)
    case Phase.PhaseTypes.COLOR => Some(new Phase.CheckColor)
  } else None
}

object PhaseFactory {
  def generatePhases(playerName: String, time: Long): GamePhase = {
    val hash = hashFunction(playerName, time, 0);
    val phaseTypes = Phase.PhaseTypes.values.toSeq
    
    val firstPhase = phaseTypes(hash % phaseTypes.size)

    if (hash % 3 < 2 || firstPhase.ordinal > 5)
      return GamePhase(List(firstPhase))
      
    val secondPhase = phaseTypes((hash + 3) % 5)
    
    GamePhase(List(firstPhase, secondPhase))
  }

  private def hashFunction(playerName: String, time: Long, position: Int): Int = {
    val toHash = s"$playerName$time$position";
    toHash.hashCode.abs
  }
}