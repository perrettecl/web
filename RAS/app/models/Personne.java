package models;

import play.*;

import java.io.UnsupportedEncodingException;
import java.security.*;

import play.db.jpa.*;

import javax.persistence.*;

import java.util.*;

@Entity
public class Personne extends Model {
	

	private String email;
	private String motDePasse;
	private String nom;
	private String prenom;
	private String telephone;
    
	public Personne(String email,String nom, String prenom, String telephone) throws Exception
	{
		this.email = email;
		
		if(Personne.find("byEmail", email).first() != null)
		{
			throw new Exception("L'email existe déjà");
		}
		
		this.nom = nom;
		this.prenom = prenom;
		this.telephone = telephone;
		
		//creation du mdp par defaut
		this.motDePasse = encodeMotDePasse(nom.toUpperCase());
	}
	

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public boolean verifMotDePasse(String motDePasse)
	{
		return this.motDePasse.equals(encodeMotDePasse(motDePasse.toUpperCase()));
	}
	
	public void setMotDePasse(String motDePasse) {
		this.motDePasse = encodeMotDePasse(motDePasse.toUpperCase());
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public String getPrenom() {
		return prenom;
	}

	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
	
	public static String encodeMotDePasse(String mdp)
	{
		String grain ="Az19@!";
		
		byte[] bytesOfDigeste;
		
		try {
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			MessageDigest sha1 = MessageDigest.getInstance("SHA");
			bytesOfDigeste = sha1.digest(md5.digest((mdp+grain).getBytes("UTF-8")));
			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
		return new String(bytesOfDigeste);
	}
	
}
