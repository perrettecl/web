function init() {
	$( "#search" ).bind("input", effectuerRecherche);
		
	modifMotDePasseAffiche = false;
	$("#div-modif-mdp").hide();
		
	ajoutCarteAffiche = false;
	$("#div-ajout-carte").hide();
}

function effectuerRecherche() {
	$("li.collection-item").remove();
	if( $( "#search" ).val() != "") {
		
		$.getJSON("/CPersonnes/rechercheUtilisateur?recherche=" + $( "#search" ).val(), function(data) {
			$("li.collection-item").remove();
			if(data != "") {
				$.each(data, function(i, field){
					$("#liste-utilisateurs").append('<li class="collection-item"><a href="javascript:getInfosUtilisateur(' + field["id"] + ')">' + field["prenom"] + ' ' + field["nom"] + '</a></li>');
				});
			}
		});
	}
}

function getInfosUtilisateur(id) {
	$.ajax({
	  url: "/CPersonnes/getInfosUtilisateur?id=" + id,
	  dataType: "html",
	  async: false
	})
	  .done(function( data ) {
	    $("#corps").html(data);
	  });

    modifMotDePasseAffiche = false;
	$("#div-modif-mdp").hide();
   	ajoutCarteAffiche = false;
	$("#div-ajout-carte").hide();
   	
}

function nouvelUtilisateur() {
	$.ajax({
	  url: "/CPersonnes/nouvelUtilisateur",
	  dataType: "html"
	})
	  .done(function( data ) {
	    $("#corps").html(data);
	  });
}
function envoyerFormulaireCreation() {
	$.ajax({
		type: "POST",
		url: "/CPersonnes/creerUtilisateur",
		data: 	{ 
					'nom' : $("#nom").val(),
					'prenom' : $("#prenom").val(),
					'email' : $("#email").val(),
					'telephone' : $("#telephone").val(),
					'adresse' : $("#adresse").val(),
					'codePostal' : $("#code-postal").val(),
					'ville' : $("#ville").val()
				},
		success: 	function(data) {
						if(data["erreur"] == "false") {
							getInfosUtilisateur(data["id"]);
							Materialize.toast('<span>Utilisateur créé</span>&nbsp;<i class="mdi-action-done prefix active"></i>', 5000, 'green');
						} else if (data["erreur"] == "true") {
							$.each(data, function(i, field){
								if(i != "erreur")
									Materialize.toast('<i class="mdi-navigation-close prefix active"></i>&nbsp;<span>' + field + '</span>', 5000, 'red');
							});
						}
				 	},
		dataType: 'json'
	});
}

function afficherModifMotDePasse() {
	if(modifMotDePasseAffiche == false) {
		modifMotDePasseAffiche = true;
		$("#div-modif-mdp").show();
	} else {
		modifMotDePasseAffiche = false;
		$("#div-modif-mdp").hide();
	}
}

function modifierMotDePasse(idPersonne) {
	$.ajax({
		type: "POST",
		url: "/CPersonnes/modifierMotDePasse",
		data: 	{ 
					'id' : idPersonne,
					'newMdp' : $("#newMdp").val(),
					'newMdpConf' : $("#newMdpConf").val()
				},
		success: 	function(data) {
						if(data["erreur"] == "false") {
							Materialize.toast('<span>Mot de passe modifié</span>&nbsp;<i class="mdi-action-done prefix active"></i>', 5000, 'green');
							modifMotDePasseAffiche = false;
							$("#div-modif-mdp").hide();
						} else if (data["erreur"] == "true") {
							$.each(data, function(i, field){
								if(i != "erreur")
									Materialize.toast('<i class="mdi-navigation-close prefix active"></i>&nbsp;<span>' + field + '</span>', 5000, 'red');
							});
						}
				 	},
		dataType: 'json'
	});
}

function afficherAjoutCarte() {
	if(ajoutCarteAffiche == false) {
		ajoutCarteAffiche = true;
		$("#div-ajout-carte").show();
		$("#btn-ajout-carte").hide();
				
	} else {
		ajoutCarteAffiche = false;
		$("#div-ajout-carte").hide();
		$("#btn-ajout-carte").show();
	}
}

function ajouterCarte(idPersonne) {
	$.ajax({
		type: "POST",
		url: "/CPersonnes/ajouterCarte",
		data: 	{ 
					'id' : idPersonne,
					'numeroCarte' : $("#numCarte").val()
				},
		success: 	function(data) {
						if(data["erreur"] == "false") {
							getInfosUtilisateur(idPersonne);
							Materialize.toast('<span>Carte ajoutée</span>&nbsp;<i class="mdi-action-done prefix active"></i>', 5000, 'green');
						} else if (data["erreur"] == "true") {
							$.each(data, function(i, field){
								if(i != "erreur")
									Materialize.toast('<i class="mdi-navigation-close prefix active"></i>&nbsp;<span>' + field + '</span>', 5000, 'red');
							});
						}
				 	},
		dataType: 'json'
	});
}

function resetMotDePasse(idPersonne) {
	if(confirm("Êtes-vous sûr de vouloir réinitialiser le mot de passe?")) {
		$.ajax({
			type: "POST",
			url: "/CPersonnes/resetMotDePasse",
			data: 	{ 
						'id' : idPersonne
					},
			success: 	function(data) {
							if(data["erreur"] == "false") {
								Materialize.toast('<span>Mot de passe réinitialisé</span>&nbsp;<i class="mdi-action-done prefix active"></i>', 5000, 'green');
							} else if (data["erreur"] == "true") {
								Materialize.toast('<i class="mdi-navigation-close prefix active"></i>&nbsp;<span>Erreur lors de la réinitialisation</span>', 5000, 'red');
							}
					 	},
			dataType: 'json'
		});
	}
}

function envoyerFormulaireModification(idPersonne) {
	$.ajax({
		type: "POST",
		url: "/CPersonnes/modifierUtilisateur",
		data: 	{ 
					'id' : idPersonne,
					'nom' : $("#nom").val(),
					'prenom' : $("#prenom").val(),
					'telephone' : $("#telephone").val(),
					'adresse' : $("#adresse").val(),
					'codePostal' : $("#code-postal").val(),
					'ville' : $("#ville").val()
				},
		success: 	function(data) {
						if(data["erreur"] == "false") {
							getInfosUtilisateur(idPersonne);
							Materialize.toast('<span>Utilisateur modifié</span>&nbsp;<i class="mdi-action-done prefix active"></i>', 5000, 'green');
						} else if (data["erreur"] == "true") {
							$.each(data, function(i, field){
								if(i != "erreur")
									Materialize.toast('<i class="mdi-navigation-close prefix active"></i>&nbsp;<span>' + field + '</span>', 5000, 'red');
							});
						}
				 	},
		dataType: 'json'
	});
}

function invaliderCarte(idCarte) {
	if(confirm("Êtes-vous sûr de vouloir invalider cette carte? L'opération est irréversible")) {
		$.ajax({
			type: "POST",
			url: "/CPersonnes/invaliderCarte",
			data: 	{ 
						'id' : idCarte
					},
			success: 	function(data) {
							//vérifier que la carte a bien été invalidée (retour JSON)
							$("#carte-" + idCarte).removeClass("green");
							$("#carte-" + idCarte).addClass("red");
							$("#btn-invalid-carte-" + idCarte).remove();
					 	},
			dataType: 'JSON',
			async: false
		});
	}
}