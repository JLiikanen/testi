package OtaTähti

import scala.collection.mutable.Map
import scala.util.Random
import  o1._


class Player(startingArea: Area, game : Adventure) {

  private var hasUsedScooter = false
  private var currentLocation = startingArea        // gatherer: changes in relation to the previous location
  private var quitCommandGiven = false              // one-way flag
  private var carrying = Map[String, Item]()


  def inventory : String = {
    if (this.carrying.nonEmpty){
      "Sinulla on mukanasi: " + "\n" + this.carrying.keys.mkString("\n")
    }
    else
      "Sinulla ei ole mitään mukana."
  }


  def playerInventory = this.carrying

  def examine(itemName: String) : String = {
    if (this.carrying.contains(itemName)){
      "Tarkastelet esinettä: " + itemName + "." + "\n" + this.carrying.get(itemName).map(p => p.description).mkString("\n")
    }
    else{
      this.game.turnCount -= 1
      "Jos haluat tarkastella jotain niin sinun tulee ensin hankkia se!"
  }
  }

  def drop(itemName: String) : String = {
    this.carrying.get(itemName) match{
      case Some(item) => this.currentLocation.addItem(item)
                        this.carrying -= itemName
                        "Tiputit seuraavan esineen: " + itemName + "."
      case None =>
       this.game.turnCount -= 1
        "Sinulla ei ole tuota esinettä!"
    }
  }

  def get(itemName: String) : String= {
    if (itemName == "sähköpotkulauta") {
              "Et voi ottaa sähköpotkulautaa mukaasi."
            }
    else {
        this.currentLocation.removeItem(itemName) match {
          case Some(item) =>
              this.carrying(itemName) = item
              "Otat esineen " + itemName + " mukaasi."
          case None =>
             this.game.turnCount -= 1
            "Tuota esinettä " + itemName + " ei ole tällä alueella."
      }
    }

  }


  def has(itemName: String): Boolean = this.carrying.contains(itemName)



  def hasQuit = this.quitCommandGiven



  def location = this.currentLocation



  def go(direction: String) : String = {
    val destination = this.location.neighbor(direction)
    this.currentLocation = destination.getOrElse(this.currentLocation)
    if (destination.isDefined) {
        if(!this.game.locationsOfOtaniemi.contains(this.currentLocation.name)) { // LOCATIONS OF OTANIEMI EI SISÄLLÄ KANDI/OPPIMISKESKUSTA
          "You go " + direction + ". You are now walking... You enter the building through the mighty doors of " + this.currentLocation.name.capitalize + "."
        }
        else
          "You go " + direction + ". You are now walking..."
    }
     else{
      this.game.turnCount -= 1
      "You can't go " + direction + "."
  }
  }


  def quit() = {
    this.quitCommandGiven = true
    ""
  }

  def help() : String = {
   this.game.turnCount -= 1
   val raja           = "\n------------------\n"
   val otsikko        = "Otaniemen Kuparikuume 2021 pelin help - osio.\n\n"
   val pelaajanrooli  = "Olet etsijä, jonka tavoitteena on löytää Aalto Yliopiston kampusrakennuksista varastettua kuparitavaraa.\n-> Tässä asian uutisointia: HS 3.11.2021 \"Varkaat repivät röyhkein ottein arvometalleja irti Otaniemen historiallisista rakennuksista.\"\n\n"
   val pelintarina    = "Kuitenkin huhun mukaan osa varastetusta kuparista on edelleenkin Otaniemessä, hyvin piilotettuna tietysti.\n" +
                        "Sinun tehtäväsi on löytää varastettu kupari. Kupariaarre on piilotettu johonkin Otaniemen alueeseen, josta pelialue koostuu.\nKupariaarretta etsivät muutkin pelaajat, ja ensimmäinen, joka löytää kupariaarteen voittaa pelin saavuttaen huikeat määrät mainetta ja kunniaa. \n\n"
   val etsiminen      = "Kun pääset jollekin alueelle voit etsiä aarretta käyttäen komentoa \"Search\". Search komennolla todenmukaiseen lopputulokseen päätymisen todennäköisyys on 1/2.\nEsimerkiksi voit olla löytämättä aarretta alueelta, vaikka se oikeasti olisikin sinne piilotettu.\n" +
                        "Mikäli sinulla on metallinpaljastin voit käyttää komentoa \"use metallinpaljastin\", jolloin saat satavarman tiedon siitä, onko aarre kyseisellä alueella vai ei.\n\n"
   val liikkuminen    = "Liikkuaksesi pelikentällä sinun on käytettävä komentoa \"go ilmansuunnan nimi\". Käypiä ilmansuuntia ovat north, east, west, ja south.\n\n"
   val muutkomennot   = "Komento \"inventory\" näyttää hallussasi olevat esineet. Komennon \"examine esineen nimi\" avulla voit saada lisätietoja kyseisestä esineestä. Alueella olevia esineitä voit poimia komennolla \"get esineen nimi\".\nEsineitä voi vastaavasti tiputtaa komennolla \"drop esineen nimi\".\n\n"
   val lisaakomentoja = "Pelissä on myös kaksi suljettua aluetta, Kandidaattikeskus sekä Oppimiskeskus. Saat avattua alueet kun löydät pelikentältä matkakortin! Matkakortti toimii kulkulupana alueille.\n\n" + "Matkustamisen helpottamiseksi pelissä on myös sähköpotkulauta, jonka avulla voit matkustaa minne tahansa yhden vuoron aikana.\n\n" +
                        "Potkulautaa pystyy käyttämään vain kerran pelin aikana." + " Tietoa sähköpotkulaudan mahdollisista matkustusalueista ja niiden nimistä saat komennolla \"map\".\nMap komento avaa myös näytöllesi kartan pelialueesta, jonka voit sulkea milloin tahansa.\n\nPotkulaudalla matkustetaan käyttäen komentoa \"travel alueen nimi\".Esim. \"Travel rantasauna\"\n\n"
   val vihollinen     = "Pelissä on mukana myös kuparivaras. Kuparivaras on vihollinen, joka saattaa hyökätä kimppuusi tietyillä alueilla. Varkaan sijainti on satunnainen.\n" +
                        "Varkaan hyökätessä sinulla on mahdollisuus joko hyökätä varkaan kimppuun tai yrittää juosta pakoon.\n\n"
   val lopetus        = "Help - osio oli tässä. Pidä hauskaa pelin parissa ;)!"

   raja + otsikko + pelaajanrooli + pelintarina + etsiminen + liikkuminen + muutkomennot + lisaakomentoja + vihollinen + lopetus + raja

  }

  private def aarteenloytaminen : String = {
    this.currentLocation.removeHiddenItem("Kupariaarre") match {
          case Some(item) =>
                this.carrying("Kupariaarre") = item
                "Etsit läpikotaisin alueen " + this.currentLocation.name.capitalize + "..... Mutta mitäs täältä löytyykään..." +
                "Tuohan on se kupariaarre!! LÖYSIN KUPARIAARTEEN!!"
          case None => "Et löytänyt mitään! Alue voi olla oikeasti tyhjä, tai sitten et vain löytänyt kuparikätköä."
    }
  }

  def search : String = { // etsii kuparia
    val arvottunumero = Random.nextInt(2) // 1/2 todennäköisyys löytää aarre ilman metallinpaljastinta

    if(this.currentLocation.containsHidden("Kupariaarre")){
       if (arvottunumero == 1) {
         aarteenloytaminen
    }
       else "Et löytänyt mitään! Alue voi olla oikeasti tyhjä, tai sitten et vain löytänyt kuparikätköä."
    }
    else "Et löytänyt mitään! Alue voi olla oikeasti tyhjä, tai sitten et vain löytänyt kuparikätköä."
  }

  def useItem(itemName : String) = {
    if (itemName == "metallinpaljastin"){
      if(this.currentLocation.containsHidden("Kupariaarre")){
        aarteenloytaminen
      }
      else {
        "Alueella ei todellakaan ole kupariaarretta."
      }
    }
    else {
      this.game.turnCount -= 1
      "Sinulla ei ole esinettä " + itemName + " tai kyseisellä esineellä ei ole käyttötarkoitusta."
    }
  }


  def travel(matkakohde : String) = {
    if (!hasUsedScooter){
      if(this.currentLocation.contains("sähköpotkulauta")){
        this.game.areaSearcher(matkakohde) match {
          case Some(matkaalue) =>
               hasUsedScooter = true
               this.currentLocation.removeItem("sähköpotkulauta") match {
                 case Some(sahkopotkulauta) =>
                   matkaalue.addItem(sahkopotkulauta)
                   this.currentLocation = matkaalue
                   "Sähköpotkulauta toimii!! Hurraa! Olet nyt saapumassa alueelle " + matkaalue.name + ". Parkkeeraat sähköpotkulaudan ja jatkat etsintää."

              }
          case None => this.game.turnCount -= 1
            "Aluetta ei ole olemassa tai kyseiselle alueelle ei pääse sähköpotkulaudan kanssa. Kannattaa tarkistaa oikeinkirjoitus"
       }
      }
      else {
        this.game.turnCount -= 1
        "Ööö, oletko täysin järjissäsi?? Aluueella ei ole sähköpotkulautaa."
      }
    }
    else {
      this.game.turnCount -= 1
      "Sähköpotkulaudasta on akku loppunut. :("
    }

  }

  def mapOfOtaniemi = {
      this.game.turnCount -= 1
      val gui = new View("Map", terminateOnClose = false){
        def makePic = {
          Pic("C:\\Users\\Jacques Liikanen\\IdeaProjects\\scala harjoitus\\Tekstipeli3\\OtaTähti\\map.png")
        }
      }
     gui.start()


     "Sähköpotkulaudan kanssa voit päästä näille alueille:\n\n" + this.game.locationsOfOtaniemi.keys.map(_.capitalize).mkString("\n") + "\n\n" +
     "Pelialueeseen kuuluu myös seuraavat alueet mutta niille ei pääse sähköpotkulaudalla:\n" +
     "Kandidaattikeskus\nOppimiskeskus\n\n" +
     "Käyttääksesi sähköpotkulaudan Travel - ominaisuutta, sinun on kirjoittettava alueiden nimet aivan kuten yllä on kirjoitettu."
  }

  def fight : String = {
      if (this.game.kuparivaras.location.contains(this.game.player.location)){
        var oddsOfAttacking = Random.nextInt(3) // 1/3 tn, että hyökkäys onnistuu
        this.game.kuparivaras.conflict(oddsOfAttacking, true)
  }
      else "Hmm?? Oletkos hieman vainoharhainen? Ei kukaan ole hyökkäämässä sinua kohti."
  }

  def run = {
      if (this.game.kuparivaras.location.contains(this.game.player.location)){
          var oddsOfRunningAway = Random.nextInt(2) // 1/2 tn, että pääset pakoon
          this.game.kuparivaras.conflict(oddsOfRunningAway, false)
      }
      else "Hmm?? Oletkos hieman vainoharhainen? Ei kukaan ole hyökkäämässä sinua kohti."
  }


  override def toString = "Now at: " + this.location.name



}
