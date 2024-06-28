package phase10.models.CardComponent

import phase10.models.Card
import phase10.models.CardComponent.GameCardImpl.GameCard
import play.api.libs.json.{JsObject, JsResult, JsSuccess, JsValue, Json, Reads, Writes}

import scala.xml.Node

trait IGameCard(val color: Card.Colors, val number: Card.Numbers) {
  override def toString: String
  def extraSpace(): String
  def toXml: Node
}

object IGameCard {
  implicit def gameCardWrites: Writes[IGameCard] = new Writes[IGameCard] {
    override def writes(gameCard: IGameCard): JsObject = Json.obj(
      "color" -> gameCard.color.toString,
      "number" -> gameCard.number.toString
    )
  }
}