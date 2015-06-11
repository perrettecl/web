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
		this.save();
	}

	public Date getDateExpiration() {
		return dateExpiration;
	}

	public void setDateExpiration(Date dateExpiration) {
		this.dateExpiration = dateExpiration;
		this.save();
	}

	public boolean isValide() {
		if(!valide){
			return valide;
		} else {
			return (new Date().compareTo(dateExpiration) <= 0);
		}
	}

	public void setValide(boolean valide) {
		this.valide = valide;
		this.save();
	}

	public Personne getUtilisateur() {
		return utilisateur;
	}

	public void setUtilisateur(Personne utilisateur) {
		this.utilisateur = utilisateur;
		this.save();
	}

	public String getNumero() {
		return numero;
	}

	public Carte(String numero, Date dateCreation, Date dateExpiration) {
		super();
		this.numero = numero;
		this.dateCreation = dateCreation;
		this.dateExpiration = dateExpiration;
		this.valide = true;
		
		this.save();
	}
	
	public void invaliderCarte(){
		this.valide = false;
		this.save();
	}
	
	public void setNumero(String numero)
	{
		this.numero = numero;
	}
	
	public static boolean existe(String numero) {
		Query q = Carte.em().createQuery("SELECT c FROM Carte c WHERE c.numero=:arg");
		q.setParameter("arg", numero);

		return !q.getResultList().isEmpty();
	}

}
