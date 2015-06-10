package models;

import play.*;
import play.db.jpa.*;

import javax.persistence.*;

import java.util.*;

@Entity
public class Evenement extends Model {
    private Date date;
    private String Description;
    
    
	public Evenement(Date date, String description) {
		this.date = date;
		Description = description;
	}
	
	public Date getDate() {
		return date;
	}
	public String getDescription() {
		return Description;
	}
}
