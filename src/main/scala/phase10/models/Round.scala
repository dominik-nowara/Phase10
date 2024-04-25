package phase10.models

def initRound() = {
  val player1 = Player("Player1", CardHand())
  Round(List(player1), 0, true)
}

def firstRound(count: Int) = {
  val players = List(1 to count).map(i => Player(s"Player $i", CardHand()))
  Round(players, 0, true)
}

case class Round(val player: List[Player], val current: Int, val swap: Boolean) {
  def nextRound(): Unit = {
    if (current == 0) {
      copy(player, 1, true)
    }

    copy(player, 0, true)
  }

  def swapPlayer(): Unit = {
    copy(player, current, false)
  }
}
