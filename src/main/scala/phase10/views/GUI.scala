package phase10.views

import phase10.controller.{GameController, GameManager}
import phase10.models.{Card, GameCard}
import phase10.util.{Event, Observer, PlayingState, StackState, SwapState}
import scalafx.application.{JFXApp3, Platform}
import scalafx.scene.Scene
import scalafx.scene.control.{Alert, Button, Label, TextField}
import scalafx.scene.image.{Image, ImageView}
import scalafx.scene.layout.{FlowPane, HBox, Priority, StackPane, VBox}
import scalafx.geometry.{Insets, Pos}
import scalafx.scene.control.Alert.AlertType
import scalafx.scene.paint.Color

import scala.util.Failure

class GUI (val controller: GameController) extends JFXApp3, Observer {
  controller.add(this)

  private var secondMiddleSection: FlowPane = new FlowPane()
  private var selectedIndex: Int = -1

  override def update(e: Event): Unit =
    e match
      case Event.Quit =>
        Platform.runLater {
          stage.close()
        }
      case Event.Draw =>
        Platform.runLater {
          stage.scene = swapScene()
        }
      case Event.Win =>
        Platform.runLater {
          stage.scene = winScene()
        }
      case Event.Swap =>
        Platform.runLater {
          stage.scene = playScene()
        }

  override def start(): Unit = {
    stage = new JFXApp3.PrimaryStage {
      title = "Phase 10"
      maximized = true
      scene = startScene()
      width = 940
      height = 820
      minWidth = 940
      minHeight = 820
    }
    stage.onCloseRequest = _ => controller.quitGame()
  }

  private def startScene(): Scene = {
    new Scene {
      val logo = new ImageView(new Image("file:src/main/resources/images/logo.png")) // Adjust path as necessary
      logo.fitWidth = 360
      logo.preserveRatio = true
      logo.smooth = true

      val textField: TextField = new TextField() {
        promptText = "Please enter the amount of player..."
        styleClass += "player-textfield"
      }

      val textFieldLabel: Label = new Label("Please enter the amount of player") {
        style = "-fx-font-weight: bold; -fx-font-size: 24px; -fx-text-fill: #FFFFFF;"
      }

      // Bundle the text field and label together
      val textVbox: VBox = new VBox(10) {
        children = Seq(textFieldLabel, textField)
        alignment = Pos.Center
      }

      // Create a button to start the game
      val button: Button = new Button("Start") {
        styleClass += "custom-button"
        onAction = _ => {
          if (textField.text.value.isEmpty || !textField.text.value.forall(_.isDigit)) {
            new Alert(AlertType.Warning) {
              initOwner(stage)
              title = "Wrong input!"
              headerText = "You have to enter a number! Please try again."
            }.showAndWait()
          }

          val inputText = textField.text.value
          controller.initGame(inputText.toInt)
        }
      }

      VBox.setMargin(logo, Insets(0, 0, 40, 0))
      VBox.setMargin(textField, Insets(0, 300, 0, 300))

      stylesheets = List("file:src/main/resources/styles.css")
      //Vbox that holds the scene
      root = new VBox(40) {
        alignment = Pos.Center
        children = Seq(logo, textVbox, button)
        styleClass += "start-background"
      }
    }
  }

  private def swapScene(): Scene = {
    new Scene {
      val textFieldLabel: Label = new Label(s"Player ${GameManager.current + 1} are you ready?") {
        style = "-fx-font-weight: bold; -fx-font-size: 40px; -fx-text-fill: #FFFFFF;"
      }

      val button: Button = new Button("Yes") {
        styleClass += "custom-button"
        onAction = _ => {
          controller.doAndPublish(SwapState(0))
        }
      }

      val topButtons: HBox = new HBox {
        children = Seq(
          new Button("Undo") {
            styleClass += "top-button"
            margin = Insets(0, 5, 0, 5)
            onAction = _ => {
              controller.undo()
            }
          },
          new Button("Redo") {
            styleClass += "top-button"
            margin = Insets(0, 5, 0, 5)
            onAction = _ => {
              controller.redo()
            }
          }
        )
        padding = Insets(10)
        margin = Insets(0, 0, 50, 0)
        alignment = Pos.CenterRight
      }

      stylesheets = List("file:src/main/resources/styles.css")
      root = new VBox(80) {
        alignment = Pos.TopCenter
        children = Seq(topButtons, textFieldLabel, button)
        styleClass += "start-background"
      }
    }
  }

  private def playScene(): Scene = {
    new Scene {
      selectedIndex = -1
      fill = Color.web("#2c2c30") // Set the background color of the scene

      val logo = new ImageView(new Image("file:src/main/resources/images/logo.png")) // Adjust path as necessary
      logo.fitWidth = 100
      logo.preserveRatio = true
      logo.smooth = true

      val cardCreator = new CardCreator
      val cards: Seq[VBox] = cardCreator.createCards(controller.player(GameManager.current).cards)

      val topButtons: HBox = new HBox {
        children = Seq(
          new Button("Check Win") {
            styleClass += "win-button"
            margin = Insets(0, 5, 0, 5)
            onAction = _ => {
              controller.win() match {
                case Failure(exception) => new Alert(AlertType.Warning) {
                  initOwner(stage)
                  title = "You can't win!"
                  headerText = "You can't win with your current cards!"
                }.showAndWait()
                case _ => ()
              }
            }
          },
          new Button("Undo") {
            styleClass += "top-button"
            margin = Insets(0, 5, 0, 5)
            onAction = _ => {
              controller.undo()
            }
          },
          new Button("Redo") {
            styleClass += "top-button"
            margin = Insets(0, 5, 0, 5)
            onAction = _ => {
              controller.redo()
            }
          }
        )
        padding = Insets(10)
        alignment = Pos.CenterRight
      }

      val topSection: HBox = new HBox {
        children = Seq(
          logo,
          topButtons
        )
        HBox.setHgrow(topButtons, Priority.Always)
        padding = Insets(10)
      }

      val newCard = new ImageView(new Image("file:src/main/resources/images/backprint.png")) // Adjust path as necessary
      newCard.fitWidth = 120
      newCard.preserveRatio = true

      val newCardBox: VBox = new VBox {
        prefWidth  = 120
        prefHeight = 160
        children = newCard
        onMouseClicked = _ => {
          if (selectedIndex == -1) {
            new Alert(AlertType.Warning) {
              initOwner(stage)
              title = "No card selected!"
              headerText = "You have to select a card to play! Please try again."
            }.showAndWait()
          }
          else {
            controller.doAndPublish(PlayingState(selectedIndex))
          }
        }
      }

      val phaseTitle: Label = new Label(s"${GameManager.current + 1}. Player Phases") {
        style = "-fx-font-weight: bold; -fx-font-size: 30px; -fx-text-fill: #FFFFFF;"
      }
      val phases: Label = new Label(controller.player(GameManager.current).phase.phases.mkString("\n")) {
        style = "-fx-font-weight: bold; -fx-font-size: 20px; -fx-text-fill: #999999;"
      }
      val phasesBox: VBox = new VBox(10) {
        children = Seq(phaseTitle, phases)
        alignment = Pos.CenterLeft
      }

      val stack: VBox = GameManager.stack match {
        case Some(card) => cardCreator.createCard(card.last, true)
        case None => new VBox() {
          prefWidth = 120
          prefHeight = 160
          styleClass += "card"
        }
      }

      val firstMiddleSection: HBox = new HBox(20) {
        children = Seq(newCardBox, stack, new StackPane {
          children = phasesBox
          alignment = Pos.CenterLeft
        })
        alignment = Pos.Center
        HBox.setHgrow(phasesBox, Priority.Always)
      }

      secondMiddleSection = new FlowPane {
        children = cards
        alignment = Pos.Center
        HBox.setHgrow(this, Priority.Always)
      }

      val middleSection: VBox = new VBox {
        children = Seq(firstMiddleSection, secondMiddleSection)
        VBox.setVgrow(secondMiddleSection, Priority.Always)
        spacing = 20
      }

      stylesheets = List("file:src/main/resources/styles.css")
      root = new VBox {
        children = Seq(topSection, middleSection)
        spacing = 20 // Spacing between sections
        VBox.setVgrow(middleSection, Priority.Always) // Middle section grows to fill space
        styleClass += "background"
      }
    }
  }

  def winScene(): Scene = {
    new Scene {
      val winLabel: Label = new Label(s"Player ${GameManager.current + 1} won the game!") {
        style = "-fx-font-weight: bold; -fx-font-size: 80px; -fx-text-fill: #29cf58;"
      }

      stylesheets = List("file:src/main/resources/styles.css")
      root = new VBox(80) {
        alignment = Pos.Center
        children = Seq(winLabel)
        styleClass += "start-background"
      }
    }
  }


   private class CardCreator {
    def createCards(cards: List[GameCard]): Seq[VBox] = {
      cards.map(card => createCard(card, stack = false))
    }

    def createCard(card: GameCard, stack: Boolean): VBox = {
      val color = getColorcode(card.color)
      val number = card.number.ordinal match {
        case 12 => "\uD83D\uDEAB"
        case 13 => "★"
        case _ => (card.number.ordinal + 1).toString
      }

      val topLabel = new Label(number) {
        style = s"-fx-font-weight: bold; -fx-font-size: 25px; -fx-text-fill: #ffffff;"
      }
      val midLabel = new Label(number) {
        style = s"-fx-font-weight: bold; -fx-font-size: 60px; -fx-text-fill: $color;"
      }
      val bottomLabel = new Label(number) {
        style = s"-fx-font-weight: bold; -fx-font-size: 25px; -fx-text-fill: #ffffff;"
        scaleY = -1
      }

      val topArea = new VBox {
        children = Seq(topLabel)
        prefWidth = 120
        prefHeight = 30
        style = s"-fx-background-color: $color; -fx-padding: 4px; -fx-border-radius: 4px; -fx-background-radius: 3px;"
      }

      val middleArea = new HBox {
        children = midLabel
        alignment = Pos.Center
      }

      val bottomArea = new VBox {
        children = Seq(bottomLabel)
        prefWidth = 120
        prefHeight = 30
        alignment = Pos.CenterRight
        style = s"-fx-background-color: $color; -fx-padding: 4px; -fx-border-radius: 4px; -fx-background-radius: 3px;"
      }

      val returnBox = new VBox() {
        prefWidth = 120
        prefHeight = 160
        children = Seq(topArea, middleArea, bottomArea)
        spacing = 0 // Spacing between sections
        styleClass += "card"
        VBox.setVgrow(middleArea, Priority.Always) // Middle section grows to fill space
        onMouseClicked = _ => {
          if (stack) {
            if (GameManager.stack.isDefined && selectedIndex == -1) {
              new Alert(AlertType.Warning) {
                initOwner(stage)
                title = "No card selected!"
                headerText = "You have to select a card to play! Please try again."
              }.showAndWait()
            }
            else if (GameManager.stack.isDefined) {
              controller.doAndPublish(StackState(selectedIndex))
            }
          }
          else {
            secondMiddleSection.children.foreach(child => child.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 5px; -fx-border-radius: 5px; -fx-border-width: 6px; -fx-border-color: #FFFFFF;"))
            this.setStyle("-fx-background-color: #ffffff; -fx-background-radius: 5px; -fx-border-radius: 5px; -fx-border-width: 6px; -fx-border-color: #aaaaaa;")
            selectedIndex = secondMiddleSection.children.indexOf(this)
          }
        }
      }
      FlowPane.setMargin(returnBox, Insets(5, 5, 5, 5)) // Setting margin for each rectangle

      returnBox
    }

    private def getColorcode(color: Card.Colors): String = {
      color match {
        case Card.Colors.RED => "#e74c3c"
        case Card.Colors.BLUE => "#3498db"
        case Card.Colors.GREEN => "#2ecc71"
        case Card.Colors.YELLOW => "#f1c40f"
        case Card.Colors.BLACK => "#050505"
      }
    }
  }
}