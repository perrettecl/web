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
	private String codePostal;
	private String ville;
	private boolean admin;
	
	@OneToMany(mappedBy="utilisateur")
	private Set<Carte> cartes = new HashSet<Carte>();
	
	@ManyToMany(targetEntity=models.Zone.class, mappedBy="responsables")
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
			String adresse, String codepostal, String ville) throws Exception {
		
		if(Personne.find("byEmail", email).first() != null)
		{
			throw new Exception("L'email existe déjà");
		}
		
		this.email = email;
		this.nom = nom;
		this.prenom = prenom;
		this.telephone = telephone;
		this.adresse = adresse;
		this.codePostal = codepostal;
		this.ville = ville;
		

		//creation du mdp par defaut
		this.motDePasse = encodeMotDePasse(nom.toUpperCase());
		System.out.println("je suis la");
	}


	public static List<Personne> recherche(String chaine) {
		List<Personne> list = null;
		Query q = Personne.em().createQuery("SELECT p FROM Personne p WHERE LOWER(p.nom) like LOWER(:arg) or LOWER(p.prenom) like LOWER(:arg)");
		q.setParameter("arg", chaine+"%");
		list = q.getResultList();
		
		for(Personne p : list)
		{
			p.cartes=null;
			p.zonesAutorise=null;
			p.zonesResponsable=null;
			p.motDePasse=null;
		}
		
		return list;
	}
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public boolean verifMotDePasse(String motDePasse)
	{
		if(this.motDePasse == null)
		{
			this.motDePasse = encodeMotDePasse(this.nom.toUpperCase());
			this.save();
		}
			
		String mdp_encode = encodeMotDePasse(motDePasse);
		return this.motDePasse.equals(mdp_encode);
	}
	
	public void setMotDePasse(String motDePasse) {
		this.motDePasse = encodeMotDePasse(motDePasse);
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


	public String getCodePostal() {
		return codePostal;
	}


	public void setCodePostal(String codepostal) {
		this.codePostal = codepostal;
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


	public void setZonesResponsable(Set<Zone> zonesResponsable) {
		this.zonesResponsable = zonesResponsable;
	}

	public void setZonesAutorise(Set<Zone> zonesAutorise) {
		this.zonesAutorise = zonesAutorise;
	}

	private static String encodeMotDePasse(String mdp)
	{
		String grain ="Az19@!";
		
		byte[] bytesOfDigeste;
		
		try {
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			MessageDigest sha1 = MessageDigest.getInstance("SHA");
			bytesOfDigeste = sha1.digest(md5.digest((mdp+grain).getBytes()));
			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
		return bytesToHex(bytesOfDigeste);
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
	
	public void addCarte(Carte c){
		cartes.add(c);
		c.setUtilisateur(this);
		
		this.save();
		c.save();
		
		this.refresh();
	}
	
	public void resetMotDePasse() {
		this.motDePasse = encodeMotDePasse(nom.toUpperCase());
	}

	private static String bytesToHex(byte[] in) {
		final StringBuilder builder = new StringBuilder();
		for(byte b : in) {
			builder.append(String.format("%02x", b));
		}
		return builder.toString();
	}

	
	
}
