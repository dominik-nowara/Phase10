package phase10.util.FileIO

import phase10.controller.IGameController

trait IFileIO {
  def save(gameController: IGameController, path: String): Unit
  def load(gameController: IGameController, path: String): Unit
}
