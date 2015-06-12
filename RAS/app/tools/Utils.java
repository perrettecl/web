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
    		strbuild.append("{ \"id\" : "+p.getPrenom()+" "+p.getNom()+", \"value\" : \""+p.id+"\"},");
    	}
    	
    	if(liste.size() > 0)
    	{
    		i++;
    		Personne p = liste.get(i);
    		strbuild.append("{ \"id\" : "+p.getPrenom()+" "+p.getNom()+", \"value\" : \""+p.id+"\"}");
    	}
    	
    	strbuild.append("]");
    	
    	return strbuild.toString();
    }
    
    public static String rechercheToJSONforAutoCompletZone(List<Zone> liste)
    {
    	StringBuilder strbuild = new StringBuilder();
    	strbuild.append("[");
    	
    	int i = 0;
    	for(i=0; i<liste.size()-1; i++){
    		Zone z = liste.get(i);
    		strbuild.append("{ \"id\" : "+z.getNom()+", \"value\" : \""+z.id+"\"},");
    	}
    	
    	if(liste.size() > 0)
    	{
    		i++;
    		Zone z = liste.get(i);
    		strbuild.append("{ \"id\" : "+z.getNom()+", \"value\" : \""+z.id+"\"}");
    	}
    	
    	strbuild.append("]");
    	
    	return strbuild.toString();
    }
}
