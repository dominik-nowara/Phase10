package phase10

import com.google.inject.{AbstractModule, Guice, Injector, Provides, Scopes, TypeLiteral}
import com.google.inject.name.Names
import net.codingwell.scalaguice.ScalaModule
import phase10.controller.{GameController, IGameController}
import phase10.models.Card.{Colors, Numbers}
import phase10.models.CardComponent.{GameCard, IGameCard}
import phase10.models.Phase.PhaseTypes
import phase10.models.PhaseComponent.{GamePhase, IGamePhase}
import phase10.models.PlayerComponent.{IPlayer, Player}

class Phase10Module extends AbstractModule with ScalaModule {
  override def configure(): Unit = {
    bind(classOf[IGameCard]).to(classOf[GameCard])
    bind(new TypeLiteral[List[IGameCard]] {}).toInstance(List.empty)
    bind(classOf[Colors]).toInstance(Colors.RED)
    bind(classOf[Numbers]).toInstance(Numbers.ONE)

    bind(classOf[IPlayer]).to(classOf[Player])
    bind(new TypeLiteral[List[IPlayer]] {}).toInstance(List.empty)

    bind(classOf[IGamePhase]).to(classOf[GamePhase])
    bind(new TypeLiteral[List[PhaseTypes]] {}).toInstance(List.empty)

    bind(classOf[IGameController]).to(classOf[GameController])
  }
}
