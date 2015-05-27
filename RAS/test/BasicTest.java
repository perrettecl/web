import org.junit.*;

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
	

}
