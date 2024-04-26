package phase10.models

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers.*

class RoundSpec extends AnyWordSpec {
  "A Round" when {
    "initialized" should {
      val round = initRound()
      "have one player" in {
        round.player.length should be(1)
      }
      "current player should be the first player" in {
        round.current should be(0)
      }
      "be swap at first turn" in {
        round.swap should be(true)
      }
    }
    "initialized with two players" should {
      val round = initRound(2)
      "have two players" in {
        round.player.length should be(2)
      }
      "current player should be the first player" in {
        round.current should be(0)
      }
      "be swap at first turn" in {
        round.swap should be(true)
      }
    }
    "change something" should {
      "next round with two players" in {
        val round = initRound(2)
        val nextRound = round.nextRound()
        nextRound.current should be(1)
        nextRound.swap should be(true)
      }
      "next round with one player" in {
        val round = initRound()
        val nextRound = round.nextRound()
        nextRound.current should be(0)
        nextRound.swap should be(true)
      }
      "swap player" in {
        val round = initRound()
        val nextRound = round.swapPlayer()
        nextRound.current should be(0)
        nextRound.swap should be(false)
      }
    }
  }
}
