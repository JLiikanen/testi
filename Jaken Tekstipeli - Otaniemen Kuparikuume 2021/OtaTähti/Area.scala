package OtaTähti
import scala.collection.mutable.Map


class Area(val name: String, var description: String, var isAccesible : Boolean){


  // Maailman alustaminen
  private val neighbors = Map[String, Area]()
  private var accesibleNeighbors = Map[String, Area]() // Onko alueelle kulkulupa?
  private var visibleItemsOfTheArea = Map[String, Item]()
  private var hiddenItemsOfTheArea = Map[String, Item]()

  def hasClearence(pelaaja : Player) : Boolean = pelaaja.playerInventory.contains("matkakortti")

  def setNeighbor(direction: String, neighbor: Area) = {
    this.neighbors += direction -> neighbor
  }

  def neighbor(direction: String) = {
    this.accesibleNeighbors.get(direction)
  }

  def setNeighbors(exits: Vector[(String, Area)]) = {
    this.neighbors ++= exits
  }

  def setAccesibleNeighbors(pelaaja : Player) = {
    val naapurit = this.neighbors.toVector
    for (naapuri <- naapurit){
      if(naapuri._2.isAccesible){
          this.accesibleNeighbors(naapuri._1) = naapuri._2
      }
      else{
        if(hasClearence(pelaaja)){
          this.accesibleNeighbors(naapuri._1) = naapuri._2
        }
      }

    }

  }

  // Alueen toiminnallisuus

  def contains(itemName: String): Boolean = this.visibleItemsOfTheArea.contains(itemName)


  def containsHidden(itemname : String) = this.hiddenItemsOfTheArea.contains("Kupariaarre")


  private def addItems(item : Item, storage : Map[String, Item]) = storage(item.name) = item


  def addItem(item : Item) : Unit = this.addItems(item, this.visibleItemsOfTheArea)


  def addHiddenItem(item : Item) : Unit = this.addItems(item, this.hiddenItemsOfTheArea)


  private def removeItems(itemName : String, storage : Map[String, Item]) = {
    storage.get(itemName) match {
     case Some(item) => storage -= itemName
     Some(item)
     case None => None
  }
  }

  def removeHiddenItem(itemName : String) : Option[Item] = {
      removeItems(itemName, this.hiddenItemsOfTheArea)
   }


  def removeItem(itemName : String) : Option[Item] = {
      removeItems(itemName, this.visibleItemsOfTheArea)
   }



  private def itemDescription = {
    if (this.visibleItemsOfTheArea.nonEmpty){
      "\nNäet alueella seuraavat esineet: " + this.visibleItemsOfTheArea.keys.mkString(" ")
    }
    else
      ""
  }

  private val alkulausematkakorttilla = "Sinulla on matkakortti mukana. Täten pääset kulkemaan myös lähellä sijaitsevaan alueeseen nimeltä"
  private val alkulauseilman =  "Sinulla ei ole matkakorttia mukana. Tästä johtuen et pääse kulkemaan lähellä sijaitsevaan alueeseen nimeltä"



  private def extraAreaInfoKandidaattikeskus(hasMatkakortti: Boolean) : String = {
    if (hasMatkakortti)
      "\n\n" + alkulausematkakorttilla + " Kandidaattikeskus. Se sijaitsee ilmansuunnassa \"north\". Ja Ken tietää mitä hyödyllistä sieltä voi löytyä.\n"

    else
      "\n\n" + alkulauseilman + " Kandidaattikeksus. Se sijaitsee ilmansuunnassa \"north\". Voit kyllä löytää kulkuluvan jostain.\n"
    }



    private def extraAreaInfoOppimiskeskus(hasMatkakortti : Boolean, isAtAlvarinaukio : Boolean) : String = {
    if(hasMatkakortti) {
      if (isAtAlvarinaukio){
        "\n\n" + alkulausematkakorttilla + " Oppimiskeskus. Oppimiskeskus sijaitsee ilmansuunnassa \"south\".\n"
      }
       else
      "\n\n" + alkulausematkakorttilla + " Oppimiskeskus. Oppimiskeskus sijaitsee ilmansuunnassa \"east\".\n"

    } else
      if(isAtAlvarinaukio){
     "\n\n" + alkulauseilman + " Oppimiskeskus. Se sijaitsee ilmansuunnassa \"south\". Voit kyllä löytää kulkuluvan jostain.\n"
      }
      else "\n\n" + alkulauseilman + " Oppimiskeskus. Se sijaitsee ilmansuunnassa \"east\". Voit kyllä löytää kulkuluvan jostain.\n"
    }


  def fullDescription(pelaaja : Player) : String = {
    val exitList = "\n\nExits available: " + this.accesibleNeighbors.keys.mkString(" or ")
    if(this.name == "metroaseman edusta"){
      this.description + extraAreaInfoKandidaattikeskus(pelaaja.has("matkakortti")) + this.itemDescription + exitList
    }
    else if(this.name == "abloc") {
      this.description + extraAreaInfoOppimiskeskus(pelaaja.has("matkakortti"), false) + this.itemDescription + exitList
    }
    else if(this.name == "alvarinaukio") {
      this.description + extraAreaInfoOppimiskeskus(pelaaja.has("matkakortti"), true) + this.itemDescription + exitList
    }
    else
      this.description + this.itemDescription + exitList


  }



  override def toString = this.name + ": " + this.description.replaceAll("\n", " ").take(150)




}
