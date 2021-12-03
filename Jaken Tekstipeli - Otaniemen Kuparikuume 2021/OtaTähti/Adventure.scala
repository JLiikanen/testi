package OtaTähti

import scala.collection.mutable.Buffer
import scala.util.Random
import scala.collection.mutable.Map




class Adventure {

  private val abloc                    = new Area("abloc", "Olet Otaniemen kauppakeskus Ablocissa. Ympärilläsi vilisee ihmisiä. Tarkkasilmäisenä huomaat myös, että rakennuksessa on Alko.", true)
  private val metroasemanEdusta        = new Area("metroaseman edusta", "Olet metroaseman edessä. Raidejokeria rakennetaan.", true)
  private val kandidaattikeskus        = new Area("kandidaattikeskus", "Isossa rakennuksessa on vaikka kuinka paljon piiloja. Ja eihän kukaan uskoisi että kuparia olisi tänne piilotettu!", false)
  private val oppimiskeskus            = new Area("oppimiskeskus", "Kirjasto. Sieltäkin löytyy piilopaikkoja, ja eihän kukaan sieltä tajuaisi etsiä!", false)
  private val alvarinaukio             = new Area("alvarinaukio", "Kaunis nurmikkoalue. Syksyinen tuuli puhaltaa.", true)
  private val puistikko                = new Area("puistikko", "Puita. Risuja. ", true)
  private val otaniemenUrheilupuisto   = new Area("otaniemen urheilupuisto", "Nurmikkokenttä ja juoksurata. Kentän laidoilta löytyy jemma jos toinenkin.", true)
  private val otasatama                = new Area("otasatama", "Metsikköä. Vajoja. Veneitä. Ja tietysti Piiloja. ", true)
  private val otaranta                 = new Area("otaranta", "Tuuli sen kuin vain puhaltaa. ", true)
  private val rantasauna               = new Area("rantasauna", "Paikalla on muitakin etsijöitä toimi ripästi! Ei ole aikaa saunomiselle!", true)
  private val smokki                   = new Area("servin mökin alue", "Skatto? Skeittö? Smökki?", true)
  private val otaniemenKappelinAlue    = new Area("otaniemen kappelin alue", "Saavut Otaniemen Kappelin alueelle. Ympäröivä metsikkö sekä kappeli tarjoavat ensiluokkaisia piiloja.", true)
  private val ossinlammenAlue          = new Area("ossinlammen alue", "Päätit tulla Ossinlammen alueelle. Alueella on viljelypalstoja, skeittiramppi, ja tietysti lampi. Tutki nyt paikka läpikotaisin!", true)
  private val miinusmaanKyykkäKenttä   = new Area("miinusmaan kyykkäkenttä", "Onpa negatiivista porukkaa täällä. ", true)
  private val martinniemenAlue         = new Area("martinniemen alue", "Alue koostuu poluista, tiheästä metsiköstä ja rantakaislikosta.", true)

    abloc.setNeighbors(Vector("north" -> metroasemanEdusta, "east" -> oppimiskeskus                                                                   ))
    metroasemanEdusta.setNeighbors(Vector("north" -> kandidaattikeskus, "east" -> alvarinaukio, "south" -> abloc                                      ))
    kandidaattikeskus.setNeighbors(Vector("south" -> metroasemanEdusta                                                                                ))
    oppimiskeskus.setNeighbors(Vector("north" -> alvarinaukio, "west" -> abloc                                                                        ))
    alvarinaukio.setNeighbors(Vector("north" -> puistikko, "east" ->  otaniemenUrheilupuisto, "west" -> metroasemanEdusta, "south" -> oppimiskeskus   ))
    puistikko.setNeighbors(Vector("north" -> ossinlammenAlue, "south" -> alvarinaukio                                                                 ))
    otaniemenUrheilupuisto.setNeighbors(Vector("north" -> otasatama, "west" -> alvarinaukio, "south" -> otaranta                                      ))
    otasatama.setNeighbors(Vector("north" -> rantasauna,"south" -> otaniemenUrheilupuisto                                                             ))
    otaranta.setNeighbors(Vector("north" -> otaniemenUrheilupuisto                                                                                    ))
    rantasauna.setNeighbors(Vector("west" -> smokki, "south" -> otasatama                                                                             ))
    smokki.setNeighbors(Vector("north" -> otaniemenKappelinAlue, "east" ->  rantasauna, "west" -> ossinlammenAlue                                     ))
    otaniemenKappelinAlue.setNeighbors(Vector("south" -> smokki                                                                                       ))
    ossinlammenAlue.setNeighbors(Vector("east" ->  smokki, "west" -> miinusmaanKyykkäKenttä, "south" -> puistikko                                     ))
    miinusmaanKyykkäKenttä.setNeighbors(Vector("north" -> martinniemenAlue, "east" ->  ossinlammenAlue                                                ))
    martinniemenAlue.setNeighbors(Vector("south" -> miinusmaanKyykkäKenttä                                                                            ))


  val player = new Player(abloc, this)

  // KAKKIALUEET VEKTORI ON JÄRJESTELTY NIIN JOTTA GIVEHINT METODI ANTAA JÄRKEVIÄ PALAUTUKSIA - JA SE SISÄLTÄÄ KAIKKI ALUEET
  val kaikkialueet = Vector[Area](abloc, metroasemanEdusta, kandidaattikeskus, oppimiskeskus, alvarinaukio, puistikko, otaniemenUrheilupuisto, otaranta, otasatama, rantasauna, smokki, otaniemenKappelinAlue, ossinlammenAlue, miinusmaanKyykkäKenttä, martinniemenAlue)

  // KANDI/OPPIMISKESKUS EIVÄT KUULU SALLITTUIHIN ALUEISIIN
  val sallitutalueet = kaikkialueet.filter(p => p != oppimiskeskus && p != kandidaattikeskus)

  // METROASEMAN EDUSTA & ABLOC EIVÄT OLE SALLITTUJA PIILOPAIKKOJA KUPARILLE
  val potentialHidingSpots = kaikkialueet.filter(p => p != metroasemanEdusta && p != abloc)


  private val esineet = Vector[Item](new Item("matkakortti", "Hmm, HSL matkakortti... Tätähän voi käyttää kulkulupana yliopisto rakennuksiin!"), new Item("Kupariaarre", "Tämähän on peräisin Aalto-Yliopiston kampusrakennuksista! Onneksi olkoon olet voittanut pelin!"), new Item("sähköpotkulauta", "Muistan lukeneeni, että tällä kulkuvälineellä voin päästä yhden vuoron aikana minne tahansa."))
  var locationsOfOtaniemi = Map[String, Area]()



  private def setScooterTravelLocations() = { // kandi & oppimiskeksus ei ole sallittuja matkustamisalueita
    for (alue <- sallitutalueet){
      locationsOfOtaniemi(alue.name) = alue
    }
  }

  private def itemHider() = { // Piilottaa esineet sattumanvaraisiin paikkoihin
      for (item <- esineet){
        if (item.name == "Kupariaarre"){
          var randomIndex = Random.nextInt(potentialHidingSpots.size)  // METROASEMAN EDUSTAA JA ABLOCIA EI HYVÄKSYTÄ PIILOPAIKOIKSI
          potentialHidingSpots(randomIndex).addHiddenItem(item)
        }
        else{
        var randomIndex = Random.nextInt(sallitutalueet.size) // MATKAKORTTI SEKÄ SÄHKÖPOTKULAUTA EIVÄT SAA OLLA KANDI - TAI OPPIMISKESKUKSESSA
        sallitutalueet(randomIndex).addItem(item)
        }
    }
  }

  // MAAILAMAN ALUSTAMISTA

  this.itemHider()
  this.setScooterTravelLocations()

  kandidaattikeskus.addItem(new Item("metallinpaljastin", "Tämä on metallinpaljastin, se nostaa todennäköisyyksiäsi löytää yliopistolta varastetut kuparit!"))


  private val randomlocation = sallitutalueet.filter(_ != abloc)(Random.nextInt(sallitutalueet.size - 1)) // varas ei saa olla ablocissa, kandikeskuksessa tai oppimiskeskuksessa.

  val kuparivaras = new Varas("kuparivaras", "Huomaat epäilyttävän miehen juoksevan aggressiivisesti sinua kohti... MITÄ V**TUA!?", Some(randomlocation), this)


  // Tästä edespäin vain hyödyllisiä metodeja

  // pelaajalle sallitut liikkumsalueet
  def checkNeighbors() ={
  for (alue <- kaikkialueet){
    alue.setAccesibleNeighbors(player)
   }
  }


  // apumetodi playerin travel metodiin.
  def areaSearcher(hakusana: String) = {
    this.locationsOfOtaniemi.get(hakusana)
  }


  def giveHint : String = {
    var oddsOfTruth = Random.nextInt(2)
    val correctFormOfHidingSpots = potentialHidingSpots.sliding(2).toVector
    var palautus = ""
    if (oddsOfTruth == 1){ // varas puhuu totta
      for (alue <- correctFormOfHidingSpots){
          if (alue(0).containsHidden("Kupariaarre") || alue(1).containsHidden("Kupariaarre")){
            if (alue(0) == kandidaattikeskus || alue(1) == oppimiskeskus){
              return "..... Selvä, joku sisätila se muistaakseni oli, en muista tarkemmin rakennuksen nimeä.\n\n"
            }
            palautus =  "..... Selvä, kupariaarre on joko alueella " + alue(0).name.capitalize + " tai alueella " + alue(1).name.capitalize + ". En muista tarkemmin, oli hyvin piemeää kun piilotimme kuparit... Ei ollu mun tehtävä pitää paikkaa mielessä.\n\n"
          }
      }
      palautus
    }
    else{ //varas valehtelee
      val possibleAnswers = potentialHidingSpots.filterNot(p => p.containsHidden("Kupariaarre")).sliding(2).toVector
      val randomIndex = Random.nextInt(possibleAnswers.size)
      if (possibleAnswers(randomIndex)(0) == kandidaattikeskus || possibleAnswers(randomIndex)(0) == oppimiskeskus || possibleAnswers(randomIndex)(1) == kandidaattikeskus ||  possibleAnswers(randomIndex)(1) == oppimiskeskus){
              "..... Selvä, joku sisätila se muistaakseni oli, en muista tarkemmin rakennuksen nimeä.\n\n"
      }
      else {
        ".... Selvä, kupariaarre on joko alueella " + possibleAnswers(randomIndex)(0).name.capitalize + " tai alueella " + possibleAnswers(randomIndex)(1).name.capitalize +
        ". En muista tarkemmin, oli hyvin piemeää kun piilotimme kuparit... Ei ollu mun tehtävä pitää paikkaa mielessä.\n\n"
      }
      }
  }

  var turnCount = 0


  val timeLimit = 15 + Random.nextInt(10) // Aikaraja riippuu muista pelaajista ja se vaihtelee joka kierros.


  def isComplete = this.player.has("Kupariaarre")


  def isOver = this.isComplete || this.player.hasQuit || this.turnCount == this.timeLimit


  def welcomeMessage = {
    "\nParhaimman mahdollisen pelikokemuksen saamiseksi suosittelemme, että laitatte seuraavan musiikin pyörimään: https://youtu.be/7JqKRqOmzi0?t=546\n\n" +
    "\n(**Metroääniä**)\n" + "*********************\n\n" + "Aalto Yliopisto\nAalto Universitetet\nAalto University\n\n" + "(**Kävelet liukuportaisiin**)\n\n" + "OTANIEMEN\n\n" + "KUPARIKUUME\n\n" + "2021\n\n" +
    "Tervetuloa pelin pariin arvoisa seikkailija!\n\n" + "Otaniemen Kuparikuume pohjautuu seuraaviin tositapahtumiin……\n" + "“Satoja metrejä kuparisäleikköä katosi kuin tuhka tuuleen: Varkaat repivät röyhkein ottein arvometalleja irti Otaniemen historiallisista rakennuksista.\"\n" +
    " - HS 3.11.2021\n\n" + "Kuitenkin huhun mukaan osa kuparista on edelleen Otaniemessä! Hyvin piilotettuna tosin.\n\n" + "Tämä huhu on saanut aikaan historiallisesti merkittävän jahdin Otaniemessä, jota ei hetkeen tulla unohtamaan...\n\n" +
    "\"Ei ole saatu kuparia takaisin, ennemminkin sitä on viety lisää\", kertoo Aalto-yliopistokiinteistöjen toimitilapäällikkö" +
    "\nNyt tuo kupari haetaan takaisin.\n\n" + "Sinun ja muiden pelaajien tehtävänä on löytää varkaiden piilottamat kuparit.\n" +
    "Kuparit ovat piilotettu yhteen pelialueeseen, ja sinun on löydettävä tuo kuparikätkö.\n\n" +
    "Pelin voittaa se pelaaja kuka löytää kätkön ensimmäisenä, joten sinun on pidettävä kiirettä, sillä kilpailet maailmanluokan aarteenetsijöitä vastaan!\n\nPelin voittamiseen riittää se, että saat kuparit ensimmäisenä haltuusi.\n\n" + "Häviät, jos et löydä kuparikätköä ennen toisia pelaajia.\n\n" +
    "Pelialueiden välillä liikutaan käyttäen komentoa \"go ilmansuunnan nimi\". Käypiä ilmansuuntia ovat north, east, west, south. Esim. \"go north\"\n\n" +
    "Kun pääset jollekin alueelle voit etsiä kuparikätköä käyttäen komentoa \"search\".\n" +
    "Search komennolla todenmukaiseen lopputulokseen päättymisen todennäköisyys on 1/2, mikä siis tarkoittaa sitä, että voit olla löytämättä aarretta alueelta, vaikka se oikeasti olisikin sinne piilotettu.\n\n" +
    "Tämän vuoksi sinun kannattaa etsiä käsiisi pelialueelta löytyvä metallinpaljastin, jonka avulla saat satavarman tiedon siitä, onko aarre tietyllä alueella vai ei.\n" +
    "Metallinpaljastimen kanssa kuparin etsiminen tapahtuu komennolla \"use metallinpaljastin\".\n\n" +
    "Aalto Yliopisto on myös pyrkinyt suojelemaan kampusrakennuksia entistä enemmän, ja tästä johtuen Oppimiskeskukseen ja Kandidaattikeskukseen päästäkseen on oltava kulkulupa. Kulkulupana toimii hsl matkakortti.\n" +
    "Matkakortin voi löytää pelikentältä.\n\n" +
    "Aluekohtainen infolaatikko ilmoittaa sinulle onko alueella matkakorttia, metallinpaljastinta tai sähköpotkulautaa.\n" +
    "Saatuasi matkakortin haltuusi suljetut alueet tulevat automaattisesti käytettäviksi.\n\n" +
    "Infolaatikko ei kuitenkaan kerro mitään siitä onko alueella kuparikätköä.\n\n" +
    "Muita hyödyllisiä komentoja:\n\n" + "Komento \"inventory\" näyttää hallussasi olevat esineet\n" + "Komennon \"examine esineen nimi\" avulla voit saada lisätietoja kyseisestä esineestä.\n" + "Alueella olevia esineitä voit poimia komennolla \"get esineen nimi\".\n" +
    "Esineitä voi vastaavasti tiputtaa komennolla \"drop esineen nimi\".\n\n" + "Matkustamisen helpottamiseksi pelissä on myös sähköpotkulauta, jonka avulla voit matkustaa minne tahansa yhden vuoron aikana.\n\nPotkulautaa pystyy käyttämään vain kerran pelin aikana. " +
    "Tietoa sähköpotkulaudan mahdollisista matkustusalueista ja niiden nimistä saat komennolla \"map\".\nMap komento avaa myös näytöllesi kartan pelialueesta, jonka voit sulkea milloin haluat.\n\nPotkulaudalla matkustetaan käyttäen komentoa \"Travel alueen nimi\". Esim. \"Travel rantasauna\"\n\n" +
    "Mikäli tarvitset pelin aikana apua pelin pelaamiseen käytä komentoa \"help\".\n\n" +
    "Ja nyt…\n\n" + "Valmistaudu pelaamaan tätä adrenaliinin täytteistä peliä.\n\n" + "1…\n" + "2…\n" + "3…\n\n" + "**Liukuportaat päättyvät**\n\n" +
    "Saavut nyt Kauppakeskus Ablociin. Verryttelet hieman tulevaa jahtia ajatellen.\n\n" +
    "(Musiikin voi tässä välissä pysäyttää, jos niin haluat.)\n\n"


  }

  val endingText = ".... Ja olet siis pelin voittaja! Olit nopeampi ja parempi kuin muut etsijät!\n\n" +
                  "Ja näin jännittävä jahti on saatu päätökseen... Sinun toimestasi!\n" +
                  "Kuparit ovat nyt palautuneet oikealle omistajalleen, Aalto-Yliopistolle. Hetken jo luuulimme, että kuparit ovat kadonneet ikiajoiksi.\n\n" +
                  "Kiitoksia satelee nyt sunnasta kuin toisesta:\n\n" +
                  "Aalto-Yliopiston ylin johto kiittää sinua. Teetämme sinulle oman haalarimerkin :)\n\n" +
                  "Otaniemen poliismestari: Ensiluokkaista etsivän työtä. Olet miltei yhtä hyvä etsivä kuin minä. Meiltä löytyisi töitä sun kaltasille kavereille.\n\n" +
                  "Toinen etsijä: Onnea. Tämä on suoritus josta voi olla ylpeä."

  def goodbyeMessage = {
    if (this.isComplete) {
      Thread.sleep(3000)
       if(givenCommands.last == "search" && givenCommands.contains("fight")){
         endingText + " Kuulin kans et hyökkäsit ihan kuparivarkaan kimppuun, ja löysit kuparit vieläpä ihan ilman apuvälineitä! Olet selvästi Otaniemen kuparikuningas!\n\n" + "***PELI PÄÄTTYY**"
       }
       else if(givenCommands.contains("fight")){
         endingText + " Kuulin kans et hyökkäsit ihan kuparivarkaan kimppuun, oot rohkea kaveri. Taidat olla Otaniemen kuparikuningas!" + "\n\n***PELI PÄÄTTYY**"
       }
       else {
       endingText + "\n\n***PELI PÄÄTTYY**"

       }
    } else if (this.turnCount == this.timeLimit)
      "Voi V***U... Joku muu on löytänyt kuparikätkön ennen sinua! Et saavuttanut mainetta ja kunniaa tällä kertaa :/"
    else  // game over due to player quitting
      "Häh? TÄH!?!? No... selvä. Peli päättyy sinun osaltasi :("
  }


  private var givenCommands = Buffer[String]()

  def playTurn(command: String) = {
    val action = new Action(command)
    val outcomeReport = action.execute(this.player)
    if (outcomeReport.isDefined) {
      this.turnCount += 1
      givenCommands += command.toLowerCase()
    }
    outcomeReport.getOrElse("Unknown command: \"" + command + "\".")
  }

  def conflictPlayTurn(command: String) = {
    val action = new ConflictAction(command)
    val outcomeReport = action.execute(this.player)
    if (outcomeReport.isDefined) {
      givenCommands += command.toLowerCase()
    }
    outcomeReport.getOrElse("Unknown command: \"" + command + "\".")
  }








}
