package phase10.models.CardComponent

import phase10.models.Card

trait IGameCard(val color: Card.Colors, val number: Card.Numbers) {
  override def toString: String
  def extraSpace(): String
}
