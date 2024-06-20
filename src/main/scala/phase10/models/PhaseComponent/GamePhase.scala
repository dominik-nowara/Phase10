package phase10.models.PhaseComponent

import com.google.inject.Inject
import phase10.models.Phase
import phase10.models.PhaseComponent.IGamePhase

class GamePhase @Inject() (override val phases: List[Phase.PhaseTypes]) extends IGamePhase(phases) {
  override def toString: String = phases.mkString(", ")

  override val firstCheck: Phase.Check = phases.head match {
    case Phase.PhaseTypes.DOUBLE | Phase.PhaseTypes.TRIPLE
         | Phase.PhaseTypes.QUADRUPLE | Phase.PhaseTypes.QUINTUPLE => new Phase.CheckMultiple
    case Phase.PhaseTypes.FOURROW | Phase.PhaseTypes.SEVENROW
         | Phase.PhaseTypes.EIGHTROW | Phase.PhaseTypes.NINEROW => new Phase.CheckRows
    case Phase.PhaseTypes.COLOR => new Phase.CheckColor
  }

  override val secondCheck: Option[Phase.Check] = if (phases.size == 2) phases(1) match {
    case Phase.PhaseTypes.DOUBLE | Phase.PhaseTypes.TRIPLE
         | Phase.PhaseTypes.QUADRUPLE | Phase.PhaseTypes.QUINTUPLE => Some(new Phase.CheckMultiple)
    case Phase.PhaseTypes.FOURROW | Phase.PhaseTypes.SEVENROW
         | Phase.PhaseTypes.EIGHTROW | Phase.PhaseTypes.NINEROW => Some(new Phase.CheckRows)
    case Phase.PhaseTypes.COLOR => Some(new Phase.CheckColor)
  } else None
}
