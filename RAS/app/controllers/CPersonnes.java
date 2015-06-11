package controllers;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import play.*;
import play.mvc.*;
import play.test.Fixtures;
import models.*;

@With(Secure.class)
public class CPersonnes extends Controller {
	static Personne current_user;
	
	@Before
	public static void setCurrentUser()
	{
		current_user = Personne.find("byEmail", session.get("username")).first();
	}

	 
	public static void test() {
		try {
			Personne une = new Personne("moi@test.fr", "Moi", "Moi", "0102030405");
			Personne deux = new Personne("toi@test.fr", "Toi", "Toi", "0102030406");
			
			une.save();
			deux.save();
		} catch (Exception e) {
			e.printStackTrace();
		}
		List<Personne> liste = Personne.findAll(); 
		String title = "Ceci est un test";
		
		render("VPersonnes/liste_personnes.html", liste, title);
	}
	
	public static void creerUtilisateur(String nom, String prenom, String email, String telephone, String adresse, String codePostal, String ville) throws Exception {
		validation.required(nom);
		validation.required(prenom);
		validation.required(email);
		validation.email(email);
		validation.required(telephone);
		validation.phone(telephone);
		validation.required(adresse);
		validation.required(codePostal);
		validation.match("code postal", codePostal, "$[0-9]{5}^");
		validation.required(ville);
		
		if(validation.hasErrors()) {
			;
			
			Map<String, HashSet<String>> messages_erreurs = new HashMap<String, HashSet<String>>();
			
			for(play.data.validation.Error error : validation.errors()) {
				if(messages_erreurs.get(error.getKey()) == null){
					messages_erreurs.put(error.getKey(), new HashSet<String>());
				}
				messages_erreurs.get(error.getKey()).add(error.message());
			}


			renderJSON(messages_erreurs);
		} else {
	    	 Personne nouvelUtilisateur = new Personne(email, nom, prenom, telephone, adresse, codePostal, ville);
	    	 nouvelUtilisateur.save();
	    	 
	    	 String message = "Utilisateur créé";
	    	 renderJSON((Object)message);
		}
	}
	
	public static void rechercheUtilisateur(String recherche) {
		List<Personne> liste = Personne.recherche(recherche);
		renderJSON((Object)liste);
	}
	
	public static void utilisateurs(String in) {
		long id;
		
		if(in == null){
			Personne p = Personne.find("byEmail", Security.connected()).first();

			id = p.id;

		} else {
			id = Long.parseLong(in);
		}
		render("VPersonnes/utilisateurs.html", id);
	}
	
	public static void nouvelUtilisateur() {			
		render("VPersonnes/nouvel_utilisateur.html");
	}
	
	public static void getInfosUtilisateur(long id) {
		Personne user = Personne.find("byId", id).first();

		if(user == null) {
			render("VPersonnes/utilisateur_introuvable.html");
		} else {			
			render("VPersonnes/fiche_utilisateur.html", user);
		}
	}
	
	public static void invaliderCarte(long id) {
		Carte c = Carte.find("byId", id).first();
		
		if(c == null) {
			renderJSON("{\"erreur\": \"true\"}");
		} else {
			c.invaliderCarte();
			renderJSON("{\"erreur\": \"false\"}");
		}
	}
}
