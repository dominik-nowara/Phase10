package phase10.models

import scala.io.AnsiColor.*

object Card {
  enum Colors:
    case RED, BLUE, GREEN, YELLOW, BLACK

  enum Numbers {
    case ONE
    case TWO
    case THREE
    case FOUR
    case FIVE
    case SIX
    case SEVEN
    case EIGHT
    case NINE
    case TEN
    case ELEVEN
    case TWELVE
    case BLOCK
    case JOKER
  }
}

case class GameCard(color: Card.Colors, number: Card.Numbers) {
  override def toString: String = {
    val numberString = number match {
      case Card.Numbers.ONE => "1"
      case Card.Numbers.TWO => "2"
      case Card.Numbers.THREE => "3"
      case Card.Numbers.FOUR => "4"
      case Card.Numbers.FIVE => "5"
      case Card.Numbers.SIX => "6"
      case Card.Numbers.SEVEN => "7"
      case Card.Numbers.EIGHT => "8"
      case Card.Numbers.NINE => "9"
      case Card.Numbers.TEN => "10"
      case Card.Numbers.ELEVEN => "11"
      case Card.Numbers.TWELVE => "12"
      case Card.Numbers.BLOCK => "B"
      case Card.Numbers.JOKER => "J"
    }

    color match {
      case Card.Colors.RED => s"${RED_B}${BLACK} ${numberString} ${RESET}"
      case Card.Colors.YELLOW => s"${YELLOW_B}${BLACK} ${numberString} ${RESET}"
      case Card.Colors.GREEN => s"${GREEN_B}${BLACK} ${numberString} ${RESET}"
      case Card.Colors.BLUE => s"${BLUE_B}${BLACK} ${numberString} ${RESET}"
      case Card.Colors.BLACK => s"${BLACK_B} ${numberString} ${RESET}"
    }
  }
  def extraSpace(): String = if (number.ordinal > 8 && number.ordinal < 12) " " else ""
}

object CardFactory {
  def generateStack(playerName: String, time: Long, positions: List[Int]): List[GameCard] = {
    positions.zipWithIndex.map { case (position, index) => generateCard(playerName, time, position) }
  }
  
  def generateCard(playerName: String, time: Long, position: Int): GameCard = {
    val hash = hashFunction(playerName, time, position);
    val colors = Card.Colors.values.toSeq
    val numbers = Card.Numbers.values.toSeq

    val number = numbers(hash % numbers.size)

    if (number == Card.Numbers.JOKER) return GameCard(Card.Colors.BLACK, number)
    else if (number == Card.Numbers.BLOCK) return GameCard(Card.Colors.BLACK, number)

    val color = colors(hash % (colors.size - 1))

    GameCard(color, number)
  }

  private def hashFunction(playerName: String, time: Long, position: Int): Int = {
    val calculate = position * 4.6721 + time / (position + 2)
    val toHash = s"$playerName$calculate";
    toHash.hashCode.abs
  }
}