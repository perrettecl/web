package controllers;

import java.util.Date;
import java.util.List;

import models.Evenement;
import play.mvc.*;

public class RSS extends Controller {

    public static void evenements() {
    	List<Evenement> evenements = Evenement.get100DernierEvenements();
    	
    	if(evenements.size() > 0){
    		Date date = evenements.get(0).getDate();
    		render("VEvenement/evenementRSS.xml",evenements,date);
    	}
    }

}
