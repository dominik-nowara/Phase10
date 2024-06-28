package phase10

import com.google.inject.{AbstractModule, TypeLiteral}
import net.codingwell.scalaguice.ScalaModule
import phase10.controller.GameControllerImpl.GameController
import phase10.controller.IGameController
import phase10.models.Card.{Colors, Numbers}
import phase10.models.CardComponent.GameCardImpl.GameCard
import phase10.models.CardComponent.IGameCard
import phase10.models.Phase.PhaseTypes
import phase10.models.PhaseComponent.GamePhaseImpl.GamePhase
import phase10.models.PhaseComponent.IGamePhase
import phase10.models.PlayerComponent.IPlayer
import phase10.models.PlayerComponent.PlayerImpl.Player
import phase10.util.FileIO.FIleIOImpl.{JsonFileIO, XmlFileIO}
import phase10.util.FileIO.IFileIO

class Phase10Module extends AbstractModule with ScalaModule {
  override def configure(): Unit = {
    // Inject cards
    bind(classOf[IGameCard]).to(classOf[GameCard])
    bind(new TypeLiteral[List[IGameCard]] {}).toInstance(List.empty)
    bind(classOf[Colors]).toInstance(Colors.RED)
    bind(classOf[Numbers]).toInstance(Numbers.ONE)

    // Inject phases
    bind(classOf[IGamePhase]).to(classOf[GamePhase])
    bind(new TypeLiteral[List[PhaseTypes]] {}).toInstance(List.empty)

    // Inject players
    bind(classOf[IPlayer]).to(classOf[Player])
    bind(new TypeLiteral[List[IPlayer]] {}).toInstance(List.empty)
    
    // Inject FileIO
    bind(classOf[IFileIO]).to(classOf[JsonFileIO])

    // Inject controller last
    bind(classOf[IGameController]).to(classOf[GameController])
  }
}
