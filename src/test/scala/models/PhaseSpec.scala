package phase10.models

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers.*
import phase10.util.GameFactories
import phase10.models.*
import phase10.models.PhaseComponent.GamePhaseImpl.GamePhase
import phase10.models.PhaseComponent.IGamePhase
import play.api.libs.json.Json

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
    "convert to json" in {
      val phase: IGamePhase = GamePhase(List(Phase.PhaseTypes.DOUBLE, Phase.PhaseTypes.TRIPLE))
      Json.toJson(phase).toString should be ("{\"phases\":[\"DOUBLE\",\"TRIPLE\"]}")
    }
    "convert from Json" in {
      val phase: IGamePhase = GamePhase(List(Phase.PhaseTypes.DOUBLE, Phase.PhaseTypes.TRIPLE, Phase.PhaseTypes.COLOR, Phase.PhaseTypes.QUADRUPLE, Phase.PhaseTypes.QUINTUPLE, Phase.PhaseTypes.FOURROW, Phase.PhaseTypes.SEVENROW, Phase.PhaseTypes.EIGHTROW, Phase.PhaseTypes.NINEROW))
      val json = Json.toJson(phase)
      val obj = Json.fromJson[GamePhase](json)
      obj.get.toString should be(phase.toString)
    }
    "convert to Xml" in {
      val phase: IGamePhase = GamePhase(List(Phase.PhaseTypes.DOUBLE, Phase.PhaseTypes.TRIPLE))
      val xml = <phases><phase>DOUBLE</phase><phase>TRIPLE</phase></phases>
      phase.toXml.toString should be(xml.toString)
    }
    "convert from Xml" in {
      val phase: IGamePhase = GamePhase(List(Phase.PhaseTypes.DOUBLE, Phase.PhaseTypes.TRIPLE, Phase.PhaseTypes.COLOR, Phase.PhaseTypes.QUADRUPLE, Phase.PhaseTypes.QUINTUPLE, Phase.PhaseTypes.FOURROW, Phase.PhaseTypes.SEVENROW, Phase.PhaseTypes.EIGHTROW, Phase.PhaseTypes.NINEROW))
      val xml = <playerphases><phases><phase>DOUBLE</phase><phase>TRIPLE</phase><phase>COLOR</phase><phase>QUADRUPLE</phase><phase>QUINTUPLE</phase><phase>FOURROW</phase><phase>SEVENROW</phase><phase>EIGHTROW</phase><phase>NINEROW</phase></phases></playerphases>
      val obj = GamePhase.fromXml(xml)
      obj.toString should be(phase.toString)
    }
  }
}