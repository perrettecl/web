package controllers;

import models.Capteur;
import models.Personne;
import models.Zone;
import play.mvc.*;

import java.util.*;

@With(Secure.class)
public class CZone extends Controller {
	static Personne current_user;
	
	@Before
	public static void setCurrentUser()
	{
		current_user = Personne.find("byEmail", session.get("username")).first();
	}

    public static void zones(String in) {   
    	long id;
		
		if(in == null){
			Zone z = Zone.getRacine();

			id = z.id;

		} else {
			id = Long.parseLong(in);
		}

		render("VZones/zones.html", id);
    }
    
    public static void getInfosZone(long id) {
		Zone zone = Zone.find("byId", id).first();

		if(zone == null) {
			render("VZones/zone_introuvable.html");
		} else {
			boolean isAdmin = current_user.isAdmin();
		//	boolean isResponsable = zone.verifResponsable(current_user);
			boolean isResponsable = true;
			render("VZones/fiche_zone.html", zone, isAdmin, isResponsable);
		}
	}

    public static void getZonesAdjacentes(long id) {
    	Zone zone = Zone.findById(id);
    	
    	if(zone != null) {
    		boolean isAdmin = current_user.isAdmin();
    		
    		render("VZones/navigation_zones.html", zone, isAdmin);
    	} else {
    		renderJSON("{\"erreur\" : \"true\", \"message\" : \"Zone introuvable\"}");
    	}
    }
    
    public static void creerZone(long idPere, String nom) {
    	if(current_user.isAdmin()) {
    		Zone zoneParente = Zone.findById(idPere);
    		
    		if(zoneParente != null) {
	    		if(nom != "") {
	    			Zone zone = new Zone(nom);
	    			zone.save();
	    			
	    			renderJSON("{\"erreur\" : \"false\"}");
	    		} else {
	    			renderJSON("{\"erreur\" : \"true\", \"message\" : \"Le nom de la zone ne peut pas être vide\"}");
	    		}
    		} else {
    			renderJSON("{\"erreur\" : \"true\", \"message\" : \"Zone parente introuvable\"}");
    		}
    	}
    }
    
    public static void renommerZone(long id, String newNom) {
    	if(current_user.isAdmin()) {
    		Zone zone = Zone.findById(id);
    		
    		if(zone != null) {
	    		if(newNom != "") {
	    			zone.setNom(newNom);
	    			zone.save();

	    			renderJSON("{\"erreur\" : \"false\"}");
	    		} else {
	    			renderJSON("{\"erreur\" : \"true\", \"message\" : \"Le nom de la zone ne peut pas être vide\"}");
	    		}
    		} else {
    			renderJSON("{\"erreur\" : \"true\", \"message\" : \"Zone introuvable\"}");
    		}
    	}
    }
    
    public static void ajouterAutorisationAcces(long idZone, long idPersonne) {
    	Zone zone = Zone.findById(idZone);
    	
    	if(zone != null) {
    		Personne user = Personne.findById(idPersonne);
    		
    		if(user != null) {
    			//if(zone.verifResponsable(current_user)) {
    			if(true) {
    				zone.addPersonneAutorise(user);
    			} else {
    				renderJSON("{\"erreur\" : \"true\", \"message\" : \"L'utilisateur n'est pas responsable de la zone\"}");
    			}
    		} else {
    			renderJSON("{\"erreur\" : \"true\", \"message\" : \"Utilisateur introuvable\"}");
    		}
    	} else {
    		renderJSON("{\"erreur\" : \"true\", \"message\" : \"Zone introuvable\"}");
    	}
    }
    
    public static void supprimerAutorisationAcces(long idZone, long idPersonne) {
    	Zone zone = Zone.findById(idZone);
    	
    	if(zone != null) {
    		Personne user = Personne.findById(idPersonne);
    		
    		if(user != null) {
    			//if(zone.verifResponsable(current_user)) {
    			if(true) {
    				zone.removePersonneAutorise(user);
    			} else {
    				renderJSON("{\"erreur\" : \"true\", \"message\" : \"Vous n'êtes pas responsable de la zone\"}");
    			}
    		} else {
    			renderJSON("{\"erreur\" : \"true\", \"message\" : \"Utilisateur introuvable\"}");
    		}
    	} else {
    		renderJSON("{\"erreur\" : \"true\", \"message\" : \"Zone introuvable\"}");
    	}
    }
    
    public static void ajouterResponsableZone(long idZone, long idPersonne) {
    	if(current_user.isAdmin()) {
    		Zone zone = Zone.findById(idZone);
        	
        	if(zone != null) {
        		Personne user = Personne.findById(idPersonne);
        		
        		if(user != null) {
        				zone.addResponsable(user);
        		} else {
        			renderJSON("{\"erreur\" : \"true\", \"message\" : \"Utilisateur introuvable\"}");
        		}
        	} else {
        		renderJSON("{\"erreur\" : \"true\", \"message\" : \"Zone introuvable\"}");
        	}
    	} else {
    		renderJSON("{\"erreur\" : \"true\", \"message\" : \"Action non autorisée\"}");
    	}
    }

    public static void supprimerResponsableZone(long idZone, long idPersonne) {
    	if(current_user.isAdmin()) {
    		Zone zone = Zone.findById(idZone);
        	
        	if(zone != null) {
        		Personne user = Personne.findById(idPersonne);
        		
        		if(user != null) {
        			zone.removeResponsable(user);
        		} else {
        			renderJSON("{\"erreur\" : \"true\", \"message\" : \"Utilisateur introuvable\"}");
        		}
        	} else {
        		renderJSON("{\"erreur\" : \"true\", \"message\" : \"Zone introuvable\"}");
        	}
    	} else {
    		renderJSON("{\"erreur\" : \"true\", \"message\" : \"Action non autorisée\"}");
    	}
    }
    
    public static void ajouterCapteurZone(long idZoneFrom, long idZoneTo) {
    	if(current_user.isAdmin()) {
    		Zone zoneFrom = Zone.findById(idZoneFrom);
    		Zone zoneTo = Zone.findById(idZoneTo);
    		
    		if(zoneFrom != null && zoneTo != null) {
    			Capteur capteur = new Capteur(zoneFrom, zoneTo);
    			capteur.save();
    		} else {
    			renderJSON("{\"erreur\" : \"true\", \"message\" : \"Zones introuvables\"}");
    		}
    	} else {
    		renderJSON("{\"erreur\" : \"true\", \"message\" : \"Action non autorisée\"}");
    	}
    }
    
    public static void rechercheNonResponsable(long idZone, String term) {
    	Zone zone = Zone.findById(idZone);
    	
    	if(zone == null){
    		renderJSON("{\"erreur\" : \"true\", \"message\" : \"Zones introuvables\"}");
    	}
    	List<Personne> non_responsables = zone.listePersonneNonResponsable(term);
    	renderJSON(non_responsables);
    }
    
    public static void rechercheNonAutorise(long idZone, String term) {
    	Zone zone = Zone.findById(idZone);
    	
    	if(zone == null){
    		renderJSON("{\"erreur\" : \"true\", \"message\" : \"Zones introuvables\"}");
    	}
    	
    	List<Personne> non_autorise = zone.listePersonneNonAutorise(term);
    	renderJSON(non_autorise);
    }
}