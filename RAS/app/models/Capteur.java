package models;

import play.*;

import java.io.UnsupportedEncodingException;
import java.security.*;

import play.db.jpa.*;

import javax.persistence.*;

import java.util.*;

@Entity
public class Capteur extends Model {
		
	@ManyToOne
	private Zone position;
	
	@ManyToOne
	private Zone acces;

	public Capteur(Zone position, Zone acces) {
		this.position = position;
		this.acces = acces;
		
		this.save();
		
		position.getCapteurDeLaZone().add(this);
		acces.getCapteurDonnantSurZone().add(this);
		
		position.save();
		acces.save();
		
		this.refresh();
	}
	
	
	public Zone getPosition() {
		return position;
	}

	public void setPosition(Zone position) {
		this.position = position;
		this.save();
	}

	public Zone getAcces() {
		return acces;
	}

	public void setAcces(Zone acces) {
		this.acces = acces;
		this.save();
	}	
	
}
