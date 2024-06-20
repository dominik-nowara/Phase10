package phase10.models

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers.*
import phase10.models.PhaseComponent.GamePhase
import phase10.util.GameFactories
import phase10.models._
import scala.util.Random

class PhaseSpec extends AnyWordSpec {
  "A Phase 10 Phase" should {
    "contain at least 1 phasetype" in {
      val phase = GameFactories.generatePhases("Player 1", 1)
      phase.phases.size should be > 0
    }
    "should contain 1 phase on right values" in {
      val phase = GameFactories.generatePhases("Player 1", 1)
      phase.phases.head.ordinal should be > 0
      phase.phases.length should be(1)
      phase.phases should be (List(Phase.PhaseTypes.QUINTUPLE))
    }
    "should contain 2 phases on right values" in {
      val phase = GameFactories.generatePhases("Player 1", 5)
      phase.phases.length should be(2)
      phase.phases.head.ordinal should be <= 5
      phase.phases(1).ordinal should be < 4
      phase.phases should be (List(Phase.PhaseTypes.COLOR, Phase.PhaseTypes.DOUBLE))
    }
    "printed look like" in {
      val phase = GamePhase(List(Phase.PhaseTypes.DOUBLE, Phase.PhaseTypes.TRIPLE))
      phase.toString should startWith ("DOUBLE, TRIPLE")
    }
  }
}