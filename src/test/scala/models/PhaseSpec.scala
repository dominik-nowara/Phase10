package phase10.models

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers.*

import phase10.models._
import scala.util.Random

class PhaseSpec extends AnyWordSpec {
  "A Phase 10 Phase" should {
    "contain at least 1 phasetype" in {
      val phase = Phase.randomPhases()
      phase.phase.size should be > 0
    }
    "contain 1 or 2 types" in {
      val phase = Phase.randomPhases()
      if (phase.phase.length == 2) {
        phase.phase.head.ordinal should be <= 5
        phase.phase(1).ordinal should be < 4
        phase.phase.length should be (2)
      }
      else {
        phase.phase.head.ordinal should be > 0
        phase.phase.length should be (1)
      }
    }
    "single phase contain exactly one phasetype" in {
      val singlePhase = Phase.firstPhase()
      singlePhase.phase.size should be (1)
    }
    "double phase contain exactly two phasetypes" in {
      val types = Phase.PhaseTypes.values
      val phaseOne = types(Random.nextInt(types.length - 5))
      val doublePhase = Phase.secondPhase(phaseOne)
      doublePhase.phase.size should be (2)
    }
    "double phase both cards contain phasetype under 5" in {
      val types = Phase.PhaseTypes.values
      val phaseOne = types(Random.nextInt(types.length - 5))
      val doublePhase = Phase.secondPhase(phaseOne)
      doublePhase.phase.head.ordinal should be < 5
      doublePhase.phase(1).ordinal should be < 4
    }
    "phase string should look like" in {
      val types = Phase.PhaseTypes.values
      val phaseType1 = types(0)
      val phaseType2 = types(0)
      val phase = Phase.Phase(List(phaseType1, phaseType2))
      phase.toString should be (s"DOUBLE, DOUBLE")
    }
  }
}