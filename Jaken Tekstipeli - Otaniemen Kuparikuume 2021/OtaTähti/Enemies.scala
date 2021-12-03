package OtaTähti

import scala.util.Random

abstract class Enemies(name : String, description : String, location : Option[Area], game : Adventure) {

  val oddsOfCausingDamage : Int

  def conflict(oddsOfAction: Int, decidedToAttack: Boolean) : String

  val penalty : Int


}

class Varas(val name : String, val description : String, var location : Option[Area], val game : Adventure) extends Enemies(name, description, location, game){

  val oddsOfCausingDamage =  0 // todennäköisyys aiheuttaa haittaa pelaajalle, varkaan tapauksessa tämä todennäköisyys määritetään jo player luokan fight tai run metodissa

  val penalty = 2 // Montako vuoroa menetät jos varas voittaa sinut

  def conflict(oddsOfAction: Int, decidedToAttack: Boolean) = {
    if (decidedToAttack){
    if (oddsOfAction == 1){
      this.game.turnCount += (penalty)
      this.location = None
      "\nHyökkäät urheasti varkaan kimppuun! Varas potkaisee sinua, mutta potku ei satuta sinua juurikaan, sillä olet kova kuin kivi.\n\n" +
      "Koitat lyödä miestä, mutta mies onnistuu väistämään iskun! Mies hyödyntää tilanteen ja lyö sinulta tajun kankaalle!\n\n" +
      "Pikku hiljaa heräilet ja toivut kivusta, kaksi pelivuoroa meni ohi sillä aikaa kun sinulla oli taju kankaalla.\n\n" +
      "Huh, mikä hullu... Tuo mies oli varmaan yksi niistä kuparivarkaista, ja näin he pyrkivät häiritsemään meitä etsijöitä. " +
      "Noh, parempi jatkaa etsintöjä välittömästi."
    }
    else {
       this.location = None
      "\nHyökkäät urheasti varkaan kimppuun! Varas potkaisee sinua, mutta potku ei satuta sinua juurikaan, sillä olet kova kuin kivi.\nKoitat lyödä miestä, ja nyrkkisi osuu kohteeseen.\n" +
      "Mies kaatuu maahan vaikeroiden. Nyt sinulla oiva tilaisuus kuulustella miestä.\n\n" +
      "Sinä: **Uhkaavalla äänensävyllä** OLETKO YKSI KUPARIVARKAISTA??\n\n" +
      "Kuparivaras: Joo-o. Kyllähä mä...\n\n" +
      "Sinä: No minne olette piilottaneet ne kuparit? HÄ?!\n\n" +
      "Kuparivaras: " + this.game.giveHint +
      "Sinä: Ok, tän on parempi pitää paikkansa. Soitan nyt poliiseille, ne saa luvan tulla hakemaan sut.\n\n" +
      "Soittaessasi poliisille, keskityt soiton suorittamiseen, ja samalla mies ponnistaa maasta tönästen sinua samalla. Mies ampaisee samantien karkuun.\n\n" +
      "Sinä: Voi vi**tu. Emmä tota kaveria enää tuu saamaan kiinni.\n\n" +
      "Noh, parempi jatkaa etsintöjä välittömästi.\n\n"
    }
    }

    else { // pakoon lähtemine
      if (oddsOfAction == 1){
        this.location = None
        this.game.turnCount += (penalty)
        "\nLähdet välittömästi pinkomaan toiseen suuntaan. Hetken juostuasi huomaat että mies jahtaa sinua edelleen.\n\nMies saakin sinusta nipin napin otteen, ja lyö sinulta tajun kankaalle...\n\n" +
        "Pikku hiljaa heräilet ja toivut kivusta, kaksi pelivuoroa meni ohi sillä aikaa kun sinulla oli taju kankaalla.\n\n" +
        "Huh, mikä hullu... Tuo mies oli varmaan yksi niistä kuparivarkaista, ja näin he pyrkivät häiritsemään meitä etsijöitä. " +
        "Noh, parempi jatkaa etsintöjä välittömästi."
      }
      else {
         this.location = None
        "\nLähdet välittömästi pinkomaan toiseen suuntaan. Onnistut löytämään piilon, ja päätät piiloutua. Piilostasi näet kuinka mies estii sinua.\n\n" +
        "Olet peloissasi, ja toivot vain, että hän ei löytäsisi sinua.\n\n" +
        "Onneksesi mies näyttää vihdoin luovuttavan, ja hän lähtee matkoihinsa. Uskallat nyt tulla esiin piilostasi.\n\n" +
        "Huh, mikä hullu... Tuo mies oli varmaan yksi niistä kuparivarkaista, ja näin he pyrkivät häiritsemään meitä etsijöitä.\n\n" +
        "Noh, parempi jatkaa etsintöjä välittömästi."
      }

    }

  }





}

