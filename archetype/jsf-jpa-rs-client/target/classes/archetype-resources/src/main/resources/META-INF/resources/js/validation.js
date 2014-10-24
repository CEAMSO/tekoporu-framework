
function validate(selector) {
	
	$(selector).validate(
			{	
				debug: true,
				
				rules: {
					firmaProtestante: {
			            required: true,
			            minlength: 3,
				        maxlength: 255
			        },
			        nombreparte: {
			            required: true,
				        maxlength: 255
			        },
			        nrodncp: {
			            required: true,
				        maxlength: 100
			        },
			        firmanteContestacion: {
			        	required: true,
			            minlength: 3,
				        maxlength: 255
			        },
			        telefono: {
			        	required: true,
				        maxlength: 255,
				        numeroTelefono: true
			        },
			        domicilio:{
			        	required: true,
				        maxlength: 255
			        },
			        descripcion:{
			        	required: true,
				        maxlength: 255
			        },
			        observacionesProcesos: {
			        	required: true,
				        maxlength: 500
			        },
			        limiteRanking: {
			        	required: true,
			        	max: 50
			        },

				},

				highlight : function(element) {
					$(element).closest('.field')
					.removeClass('has-success').addClass('has-error');
					//console.log(element);
					//$(element).parent().find('.form-control-feedback')
					//		.removeClass('glyphicon-ok').addClass(
					//				'glyphicon-remove');
					
					
				},
				unhighlight : function(element) {
					//console.log(element);
					$(element).closest('.field').removeClass('has-error')
							.addClass('has-success');
					/*
					if (!$(element).is('input, select, textarea')){
						var clasesParent = $.validator.prototype.getAllClasses(element, '.field');
						if ( clasesParent.search('checkgroup') >= 0) {
							console.log($($(element).parent().parent().parent().parent()[0]));
							$($(element).parent().parent().parent().parent()[0])
						}
					}
					*/
					$(element).closest('.field').find('.help-block').remove();
					//$(element).parent().find('.form-control-feedback')
					//		.removeClass('glyphicon-remove').addClass('glyphicon-ok');
				},
				// errorElement: 'span',
				errorClass: 'help-block',
				errorPlacement : function(error, element) {
					//console.log(element);
					var clases = element[0].getAttribute('class') || "";
					if (!$(element).is('input, select, textarea') &&
							!(clases.search('custom-validator') >= 0) ||
							($(element).is('input') && $(element).parent()[0].getAttribute('class') == 'ui-helper-hidden-accessible')){
						var clasesParent = $.validator.prototype.getAllClasses(element, '.field');
						if ( clasesParent.search('checkgroup') >= 0) {
							//console.log($($(element).parent().parent().parent().parent()[0]));
							$($(element).parent().parent().parent().parent().parent()[0]).append(error);
						} else if (clasesParent.search('selection') >= 0) {
							$($(element).parent().parent().parent()[0]).append(error);
						}
					} else {
						$(element).closest('.field').append(error);
					}
					/*
					
					
					if (element.parent('.input-group').length || element.parent('.input-group-btn').length) {
						error.insertAfter(element.parent());
					} else if (!$(element).is('input, select, textarea') &&
							!(clases.search('custom-validator') >= 0) ||
							($(element).is('input') && $(element).parent()[0].getAttribute('class') == 'ui-helper-hidden-accessible')){
						var clasesParent = $.validator.prototype.getAllClasses(element, '.field');
						if ( clasesParent.search('checkgroup') >= 0) {
							//console.log($($(element).parent().parent().parent().parent()[0]));
							$($(element).parent().parent().parent().parent().parent()[0]).append(error);
						} else if (clasesParent.search('selection') >= 0) {
							$($(element).parent().parent().parent()[0]).append(error);
						}
					} else {
						//error.insertAfter(element);
						element.parent().append(error);
					}
					*/
					
				},
				//onfocusout : function(){console.log($.validator.prototy.element(this));},
				submitHandler: function (form) {
					//console.log("submit");
			      if ($(form).valid()) {
			        form.submit();
			      }
			    }
			});
}

var num = 0;

function nameCustomElements() {
	var a = $('.custom-validator');
	
	
	a.each(function() {
		var name = "name_" + num;
		console.log(name);
		if (!$(this).attr('name')) {
			$(this).attr('name', name);
			num++;
		}

	});
}

function checkFile(idForm) {
	var formValid = true;
	if (idForm) {
		formValid = $('#' + idForm).valid();
	}
	var files = uploader.files[0];
	if (files) {
		$("#fileSelector").removeClass('has-error');
		$("#fileSelector").addClass('has-success');
		$("#fileSelector").find("#error").remove();
		return formValid;
	} else {
		$("#fileSelector").removeClass('has-success');
		$("#fileSelector").addClass('has-error');
		$("#fileSelector").find("#error").remove();
		var error = "<label id='error' class='help-block'>" + "Debe seleccionar un archivo" + "</label>";
		$("#fileSelector").append(error);
		return false;
	}
}

$(function() {
	$("#examinar").on("click", function() {
		uploader.clear();
	});
	
	$("input:file").change(function (){
	//var fileName = $(this).val();
	var files = $(uploader.files).last().get(0);
	if (files) {
		console.log(files);
		$("#fileSelector").removeClass('has-error');
		$("#fileSelector").addClass('has-success');
		$("#fileSelector").find("#error").remove();
		$('#nombreArchivo').val(files.name);
	} else {
		console.log(uploader.messageList);
		$("#fileSelector").addClass('has-error');
		$("#fileSelector").find("#error").remove();
		var error = "<label id='error' class='help-block'>" + $(uploader.messageList).find('li').last().html() + "</label>";
		$("#fileSelector").append(error);
		$('#nombreArchivo').val("");
	}
	
});
 });



$(document).ready( function () {
	validate('#editform');
	validate('#formulario');
	validate('#pruebaReporte');
	validate('#formularioDocumento');
});

