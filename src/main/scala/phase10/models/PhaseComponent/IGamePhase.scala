package phase10.models.PhaseComponent

import phase10.models.Phase
import play.api.libs.json.{JsValue, Json, Writes}

import scala.xml.Node

trait IGamePhase(val phases: List[Phase.PhaseTypes]) {
  override def toString: String
  val firstCheck: Phase.Check
  val secondCheck: Option[Phase.Check]
  def toXml: Node
}

object IGamePhase {
  implicit def gamePhaseWrites: Writes[IGamePhase] = new Writes[IGamePhase]:
    override def writes(o: IGamePhase): JsValue = Json.obj(
      "phases" -> o.phases.map(p => Json.toJson(p.toString))
    )
}
