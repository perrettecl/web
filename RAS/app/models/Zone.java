package models;

import play.*;

import java.io.UnsupportedEncodingException;
import java.security.*;

import play.db.jpa.*;

import javax.persistence.*;

import java.util.*;

@Entity
public class Zone extends Model {
	
	
	private String Nom;
	private String adresse;
	private String codePostal;
	private String ville;
	private String info;
	private boolean accesExclusif;
	
	@ManyToMany(targetEntity=models.Personne.class)
	private Set<Personne> responsables;
	
	@ManyToMany(targetEntity=models.Personne.class)
	private Set<Personne> personnesAutorise;
	
	@OneToMany(targetEntity = models.Capteur.class, mappedBy= "position")
	private Set<Capteur> capteurDepuisZone;
	
	@OneToMany(targetEntity = models.Capteur.class, mappedBy= "acces")
	private Set<Capteur> capteurVersZone;
	
	public Set<Capteur> getCapteurDeLaZone() {
		return capteurDepuisZone;
	}

	public Set<Capteur> getCapteurDonnantSurZone() {
		return capteurVersZone;
	}
	
	@ManyToMany
	private Set<Zone> peres;
	
	@ManyToMany(mappedBy="peres")
	private Set<Zone> fils;
	
	
	public String getNom() {
		return Nom;
	}

	public void setNom(String nom) {
		Nom = nom;
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

	public void setCodePostal(String codePostal) {
		this.codePostal = codePostal;
	}

	public String getVille() {
		return ville;
	}

	public void setVille(String ville) {
		this.ville = ville;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public boolean isAccesExclusif() {
		return accesExclusif;
	}

	public void setAccesExclusif(boolean accesExclusif) {
		this.accesExclusif = accesExclusif;
	}

	public Long getId() {
		return id;
	}

	public Set<Personne> getResponsables() {
		return responsables;
	}

	public Set<Personne> getPersonnesAutorise() {
		return personnesAutorise;
	}


	public Set<Zone> getPeres() {
		return peres;
	}

	public Set<Zone> getFils() {
		return fils;
	}
	
}
