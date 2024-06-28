package phase10.util

import phase10.models.CardComponent.GameCardImpl.GameCard
import phase10.models.CardComponent.IGameCard
import phase10.models.PhaseComponent.GamePhaseImpl.GamePhase
import phase10.models.{Card, Phase}
import phase10.models.PhaseComponent.IGamePhase
import phase10.models.PlayerComponent.{IPlayer, PlayerImpl}
import phase10.models.PlayerComponent.PlayerImpl.Player

object GameFactories {
  def createPlayer(name: String): IPlayer = {
    val cardPositions = List.range(0, 10)
    val time = System.currentTimeMillis()
    PlayerImpl.Player(name, generateStack(name, time, cardPositions),
      generatePhases(name, time))
  }

  def generateStack(playerName: String, time: Long, positions: List[Int]): List[IGameCard] = {
    positions.zipWithIndex.map { case (position, index) => generateCard(playerName, time, position) }
  }

  def generateCard(playerName: String, time: Long, position: Int): IGameCard = {
    val hash = hashFunction(playerName, time, position);
    val colors = Card.Colors.values.toSeq
    val numbers = Card.Numbers.values.toSeq

    val number = numbers(hash % numbers.size)

    if (number == Card.Numbers.JOKER) return GameCard(Card.Colors.BLACK, number)
    else if (number == Card.Numbers.BLOCK) return GameCard(Card.Colors.BLACK, number)

    val color = colors(hash % (colors.size - 1))

    GameCard(color, number)
  }

  private def hashFunction(playerName: String, time: Long, position: Int): Int = {
    val calculate = position * 4.6721 + time / (position + 2)
    val toHash = s"$playerName$calculate";
    toHash.hashCode.abs
  }

  def generatePhases(playerName: String, time: Long): IGamePhase = {
    val hash = phaseHash(playerName, time, 0);
    val phaseTypes = Phase.PhaseTypes.values.toSeq

    val firstPhase = phaseTypes(hash % phaseTypes.size)

    if (hash % 3 < 2 || firstPhase.ordinal > 5)
      return GamePhase(List(firstPhase))

    val secondPhase = phaseTypes((hash + 3) % 5)

    GamePhase(List(firstPhase, secondPhase))
  }

  private def phaseHash(playerName: String, time: Long, position: Int): Int = {
    val toHash = s"$playerName$time$position";
    toHash.hashCode.abs
  }
}
