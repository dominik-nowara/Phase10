import scala.util.Random

object Main {
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

  enum PhaseTypes {
    case DOUBLE, TRIPLE, QUADRUPLE, QUINTUPLE

    case FOURROW
    case COLOR

    case SEVENROW
    case EIGHTROW
    case NINEROW
  }

  class Card(val color: Colors, val number: Numbers) {
    override def toString: String = color.toString + " " + number.toString
  }

  class Phase(val phase: List[PhaseTypes]) {
    override def toString: String = phase.mkString(", ")
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

  def randomPhases(): Phase = {
    val types = PhaseTypes.values
    val phaseOne = types(Random.nextInt(types.length))

    if (Random.nextInt(2) == 0 || phaseOne.ordinal > 5)
      return Phase(List(phaseOne))

    val phaseTwo = types(Random.nextInt(4))
    Phase(List(phaseOne, phaseTwo))
  }

  def main(args: Array[String]): Unit = {
    val phaseList = List.fill(10)(randomPhases())
    val list2 = List.fill(10)(randomCard())

    println(phaseList.toString())
    println(list2.toString())
  }
}