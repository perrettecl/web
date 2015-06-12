package controllers;

import models.Capteur;
import models.Carte;
import models.Zone;
import play.mvc.*;


public class CTestAutorisation extends Controller {

    public static void index() {
        render("VTestAutorisation/testAutorisation.html");
    }
    
    public static void verifierAutorisation(String numeroCarte, long idCapteur) {
    	Carte carte = Carte.find("byNumero", numeroCarte).first();
    	Capteur capteur = Capteur.findById(idCapteur);
    	
    	if(carte != null && capteur != null) {
    		if(Zone.verifAutorisation(carte, capteur)) {
    			renderJSON("{\"erreur\" : \"false\"}");
    		} else {
    			renderJSON("{\"erreur\" : \"true\", \"message\" : \"Accès refusé\"}");
    		}
    	} else {
    		renderJSON("{\"erreur\" : \"true\", \"message\" : \"Carte ou capteur inexistant\"}");
    	}
    }

}
