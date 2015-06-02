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

	private String info;

	public Capteur(Zone position, Zone acces, String info) {
		super();
		this.position = position;
		//this.acces = acces;
		this.info = info;
	}
	
	
	public Zone getPosition() {
		return position;
	}

	public void setPosition(Zone position) {
		this.position = position;
	}

	/*public Zone getAcces() {
		return acces;
	}

	public void setAcces(Zone acces) {
		this.acces = acces;
	}*/

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public Long getId() {
		return id;
	}

	
	
	
}
