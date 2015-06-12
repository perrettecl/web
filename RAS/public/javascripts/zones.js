numNouvelleZone = 0;

function afficherZone(id) {
	$.ajax({
	  url: "/CZone/getInfosZone",
	  data: 	{ 
					'id' : id
				},
	  dataType: "html",
	  async: false
	})
	  .done(function( data ) {
	    $("#corps").html(data);
	  });
	  
	$.ajax({
	  url: "/CZone/getZonesAdjacentes",
	  data: 	{ 
					'id' : id
				},
	  dataType: "html",
	  async: false
	})
	  .done(function( data ) {
	    $("#navigation-zone").html(data);
	  });
	
	if(typeof $("#nouveau-responsable") !== undefined) {
		$( "#nouveau-responsable" ).autocomplete({
			source: "/CZone/rechercheNonResponsable?idZone=" + id,
			minLength: 2,
			select: function( event, ui ) {
				$( "#id-nouveau-responsable" ).val(ui.item.id);
			}
		});
	}
	  
	if(typeof $("#nouvelle-autorisation") !== undefined) {
	  
		$( "#nouvelle-autorisation" ).autocomplete({
			source: "/CZone/rechercheNonAutorise?idZone=" + id,
			minLength: 2,
			select: function( event, ui ) {
				$( "#id-nouvelle-autorisation" ).val(ui.item.id);
			}
		});
	}
	
}

function modifierNomZone(idZone) {
	var nomZone = $("#spanNomZone").html();
				
	$("#titreZone").empty();
	$("#titreZone").append('<input type="text" class="validate col s12 m6 l6" id="inputNomZone" name="nomZone" value="' + nomZone + '" />');
	$("#titreZone").append('<a class="waves-effect waves-light btn green" href="javascript:submitNomZone(' + idZone + ')">Valider</a>');
	
	$("#inputNomZone").keyup(function(e){
		if(e.keyCode == 13)
		{
			submitNomZone(idZone);
		}
	});
	          
}

function creerZone(idZoneCourante) {
	var idNouvelleZone = numNouvelleZone++;

	$("#liste-zones").append('<input type="text" class="collection-item" name="nouvelle-zone" id="' + idNouvelleZone +'" placeHolder="Nom de la nouvelle zone" />');
	$("#" + idNouvelleZone).keyup(function(e){
		if(e.keyCode == 13)
		{
			submitNouvelleZone(this, idZoneCourante);
		}
	});
}

function submitNouvelleZone(input, idPere) {
	$.ajax({
		type: "POST",
		url: "/CZone/creerZone",
		data: 	{ 
					'idPere' : idPere,
					'nom' : input.value
				},
		success: 	function(data) {
						if(data["erreur"] == "false") {
							Materialize.toast('<span>Zone créée</span>&nbsp;<i class="mdi-action-done prefix active"></i>', 5000, 'green');
							afficherZone(idPere);
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

function submitNomZone(idZone) {
	var newNom = $("#inputNomZone").val();

	$.ajax({
		type: "POST",
		url: "/CZone/renommerZone",
		data: 	{ 
					'id' : idZone,
					'newNom' : newNom
				},
		success: 	function(data) {
						if(data["erreur"] == "false") {
							$("#titreZone").empty();
							$("#titreZone").append('<span id="spanNomZone">' + newNom + '</span> ');
							$("#spanNomZoneNav").html(newNom);
							$("#titreZone").append('<a class="btn-floating waves-effect waves-light grey" href="javascript:modifierNomZone(' + idZone + ')"><i class="tiny mdi-image-edit"></i></a>');
							Materialize.toast('<span>Nom de la zone modifié</span>&nbsp;<i class="mdi-action-done prefix active"></i>', 5000, 'green');
						} else if (data["erreur"] == "true") {
							$("#inputNomZone").addClass("invalid");
							$.each(data, function(i, field){
								if(i != "erreur")
									Materialize.toast('<i class="mdi-navigation-close prefix active"></i>&nbsp;<span>' + field + '</span>', 5000, 'red');
							});
						}
				 	},
		dataType: 'json'
	});
}

function ajouterResponsable(idZone) {
	var idNvResponsable = $("#id-nouveau-responsable").val();
	
	if(idNvResponsable != "") {
		$.ajax({
			type: "POST",
			url: "/CZone/ajouterResponsableZone",
			data: 	{ 
						'idZone' : idZone,
						'idPersonne' : idNvResponsable
					},
			success: 	function(data) {
							if(data["erreur"] == "false") {
								Materialize.toast('<span>Responsable ajouté</span>&nbsp;<i class="mdi-action-done prefix active"></i>', 5000, 'green');
							} else if (data["erreur"] == "true") {
								$.each(data, function(i, field){
									if(i != "erreur")
										Materialize.toast('<i class="mdi-navigation-close prefix active"></i>&nbsp;<span>' + field + '</span>', 5000, 'red');
								});
							}
					 	},
			dataType: 'json'
		});
	} else {
		Materialize.toast('<i class="mdi-navigation-close prefix active"></i>&nbsp;<span>Veuillez sélectionner un responsable à ajouter</span>', 5000, 'red');
	}
}

function removeResponsableZone(idZone, idPersonne) {
	if(confirm("Êtes-vous sûr de vouloir supprimer ce responsable pour la zone?")) {
		$.ajax({
			type: "POST",
			url: "/CZone/supprimerResponsableZone",
			data: 	{ 
						'idZone' : idZone,
						'idPersonne' : idPersonne
					},
			success: 	function(data) {
							if(data["erreur"] == "false") {
								Materialize.toast('<span>Responsable supprimé</span>&nbsp;<i class="mdi-action-done prefix active"></i>', 5000, 'green');
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
}

function removeAutorisationZone(idZone, idPersonne) {
	if(confirm("Êtes-vous sûr de vouloir supprimer cette autorisation pour la zone?")) {
		$.ajax({
			type: "POST",
			url: "/CZone/supprimerAutorisationAcces",
			data: 	{ 
						'idZone' : idZone,
						'idPersonne' : idPersonne
					},
			success: 	function(data) {
							if(data["erreur"] == "false") {
								Materialize.toast('<span>Autorisation supprimée</span>&nbsp;<i class="mdi-action-done prefix active"></i>', 5000, 'green');
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
}