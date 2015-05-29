package models;

import play.*;

import java.io.UnsupportedEncodingException;
import java.security.*;

import play.db.jpa.*;

import javax.persistence.*;

import java.util.*;

@Entity
public class Zone extends Model {
	
	@Id
	private long id;
	private String Nom;
	private String adresse;
	private String codePostal;
	private String ville;
	private String info;
	private boolean accesExclusif;
	
	@ManyToMany(mappedBy="zonesResponsable")
	private Collection<Personne> responsables;
	
	@ManyToMany(mappedBy="zonesAutorise")
	private Collection<Personne> personnesAutorise;
	
	@OneToMany(mappedBy="position")
	private Collection<Capteur> capteurDeLaZone;
	
	@OneToMany(mappedBy="acces")
	private Collection<Capteur> capteurDonnantSurZone;
	
	@ManyToMany(mappedBy="fils")
	private Collection<Zone> peres;
	
	@ManyToMany(mappedBy="peres")
	private Collection<Zone> fils;
	
	
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

	public Collection<Personne> getResponsables() {
		return responsables;
	}

	public Collection<Personne> getPersonnesAutorise() {
		return personnesAutorise;
	}

	public Collection<Capteur> getCapteurDeLaZone() {
		return capteurDeLaZone;
	}

	public Collection<Capteur> getCapteurDonnantSurZone() {
		return capteurDonnantSurZone;
	}

	public Collection<Zone> getPeres() {
		return peres;
	}

	public Collection<Zone> getFils() {
		return fils;
	}
	
}
