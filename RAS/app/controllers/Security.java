package controllers;
 
import models.*;
 
public class Security extends Secure.Security {
	
    static boolean authenticate(String username, String password) {
    	Personne user = Personne.find("byEmail",username).first();
    	
    	if(user == null)
    	{
    		return false;
    	}else{
    		return user.verifMotDePasse(password);
    	}
    	
    }
    
    static boolean check(String profile) {
    	return connected() != null;
    }

}
