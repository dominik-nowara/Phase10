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

class Card(val color: Colors, val number: Numbers) {
  override def toString: String = color.toString + " " + number.toString
}

def randomCard(): Card = {
  val colors = Colors.values
  val numbers = Numbers.values
  val color = colors(Random.nextInt(colors.length))

  if (color == Colors.BLACK) {
    val number = numbers(Random.nextInt(2) + 12)
    return Card(color, number)
  }

  val number = numbers(Random.nextInt(numbers.length - 2))
  Card(color, number)
}

def randomStack() = {
  val stack = List.fill(10)(randomCard())
  stack
}