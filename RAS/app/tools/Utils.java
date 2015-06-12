package tools;

import java.util.List;

import models.Personne;
import models.Zone;


public class Utils {
	public static String rechercheToJSONforAutoCompletPersonne(List<Personne> liste)
    {
    	StringBuilder strbuild = new StringBuilder();
    	strbuild.append("[");
    	
    	int i = 0;
    	for(i=0;i<liste.size()-1;i++){
    		Personne p = liste.get(i);
    		strbuild.append("{ \"value\" : \""+p.getPrenom()+" "+p.getNom()+"\", \"id\" : \""+p.id+"\"},");
    	}
    	
    	if(liste.size() > 0)
    	{
    		Personne p = liste.get(i);
    		strbuild.append("{ \"value\" : \""+p.getPrenom()+" "+p.getNom()+"\", \"id\" : \""+p.id+"\"}");
    	}
    	
    	strbuild.append("]");
    	
    	System.out.println("SALUUUUUUUT");
    	
    	return strbuild.toString();
    }
    
    public static String rechercheToJSONforAutoCompletZone(List<Zone> liste)
    {
    	StringBuilder strbuild = new StringBuilder();
    	strbuild.append("[");
    	
    	int i = 0;
    	for(i=0; i<liste.size()-1; i++){
    		Zone z = liste.get(i);
    		strbuild.append("{ \"value\" : \""+z.getNom()+"\", \"id\" : \""+z.id+"\"},");
    	}
    	
    	if(liste.size() > 0)
    	{
    		Zone z = liste.get(i);
    		strbuild.append("{ \"value\" : \""+z.getNom()+"\", \"id\" : \""+z.id+"\"}");
    	}
    	
    	strbuild.append("]");
    	
    	return strbuild.toString();
    }
}
