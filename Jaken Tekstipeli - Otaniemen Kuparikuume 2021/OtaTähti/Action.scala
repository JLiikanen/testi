package OtaTähti



class Action(input: String) {

  private val commandText = input.trim.toLowerCase
  private val verb        = commandText.takeWhile( _ != ' ' )
  private val modifiers   = commandText.drop(verb.length).trim


  /** Causes the given player to take the action represented by this object, assuming
    * that the command was understood. Returns a description of what happened as a result
    * of the action (such as "You go west."). The description is returned in an `Option`
    * wrapper; if the command was not recognized, `None` is returned. */
  def execute(actor: Player) : Option[String] = this.verb match {
    case "go"        => Some(actor.go(this.modifiers))
    case "quit"      => Some(actor.quit())
    case "get"       => Some(actor.get(this.modifiers))
    case "drop"      => Some(actor.drop(this.modifiers))
    case "inventory" => Some(actor.inventory)
    case "examine"   => Some(actor.examine(this.modifiers))
    case "search"    => Some(actor.search)
    case "use"       => Some(actor.useItem(this.modifiers))
    case "help"      => Some(actor.help())
    case "travel"    => Some(actor.travel(this.modifiers))
    case "map"       => Some(actor.mapOfOtaniemi)
    case other       => None
  }



  override def toString = this.verb + " (modifiers: " + this.modifiers + ")"


}

class ConflictAction(input : String) {

   private val commandText = input.trim.toLowerCase
   private val verb        = commandText.takeWhile( _ != ' ' )
   private val modifiers   = commandText.drop(verb.length).trim

  def execute(actor: Player) : Option[String] = this.verb match {
    case "fight"     => Some(actor.fight)
    case "run"       => Some(actor.run)
    case other       => None
  }


}


