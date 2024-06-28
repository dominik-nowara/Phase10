package phase10.models.CardComponent.GameCardImpl

import com.google.inject.Inject
import phase10.models.Card
import phase10.models.Card.*
import phase10.models.Card.Numbers.*
import phase10.models.CardComponent.IGameCard
import play.api.libs.json.{JsObject, JsResult, JsSuccess, JsValue, Json, OWrites, Reads, Writes}

import scala.io.AnsiColor.*
import scala.xml.Node

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

  def toXml: Node = <card><color>{color}</color><number>{number}</number></card>
}

object GameCard {
  def fromXml(node: Node): IGameCard = {
    val color = (node \ "color").text match {
      case "RED" => phase10.models.Card.Colors.RED
      case "YELLOW" => phase10.models.Card.Colors.YELLOW
      case "GREEN" => phase10.models.Card.Colors.GREEN
      case "BLUE" => phase10.models.Card.Colors.BLUE
      case "BLACK" => phase10.models.Card.Colors.BLACK
    }
    val number = (node \ "number").text match {
      case "ONE" => ONE
      case "TWO" => TWO
      case "THREE" => THREE
      case "FOUR" => FOUR
      case "FIVE" => FIVE
      case "SIX" => SIX
      case "SEVEN" => SEVEN
      case "EIGHT" => EIGHT
      case "NINE" => NINE
      case "TEN" => TEN
      case "ELEVEN" => ELEVEN
      case "TWELVE" => TWELVE
      case "BLOCK" => BLOCK
      case "JOKER" => JOKER
    }
    GameCard(color, number)
  }

  implicit def gameCardReads: Reads[GameCard] = new Reads[GameCard] {
    override def reads(json: JsValue): JsResult[GameCard] = {
      val color = (json \ "color").as[String] match {
        case "RED" => Card.Colors.RED
        case "YELLOW" => Card.Colors.YELLOW
        case "GREEN" => Card.Colors.GREEN
        case "BLUE" => Card.Colors.BLUE
        case "BLACK" => Card.Colors.BLACK
      }
      val number = (json \ "number").as[String] match {
        case "ONE" => Card.Numbers.ONE
        case "TWO" => Card.Numbers.TWO
        case "THREE" => Card.Numbers.THREE
        case "FOUR" => Card.Numbers.FOUR
        case "FIVE" => Card.Numbers.FIVE
        case "SIX" => Card.Numbers.SIX
        case "SEVEN" => Card.Numbers.SEVEN
        case "EIGHT" => Card.Numbers.EIGHT
        case "NINE" => Card.Numbers.NINE
        case "TEN" => Card.Numbers.TEN
        case "ELEVEN" => Card.Numbers.ELEVEN
        case "TWELVE" => Card.Numbers.TWELVE
        case "BLOCK" => Card.Numbers.BLOCK
        case "JOKER" => Card.Numbers.JOKER
      }
      JsSuccess(new GameCard(color, number))
    }
  }
}