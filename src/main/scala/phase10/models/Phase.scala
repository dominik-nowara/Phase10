package phase10.models

import scala.util.Random

enum PhaseTypes {
  case DOUBLE, TRIPLE, QUADRUPLE, QUINTUPLE

  case FOURROW
  case COLOR

  case SEVENROW
  case EIGHTROW
  case NINEROW
}

class Phase(val phase: List[PhaseTypes]) {
  override def toString: String = phase.mkString(", ")
}

def randomPhases(): Phase = {
  val types = PhaseTypes.values
  val phaseOne = types(Random.nextInt(types.length))

  if (Random.nextInt(2) == 0 || phaseOne.ordinal > 5)
    return Phase(List(phaseOne))

  secondPhase(phaseOne)
}

def firstPhase(): Phase = {
  val types = PhaseTypes.values
  val phaseOne = types(Random.nextInt(types.length - 5))
  Phase(List(phaseOne))
}

def secondPhase(phaseOne: PhaseTypes): Phase = {
  val phaseTwo = PhaseTypes.values(Random.nextInt(4))
  Phase(List(phaseOne, phaseTwo))
}