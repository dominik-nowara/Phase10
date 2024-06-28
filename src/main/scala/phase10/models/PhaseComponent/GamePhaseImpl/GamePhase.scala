package phase10.models.PhaseComponent.GamePhaseImpl

import com.google.inject.Inject
import phase10.models.Phase
import phase10.models.PhaseComponent.IGamePhase
import play.api.libs.json.{JsResult, JsSuccess, JsValue, Json, OWrites, Reads, Writes}

import scala.xml.Node

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

  override def toXml: Node = <phases>{phases.map(p => <phase>{p}</phase>)}</phases>
}

object GamePhase {
  def fromXml(node: Node): IGamePhase = {
    val phases = (node \ "phases" \ "phase").map(p => {
      p.text match {
        case "DOUBLE" => Phase.PhaseTypes.DOUBLE
        case "TRIPLE" => Phase.PhaseTypes.TRIPLE
        case "QUADRUPLE" => Phase.PhaseTypes.QUADRUPLE
        case "QUINTUPLE" => Phase.PhaseTypes.QUINTUPLE
        case "FOURROW" => Phase.PhaseTypes.FOURROW
        case "SEVENROW" => Phase.PhaseTypes.SEVENROW
        case "EIGHTROW" => Phase.PhaseTypes.EIGHTROW
        case "NINEROW" => Phase.PhaseTypes.NINEROW
        case "COLOR" => Phase.PhaseTypes.COLOR
      }
    }).toList
    new GamePhase(phases)
  }

  implicit def gamePhaseReads: Reads[GamePhase] = new Reads[GamePhase] {
    override def reads(json: JsValue): JsResult[GamePhase] = {
      val phases = (json \ "phases").as[List[JsValue]].map(p => {
        p.as[String] match {
          case "DOUBLE" => Phase.PhaseTypes.DOUBLE
          case "TRIPLE" => Phase.PhaseTypes.TRIPLE
          case "QUADRUPLE" => Phase.PhaseTypes.QUADRUPLE
          case "QUINTUPLE" => Phase.PhaseTypes.QUINTUPLE
          case "FOURROW" => Phase.PhaseTypes.FOURROW
          case "SEVENROW" => Phase.PhaseTypes.SEVENROW
          case "EIGHTROW" => Phase.PhaseTypes.EIGHTROW
          case "NINEROW" => Phase.PhaseTypes.NINEROW
          case "COLOR" => Phase.PhaseTypes.COLOR
        }
      })
      JsSuccess(GamePhase(phases))
    }
  }
}