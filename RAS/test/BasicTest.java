import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormatter;
import org.junit.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import play.test.*;
import models.*;

public class BasicTest extends UnitTest {

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
	
	/*@Test
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
	    
	    c.setUtilisateur(perrettecl);
	    c.save();
	    perrettecl.save();
	    
	    
	    
	    
	    perrettecl = Personne.find("byEmail", "perrette.c@gmail.com").first();
	    assertEquals(1, perrettecl.getCartes().size());
	    
	    c.delete();
	    perrettecl.delete();
	    
	    
	    
	}*/
	
	@Test
	public void recherchePersonne() throws Exception {
	    // Create a new user and save it
	    new Personne("perrette.c@gmail.com","Perrette", "Clément", null).save();
	    
	    // Retrieve the user with e-mail address perrette.c@gmail.com
	    List<Personne> list = Personne.recherche("clém");
	    assertNotEquals(0, list.size());
	   
	    Personne perrettecl = Personne.find("byEmail", "perrette.c@gmail.com").first();
	    perrettecl.delete();
	    
	    
	}
	
	/*@Test
	public void sequenceCreation() throws Exception {
	    // Create a new user and save it
	    new Personne("perrette.c@gmail.com","Perrette", "Clément", null).save();
	    new Personne("toto@gmail.com","Toto", "Titi", null).save();
	    new Personne("admin@gmail.com","Admin", "Istrateur", null).save();
	    
	    // Retrieve the user with e-mail address perrette.c@gmail.com
	    Personne perrettecl = Personne.find("byEmail", "perrette.c@gmail.com").first();
	    Personne toto = Personne.find("byEmail", "toto@gmail.com").first();
	    Personne admin = Personne.find("byEmail", "admin@gmail.com").first();

	    // Test 
	    assertNotNull(perrettecl);
	    assertEquals(true, perrettecl.verifMotDePasse("PERRETTE"));
	}*/
	

}
