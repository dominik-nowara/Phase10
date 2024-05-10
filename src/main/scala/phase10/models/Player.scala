package phase10.models

case class Player(name: String, cardHand: CardHand.CardHand, phase: Phase.Phase, isFinished: Boolean) {
  override def toString: String = name + ": " + cardHand.toString
}
