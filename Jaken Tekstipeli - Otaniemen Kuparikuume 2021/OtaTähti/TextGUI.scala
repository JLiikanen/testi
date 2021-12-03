package OtaTähti

import scala.io.StdIn.readLine

object TextGUI extends App {

  val game = new Adventure
  private val player = game.player
  println(this.game.welcomeMessage)
  Thread.sleep(90000)
  this.run()


  private def run() = {
    while (!this.game.isOver) {
      game.checkNeighbors()
      Thread.sleep(2000)
        if (this.game.kuparivaras.location.contains(this.game.player.location)){
            this.hostileEncounter()
            Thread.sleep(23000)
        }
        else {
          this.printAreaInfo()
          this.playTurn()

        }
    }
    println("\n" + this.game.goodbyeMessage)
  }

  private def printAreaInfo() = {
    val area = this.player.location
      println("\n\n" + area.name.toUpperCase())
      println("-" * area.name.length)
      println(area.fullDescription(this.player) + "\n")
  }

  private def hostileEncounter() = {
    println("\nJOUDUT MATKALLASI HYÖKKÄYKSEN KOHTEEKSI!\n")
    println(this.game.kuparivaras.description + "\n")
    println("Sinun on nyt pidettävä pää kylmänä, ja tehdä tärkeä päätös. Voit joko hyökätä varkaan kimppuun tai yrittää juosta pakoon. Toimivat komennot ovat fight ja run.")
    this.conflictPlayTurn()
  }

  private def playTurn() = {
    println()
    val command = readLine("Command: ")
    val turnReport = this.game.playTurn(command)
    if (turnReport.nonEmpty) {
      println(turnReport)
    }
  }

  private def conflictPlayTurn() = {
    println()
    val command = readLine("Command: ")
    val turnReport = this.game.conflictPlayTurn(command)
    if (turnReport.nonEmpty) {
      println(turnReport)
    }
  }


}
