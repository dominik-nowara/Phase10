package phase10.models

case class Player(val name: String, val cards: List[Card]) {
  //Print list of cards into console
  override def toString: String = {
    cards.mkString(", ")
  }
}
