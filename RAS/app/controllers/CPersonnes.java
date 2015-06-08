package controllers;


import java.util.List;

import play.*;
import play.mvc.*;
import models.*;

public class CPersonnes extends Controller {
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
	
	public static void creerUtilisateur() {
		
	}
	
	public static void rechercheUtilisateur(String recherche) {
		List<Personne> liste = Personne.find("byNom", recherche).fetch();

		renderJSON((Object)liste);
	}
	
	public static void utilisateurs(long id) {
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
}
