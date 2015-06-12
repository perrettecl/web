import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormatter;
import org.junit.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import play.test.*;
import models.*;

public class BasicTest extends UnitTest {
	
	@Before
	public void setUp() {
	    Fixtures.deleteAll();
	    Fixtures.loadModels("init-db.yml");
	}

	@Test
	public void creationPersonne() throws Exception {
	    // Create a new user and save it
	    new Personne("perrette.c@gmail.com","Perrette", "Clément", null).save();
	    
	    // Retrieve the user with e-mail address perrette.c@gmail.com
	    Personne perrettecl = Personne.find("byEmail", "perrette.c@gmail.com").first();
	    perrettecl.delete();
	    
	    // Test 
	    assertNotNull(perrettecl);
	    assertEquals("Perrette", perrettecl.getNom());
	}
	
	@Test
	public void verifMotDePassePersonne() throws Exception {
	    // Create a new user and save it
	    new Personne("perrette.c@gmail.com","Perrette", "Clément", null).save();
	    
	    // Retrieve the user with e-mail address perrette.c@gmail.com
	    Personne perrettecl = Personne.find("byEmail", "perrette.c@gmail.com").first();
	    perrettecl.delete();
	    
	    // Test 
	    assertNotNull(perrettecl);
	    assertEquals(true, perrettecl.verifMotDePasse("PERRETTE"));
	}
	
	@Test
	public void verifCreationCarte() throws Exception {
	    // Create a new user and save it
	    new Personne("perrette.c@gmail.com","Perrette", "Clément", null).save();
	    
	    // Retrieve the user with e-mail address perrette.c@gmail.com
	    Personne perrettecl = Personne.find("byEmail", "perrette.c@gmail.com").first();


	    // Test 
	    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
	    Date debut  = formatter.parse("01/01/2015");
	    Date fin  = formatter.parse("01/01/2017");
	    
	    Carte c = new Carte("0001",debut,fin);
	    c.save();
	    
	    c = Carte.find("byNumero","0001").first();
	    assertNotNull(c);
	    
	    perrettecl.addCarte(c);
	    perrettecl.save();
	    
	    
	    
	    
	    perrettecl = Personne.find("byEmail", "perrette.c@gmail.com").first();
	    assertEquals(1, perrettecl.getCartes().size());
	    
	    c.delete();
	    perrettecl.delete();
	    
	    
	    
	}
	
	@Test
	public void recherchePersonne() throws Exception {

	    List<Personne> list = Personne.recherche("l");
	    assertNotEquals(0, list.size());
	    
	}
	
	@Test
	public void test_getRacine() {
		Zone ext = Zone.getRacine();
	    assertNotNull(ext);  
	}
	
	@Test
	public void test_verifAutorise() throws Exception {
		Personne alain = Personne.find("byEmail", "alain.deloin@coin.com").first();
		Personne marc = Personne.find("byEmail", "marc.leblanc@coin.com").first();
		Zone zone1 = Zone.find("byNom", "Zone1").first();
		Zone ext = Zone.getRacine();
		
		assertTrue(ext.verifAutorise(alain));
		assertTrue(zone1.verifAutorise(marc));
		assertFalse(zone1.verifAutorise(alain));
	}
	
	@Test
	public void test_verifResponsable() throws Exception {
		Personne alain = Personne.find("byEmail", "alain.deloin@coin.com").first();
		Personne marc = Personne.find("byEmail", "marc.leblanc@coin.com").first();
		Zone zone1 = Zone.find("byNom", "Zone1").first();

		assertTrue(zone1.verifResponsable(marc));
		assertFalse(zone1.verifResponsable(alain));
	}
	
	@Test
	public void test_getFirstPere() throws Exception {
		Zone zone1 = Zone.find("byNom", "Zone1").first();

		assertTrue(zone1.getFirstPere().id == Zone.getRacine().id);
	}
	
	@Test
	public void test_verifAutorisation() throws Exception {
		
		Carte carte_alain = Carte.find("byNumero", "9827-5897-4578-4172").first();
		Carte carte_marc = Carte.find("byNumero", "5124-5897-4578-1384").first();
		Capteur c_ext_vers_zone1 =  Capteur.find("byPosition", Zone.getRacine()).first();

		assertTrue(Zone.verifAutorisation(carte_marc,c_ext_vers_zone1));
		assertFalse(Zone.verifAutorisation(carte_alain,c_ext_vers_zone1));
	}
	
	@Test
	public void test_listePersonneNonResponsable() throws Exception {
		Zone zone1 = Zone.find("byNom", "Zone1").first();

		assertTrue(zone1.listePersonneNonResponsable("").size()==3);
		assertTrue(zone1.listePersonneNonResponsable("Chantal").size()==1);
	}
	
	@Test
	public void test_listePersonneNonAutorise() throws Exception {
		Zone zone1 = Zone.find("byNom", "Zone1").first();

		assertTrue(zone1.listePersonneNonAutorise("").size()==2);
		assertTrue(zone1.listePersonneNonAutorise("Chantal").size()==1);
	}
	
	@Test
	public void test_ajoutZone() throws Exception {
		Zone ext = Zone.getRacine();
		Zone new_zone = new Zone("Nouvelle_zone",ext);

		assertTrue(ext.getFils().size()==2);
		assertTrue(new_zone.getFirstPere().id==Zone.getRacine().id);
	}
	

	

}
