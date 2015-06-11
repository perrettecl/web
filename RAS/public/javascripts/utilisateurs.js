function init() {
	$( "#search" ).bind("input", effectuerRecherche);
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
//

function getInfosUtilisateur(id) {
	$.ajax({
	  url: "/CPersonnes/getInfosUtilisateur?id=" + id,
	  dataType: "html"
	})
	  .done(function( data ) {
	    $("#corps").html(data);
	  });
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
						console.log(data);	
				 	},
		dataType: 'json'
	});
}

function invaliderCarte(idCarte) {
	$.ajax({
		type: "POST",
		url: "/CPersonnes/invaliderCarte",
		data: 	{ 
					'id' : idCarte
				},
		success: 	function(data) {
						$("#carte-" + idCarte).removeClass("green");
						$("#carte-" + idCarte).addClass("red");
						$("#btn-invalid-carte-" + idCarte).remove();
				 	},
		dataType: 'JSON',
		async: false
	});
}