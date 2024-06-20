package phase10.models.PhaseComponent

import phase10.models.Phase

trait IGamePhase(val phases: List[Phase.PhaseTypes]) {
  override def toString: String
  val firstCheck: Phase.Check
  val secondCheck: Option[Phase.Check]
}
