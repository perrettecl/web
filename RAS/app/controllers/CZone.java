package controllers;

import models.Capteur;
import models.Carte;
import models.Personne;
import models.Zone;
import tools.Utils;
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
			boolean isResponsable = zone.verifResponsable(current_user);
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
	    		if(!nom.equals("")) {
	    			
	    			Zone zone = new Zone(nom,zoneParente);
	    			
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
	    		if(!newNom.equals("")) {
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
    			if(zone.verifResponsable(current_user)) {
    				if(!zone.verifAutorise(user)) {
    					zone.addPersonneAutorise(user);
    					renderJSON("{\"erreur\" : \"false\"}");
    				} else {
    					renderJSON("{\"erreur\" : \"true\", \"message\" : \"Utilisateur déjà autorisé dans la zone\"}");
    				}
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
    			if(zone.verifResponsable(current_user)) {
    				zone.removePersonneAutorise(user);
    				renderJSON("{\"erreur\" : \"false\"}");
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
        			if(!zone.verifResponsable(user)) {
        				zone.addResponsable(user);
        				renderJSON("{\"erreur\" : \"false\"}");
        			} else {
        				renderJSON("{\"erreur\" : \"true\", \"message\" : \"Utilisateur déjà responsable de la zone\"}");
        			}
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
        			renderJSON("{\"erreur\" : \"false\"}");
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
    			renderJSON("{\"erreur\" : \"false\"}");
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
		for(Personne p : non_responsables)
    	{
    		p.setZonesAutorise(null);
    		p.setZonesResponsable(null);
    		p.setCartes(null);
    	}
    	renderJSON(Utils.rechercheToJSONforAutoCompletPersonne(non_responsables));
    }
    
    public static void rechercheNonAutorise(long idZone, String term) {
    	Zone zone = Zone.findById(idZone);
    	
    	if(zone == null){
    		renderJSON("{\"erreur\" : \"true\", \"message\" : \"Zones introuvables\"}");
    	}
    	
    	List<Personne> non_autorise = zone.listePersonneNonAutorise(term);
		for(Personne p : non_autorise)
    	{
    		p.setZonesAutorise(null);
    		p.setZonesResponsable(null);
    	}
    	renderJSON(Utils.rechercheToJSONforAutoCompletPersonne(non_autorise));
    }
    
    public static void rechercheAutresZone(long idZone, String term) {
    	Zone zone = Zone.findById(idZone);
    	
    	if(zone == null){
    		renderJSON("{\"erreur\" : \"true\", \"message\" : \"Zones introuvables\"}");
    	}
    	
    	List<Zone> liste_zone = zone.listeAutresZone(term);
		for(Zone z : liste_zone)
    	{
    		z.setPeres(null);
    		z.setFils(null);
    		z.setResponsables(null);
    		z.setPersonnesAutorise(null);
    	}
    	renderJSON(Utils.rechercheToJSONforAutoCompletZone(liste_zone));
    }
}
