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

enum PhaseTypes {
  case DOUBLE
  case TRIPLE
  case QUADRUPLE
  case QUINTUPLE
  case FOURROW
  case COLOR

  case SEVENROW
  case EIGHTROW
  case NINEROW
}

class Card(val color: Colors, val number: Numbers)
class Phase(val phase: List[PhaseTypes])

def randomCard(): Card = {
  val colors = Colors.values
  val numbers = Numbers.values
  val color = colors(Random.nextInt(colors.length))

  if (color == Colors.BLACK) {
    val number = numbers(Random.nextInt(2) + 12)
    return Card(color, number)
  }

  val number = numbers(Random.nextInt(numbers.length))
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

val phaseList = List.fill(10)(randomPhases())
val list = List.fill(10)(randomCard())

list.foreach(x => println(x.color.toString + " " + x.number.toString))
phaseList.foreach(x => println(x.phase.mkString(", ")))