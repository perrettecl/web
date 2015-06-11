package models;

import play.*;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.*;

import play.db.jpa.*;

import javax.persistence.*;

import java.util.*;

@Entity
public class Zone extends Model {
	private String nom;
	private String adresse;
	private String codePostal;

	private String ville;
	private String info;
	private boolean accesExclusif;
	private boolean racine;
	
	public void setRacine(boolean racine) {
		this.racine = racine;
	}


	
	@ManyToMany(targetEntity=models.Personne.class)
	@JoinTable(name = "Responsable_Zone", joinColumns =
	{ @JoinColumn(name = "zonesresponsable_id", nullable = false) },
	inverseJoinColumns = { @JoinColumn(name =
	"responsables_id", nullable = false) })
	private Set<Personne> responsables;
	
	public void setPeres(Set<Zone> peres) {
		this.peres = peres;
	}

	public void setFils(Set<Zone> fils) {
		this.fils = fils;
	}

	
	@ManyToMany(targetEntity=models.Personne.class)
	@JoinTable(name = "AUTORISE_ZONE", joinColumns =
	{ @JoinColumn(name = "ZONESAUTORISE_ID", nullable = false) },
	inverseJoinColumns = { @JoinColumn(name =
	"PERSONNESAUTORISE_ID", nullable = false) })
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
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
		this.save();
	}

	public String getAdresse() {
		return adresse;
	}

	public void setResponsables(Set<Personne> responsables) {
		this.responsables = responsables;
	}

	public void setPersonnesAutorise(Set<Personne> personnesAutorise) {
		this.personnesAutorise = personnesAutorise;
	}
	
	public void setAdresse(String adresse) {
		this.adresse = adresse;
		this.save();
	}

	public String getCodePostal() {
		return codePostal;
	}

	public void setCodePostal(String codePostal) {
		this.codePostal = codePostal;
		this.save();
	}

	public String getVille() {
		return ville;
	}

	public void setVille(String ville) {
		this.ville = ville;
		this.save();
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
		this.save();
	}

	public boolean isAccesExclusif() {
		return accesExclusif;
	}

	public void setAccesExclusif(boolean accesExclusif) {
		this.accesExclusif = accesExclusif;
		this.save();
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
	
	
	public void addPersonneAutorise(Personne p)
	{
		personnesAutorise.add(p);
		p.getZonesAutorise().add(this);
		
		p.save();
		this.save();
		
		this.refresh();
	}
	
	public void addResponsable(Personne p)
	{
		responsables.add(p);
		p.getZonesResponsable().add(this);
		
		p.save();
		this.save();
		
		this.refresh();
	}
	
	public void removePersonneAutorise(Personne p)
	{
		personnesAutorise.remove(p);
		p.getZonesAutorise().remove(this);
		
		p.save();
		this.save();
		
		this.refresh();
	}
	
	public void removeResponsable(Personne p)
	{
		responsables.remove(p);
		p.getZonesResponsable().remove(this);
		
		p.save();
		this.save();
		
		this.refresh();
	}
	
	public boolean isRacine() {
		return racine;
	}

	public Zone(String nom) {
		this.nom = nom;
		this.racine = false;
	}
	
	public static Zone getRacine()
	{
		return Zone.find("SELECT z FROM Zone z WHERE z.racine=true").first();
	}
	
	public static Set<Zone> getPremierNiveau()
	{
		return Zone.getRacine().getFils();
	}
	
	public boolean verifResponsable(Personne user)
	{
		Query q = Zone.em().createNativeQuery("SELECT count(zr.*) FROM  Responsable_Zone rz where rz.zonesresponsable_id=:id_zone and rz.responsables_id=:id_user");
		q.setParameter("id_zone", this.id);
		q.setParameter("id_user", user.id);
		
		BigInteger count = ((BigInteger)q.getSingleResult());
		
		return count.intValue() > 0;
	}
	
	public boolean verifAutorise(Personne user)
	{
		//cas de la zone exterieur
		if(this.id==Zone.getRacine().id)
			return true;
		
		Query q = Zone.em().createNativeQuery("SELECT count(az.*) FROM  Autorise_Zone az where az.ZONESAUTORISE_ID=:id_zone and az.PERSONNESAUTORISE_ID=:id_user");
		q.setParameter("id_zone", this.id);
		q.setParameter("id_user", user.id);
		BigInteger count = ((BigInteger)q.getSingleResult());
		
		return count.intValue() > 0;
	}
	
	public static boolean verifAutorisation(Carte carte, Capteur capteur)
	{
		// carte invalide
		if(!carte.isValide())
			return false;
		
		// zone exterieur
		if(capteur.getAcces().id == Zone.getRacine().id)
			return true;
		
		Query q = Zone.em().createNativeQuery("select count(az.*)  from CARTE b, CAPTEUR c, AUTORISE_ZONE az, PERSONNE p where az.ZONESAUTORISE_ID=c.acces_id and az.PERSONNESAUTORISE_ID=b.utilisateur_id and c.id=:id_capteur and b.id=:id_carte");
		q.setParameter("id_capteur", capteur.id);
		q.setParameter("id_carte", carte.id);
		
		BigInteger count = ((BigInteger)q.getSingleResult());
		
		return count.intValue() > 0;
		
	}
	
	
	public Zone getFirstPere()
	{
		Zone retour = null;
		
		for(Zone z: peres)
		{
			retour = z;
			break;
		}
		
		return retour;
	}
	

}
