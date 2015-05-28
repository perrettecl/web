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
}
