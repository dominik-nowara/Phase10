package phase10.models

import scala.util.Random

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
  override def toString: String = color.toString + " " + number.toString
}

def randomCard(): Card = randomColorCard(Colors.values(Random.nextInt(Colors.values.length - 1)))

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