package phase10.models

case class Player(name: String, cardHand: CardHand, phase: Phase, isFinished: Boolean) {
  override def toString: String = name + ": " + cardHand.toString
}
