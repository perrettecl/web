package controllers;

import models.Personne;
import play.mvc.*;

public class EnvoiMail extends Mailer {
	
	public static void mail_resteMotDePasse(Personne p)
	{
		setFrom("RAS admin <admin@ras.com>");
		setSubject("Votre nouveau mot de passe à été remis à zero");
		addRecipient(p.getEmail());
		send("VMails/resetMail",p);
	}

}
