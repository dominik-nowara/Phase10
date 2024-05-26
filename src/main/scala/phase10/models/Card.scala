package phase10.models

import scala.util.Random
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

  case class Card(val color: Colors, val number: Numbers) {
    override def toString: String = {
      val numberString = number match {
        case Numbers.ONE => "1"
        case Numbers.TWO => "2"
        case Numbers.THREE => "3"
        case Numbers.FOUR => "4"
        case Numbers.FIVE => "5"
        case Numbers.SIX => "6"
        case Numbers.SEVEN => "7"
        case Numbers.EIGHT => "8"
        case Numbers.NINE => "9"
        case Numbers.TEN => "10"
        case Numbers.ELEVEN => "11"
        case Numbers.TWELVE => "12"
        case Numbers.BLOCK => "B"
        case Numbers.JOKER => "J"
      }

      color match {
        case Colors.RED => s"${RED_B}${BLACK} ${numberString} ${RESET}"
        case Colors.YELLOW => s"${YELLOW_B}${BLACK} ${numberString} ${RESET}"
        case Colors.GREEN => s"${GREEN_B}${BLACK} ${numberString} ${RESET}"
        case Colors.BLUE => s"${BLUE_B}${BLACK} ${numberString} ${RESET}"
        case Colors.BLACK => s"${BLACK_B} ${numberString} ${RESET}"
      }
    }

    def extraSpace(): String = if (number.ordinal > 8 && number.ordinal < 12) " " else ""
  }

  def randomCard(): Card = if (Random.nextInt(100) < 10) randomBlackCard() else randomColorCard(Colors.values(Random.nextInt(Colors.values.length - 1)))

  def randomColorCard(color: Colors) = {
    val numbers = Numbers.values
    val number = numbers(Random.nextInt(numbers.length - 2))
    Card(color, number)
  }

  def randomBlackCards(randomNumber: Int) = List.fill(randomNumber)(randomBlackCard())

  def randomBlackCard() = {
    val numbers = Numbers.values
    val number = numbers(Random.nextInt(2) + 12)
    Card(Colors.BLACK, number)
  }
}