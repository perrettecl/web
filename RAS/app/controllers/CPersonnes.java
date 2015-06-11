package controllers;


import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;




import org.joda.time.DateTime;

import com.google.gson.JsonElement;

import play.*;
import play.libs.WS;
import play.libs.WS.HttpResponse;
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
	
	public static void resetMotDePasse(long id) {
		Personne user = Personne.findById(id);
		
		if(user != null) {
			user.resetMotDePasse();
			user.save();
			
			renderJSON("{\"erreur\" : \"false\"}");
		} else {
			renderJSON("{\"erreur\" : \"true\"}");
		}
	}
	
	public static void modifierUtilisateur(long id, String nom, String prenom, String telephone, String adresse, String codePostal, String ville) throws Exception {
		validation.required(nom);
		validation.required("prénom", prenom);
		validation.required("téléphone", telephone);
		validation.phone("téléphone", telephone);
		validation.required(adresse);
		validation.required("code postal", codePostal);
		validation.match("code postal", codePostal, "^[0-9]{5}$");
		validation.required(ville);
		
		if(validation.hasErrors()) {

			Map<String,String> messages_erreurs = new HashMap<String, String>();
			
			for(play.data.validation.Error error : validation.errors()) {
				messages_erreurs.put(error.getKey(), error.message());
			}
			
			renderJSON(messages_erreurs);
		}
		else if(!valideVilleCP(ville,codePostal)){
			Map<String,String> messages_erreurs = new HashMap<String, String>();

			messages_erreurs.put("code postal","La ville et le code postal ne correspondent pas");
			
			renderJSON(messages_erreurs);
		} else {
	    	 Personne user = Personne.findById(id);
	    	 
	    	 user.setNom(nom);
	    	 user.setPrenom(prenom);
	    	 user.setTelephone(telephone);
	    	 user.setAdresse(adresse);
	    	 user.setCodePostal(codePostal);
	    	 user.setVille(ville);
	    	 
	    	 user.save();
	    	 
	    	 renderJSON("{\"erreur\" : \"false\"}");
		}
	}
	
	public static void creerUtilisateur(String nom, String prenom, String email, String telephone, String adresse, String codePostal, String ville) throws Exception {
		validation.required(nom);
		validation.required("prénom", prenom);
		validation.required(email);
		validation.email(email);
		
		if(Personne.find("byEmail", email).first() != null) {
			validation.addError("email", "L'adresse email existe déja");
	    }
		
		validation.required("téléphone", telephone);
		validation.phone("téléphone", telephone);
		validation.required(adresse);
		validation.required("code postal", codePostal);
		validation.match("code postal", codePostal, "^[0-9]{5}$");
		validation.required(ville);
		
		if(validation.hasErrors()) {
			Map<String,String> messages_erreurs = new HashMap<String,String>();
			
			for(play.data.validation.Error error : validation.errors()) {
				messages_erreurs.put(error.getKey(), error.message());
			}
			
			messages_erreurs.put("erreur", "true");
			
			renderJSON(messages_erreurs);
		}else if(!valideVilleCP(ville,codePostal)){
			Map<String,String> messages_erreurs = new HashMap<String, String>();

			messages_erreurs.put("codePostal","La ville et le code postal ne corresponde pas");
			
			renderJSON(messages_erreurs);
		}else {
	    	 Personne nouvelUtilisateur = new Personne(email, nom, prenom, telephone, adresse, codePostal, ville);
	    	 nouvelUtilisateur.save();
	    	 
	    	 renderJSON("{\"erreur\" : \"false\", \"id\" : \"" + nouvelUtilisateur.id + "\"}");
		}
	}
	
	public static void ajouterCarte(long id, String numeroCarte) {
		Personne user = Personne.findById(id);
		
		if(user != null) {
			
			if(Carte.find("byNumero", numeroCarte).first() != "null") {
				DateTime dateJour = new DateTime();
				DateTime dateFin = dateJour.plusYears(2);
				
				Carte carte = new Carte(numeroCarte, dateJour.toDate(), dateFin.toDate());

				user.addCarte(carte);
				
				user.save();
				
				renderJSON("{\"erreur\" : \"false\"}");
			} else {
				renderJSON("{\"erreur\" : \"true\"}, \"message\" : \"Ce numéro de carte existe déjà\"");
			}
		} else {
			renderJSON("{\"erreur\" : \"true\"}, \"message\" : \"Utilisateur introuvable\"");
		}
	}
	
	public static void modifierMotDePasse(long id, String newMdp, String newMdpConf) {
		Personne user = Personne.findById(id);
		
		if(user != null) {
			if(newMdp == newMdpConf) {
				user.setMotDePasse(newMdp);
				user.save();
			} else {
				renderJSON("{\"erreur\" : \"true\"}, \"message\" : \"Le mot de passe et sa confirmation sont différents\"");
			}
		} else {
			renderJSON("{\"erreur\" : \"true\"}, \"message\" : \"Utilisateur introuvable\"");
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

		for(Carte carte : user.getCartes()) {
			System.out.println(carte.getNumero() + carte.getDateCreation());
		}
		
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
	
	private static boolean valideVilleCP(String ville, String cp)
	{
		HttpResponse res = WS.url("http://www.cp-ville.com/cpcom.php?cpcommune="+ville).get();
		int statut = res.getStatus();
		
		if(statut == 200){
			System.out.println(""+statut);
			String json = res.getString().split("\n")[1];
			return json.matches(".*"+cp+".*");
		}
		
		return true;
	}
}
