function testerAutorisation() {
	if($("#numeroCarte").val() != "" && $("#idCapteur").val() != "") {
		$.ajax({
			type: "POST",
			url: "/CTestAutorisation/verifierAutorisation",
			data: 	{ 
						'numeroCarte' : $("#numeroCarte").val(),
						'idCapteur' : $("#idCapteur").val()
					},
			success: 	function(data) {
							if(data["erreur"] == "false") {
								Materialize.toast('<span>Accès autorisé</span>&nbsp;<i class="mdi-action-done prefix active"></i>', 5000, 'green');
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
		Materialize.toast('<i class="mdi-navigation-close prefix active"></i>&nbsp;<span>Veuillez saisir un numéro de carte et un numéro de capteur</span>', 5000, 'red');
	}
}