package jobs;

import play.*;
import play.jobs.*;
import play.test.*;
import models.*;

@OnApplicationStart
public class Bootstrap extends Job {
    
    public void doJob() {
    	// Check if the database is empty
        if(Personne.count() == 0) {
            Fixtures.loadModels("init-db.yml");
        }
    }  
}