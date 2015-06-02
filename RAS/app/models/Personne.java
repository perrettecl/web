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
	private String adresse;
	private String codepostal;
	private String ville;
	private boolean admin;
	
	@OneToMany(mappedBy="utilisateur")
	private Set<Carte> cartes = new HashSet<Carte>();
	
	@ManyToMany(targetEntity=models.Zone.class,mappedBy="responsables")
	private Set<Zone> zonesResponsable = new HashSet<Zone>();
	
	@ManyToMany(targetEntity=models.Zone.class, mappedBy="personnesAutorise")
	private Set<Zone> zonesAutorise = new HashSet<Zone>();
	
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
	
	
	public Personne(String email, String nom, String prenom, String telephone,
			String adresse, String codepostal, String ville) {
		this.email = email;
		this.nom = nom;
		this.prenom = prenom;
		this.telephone = telephone;
		this.adresse = adresse;
		this.codepostal = codepostal;
		this.ville = ville;
		

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
	
	public String getAdresse() {
		return adresse;
	}


	public void setAdresse(String adresse) {
		this.adresse = adresse;
	}


	public String getCodepostal() {
		return codepostal;
	}


	public void setCodepostal(String codepostal) {
		this.codepostal = codepostal;
	}


	public String getVille() {
		return ville;
	}


	public void setVille(String ville) {
		this.ville = ville;
	}


	public boolean isAdmin() {
		return admin;
	}


	public void setAdmin(boolean admin) {
		this.admin = admin;
	}


	public Set<Carte> getCartes() {
		return cartes;
	}


	public Set<Zone> getZonesResponsable() {
		return zonesResponsable;
	}


	public Set<Zone> getZonesAutorise() {
		return zonesAutorise;
	}


	private static String encodeMotDePasse(String mdp)
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
	
	public static Personne connect(String email, String mdp)
	{		
		Personne retour = Personne.find("byEmail", email).first();
		
		if(retour != null && retour.verifMotDePasse(mdp))
		{
			return retour;
		}
		else
		{
			return null;	
		}
			
	}
	
}
