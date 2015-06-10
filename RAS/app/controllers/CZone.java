package controllers;

import models.Personne;
import models.Zone;
import play.mvc.*;

public class CZone extends Controller {

    public static void index() {
        render();
    }

    public static void zones(long id) {
		render("VZones/zones.html", id);
    }
    
    public static void getInfosZone(long id) {
		Zone zone = Zone.find("byId", id).first();

		if(zone == null) {
			render("VZones/zone_introuvable.html");
		} else {			
			render("VZones/fiche_zone.html", zone);
		}
	}
}
