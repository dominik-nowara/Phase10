package phase10.models

def initRound(count: Int = 1): Round = {
  val players = (1 to count).map(i => Player(s"Player ${i}", CardHand(), randomPhases(), false)).toList
  Round(players, 0, true)
}

case class Round(player: List[Player], current: Int, swap: Boolean) {
  def nextRound(): Round = {
    if (current == player.length - 1) {
      return copy(current = 0, swap = true)
    }

    copy(current = current + 1, swap = true)
  }

  def swapPlayer(): Round = {
    copy(player, current, false)
  }
}
