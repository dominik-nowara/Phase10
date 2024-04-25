package phase10.models

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers.*

import phase10.models._
import scala.util.Random

class PhaseSpec extends AnyWordSpec {
  "A Phase 10 Phase" should {
    "contain at least 1 phasetype" in {
      val phase = randomPhases()
      phase.phase.size should be > 0
    }
    "single phase contain exactly one phasetype" in {
      val singlePhase = firstPhase()
      singlePhase.phase.size should be (1)
    }
    "double phase contain exactly two phasetypes" in {
      val types = PhaseTypes.values
      val phaseOne = types(Random.nextInt(types.length - 5))
      val doublePhase = secondPhase(phaseOne)
      doublePhase.phase.size should be (2)
    }
    "double phase both cards contain phasetype under 5" in {
      val types = PhaseTypes.values
      val phaseOne = types(Random.nextInt(types.length - 5))
      val doublePhase = secondPhase(phaseOne)
      doublePhase.phase.head.ordinal should be < 5
      doublePhase.phase(1).ordinal should be < 4
    }
  }
}