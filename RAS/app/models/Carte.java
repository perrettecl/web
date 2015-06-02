package models;

import play.*;

import java.io.UnsupportedEncodingException;
import java.security.*;

import play.db.jpa.*;

import javax.persistence.*;

import java.util.*;

@Entity
public class Carte extends Model {
	
	private String numero;
	private Date dateCreation;
	private Date dateExpiration;
	private boolean valide;
	
	@ManyToOne
	private Personne utilisateur;

	public Date getDateCreation() {
		return dateCreation;
	}

	public void setDateCreation(Date dateCreation) {
		this.dateCreation = dateCreation;
	}

	public Date getDateExpiration() {
		return dateExpiration;
	}

	public void setDateExpiration(Date dateExpiration) {
		this.dateExpiration = dateExpiration;
	}

	public boolean isValide() {
		return valide;
	}

	public void setValide(boolean valide) {
		this.valide = valide;
	}

	public Personne getUtilisateur() {
		return utilisateur;
	}

	public void setUtilisateur(Personne utilisateur) {
		this.utilisateur = utilisateur;
		utilisateur.getCartes().add(this);
	}

	public String getNumero() {
		return numero;
	}

	public Carte(String numero, Date dateCreation, Date dateExpiration) {
		super();
		this.numero = numero;
		this.dateCreation = dateCreation;
		this.dateExpiration = dateExpiration;
	}
	
}