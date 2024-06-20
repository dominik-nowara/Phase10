package phase10.models.CardComponent

import com.google.inject.Inject
import phase10.models.Card
import phase10.models.Card.*

import scala.io.AnsiColor.*

case class GameCard @Inject() (override val color: Card.Colors, override val number: Card.Numbers)
  extends IGameCard(color, number) {

  override def toString: String = {
    val numberString = number match {
      case Card.Numbers.ONE => "1"
      case Card.Numbers.TWO => "2"
      case Card.Numbers.THREE => "3"
      case Card.Numbers.FOUR => "4"
      case Card.Numbers.FIVE => "5"
      case Card.Numbers.SIX => "6"
      case Card.Numbers.SEVEN => "7"
      case Card.Numbers.EIGHT => "8"
      case Card.Numbers.NINE => "9"
      case Card.Numbers.TEN => "10"
      case Card.Numbers.ELEVEN => "11"
      case Card.Numbers.TWELVE => "12"
      case Card.Numbers.BLOCK => "B"
      case Card.Numbers.JOKER => "J"
    }

    color match {
      case Card.Colors.RED => s"${RED_B}${BLACK} ${numberString} ${RESET}"
      case Card.Colors.YELLOW => s"${YELLOW_B}${BLACK} ${numberString} ${RESET}"
      case Card.Colors.GREEN => s"${GREEN_B}${BLACK} ${numberString} ${RESET}"
      case Card.Colors.BLUE => s"${BLUE_B}${BLACK} ${numberString} ${RESET}"
      case Card.Colors.BLACK => s"${BLACK_B} ${numberString} ${RESET}"
    }
  }

  def extraSpace(): String = if (number.ordinal > 8 && number.ordinal < 12) " " else ""
}
