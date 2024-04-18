package phase10.models

case class Player(val name: String, val cards: CardHand) {
  override def toString: String = name + ": " + cards.toString
}
