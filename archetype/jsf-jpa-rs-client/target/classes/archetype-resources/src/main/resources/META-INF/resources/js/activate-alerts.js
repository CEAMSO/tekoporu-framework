//permite mostrar las opciones de selección en los modals
PrimeFaces.zindex=6000;

//Para mostrar la barra de progreso durante la descarga de archivos 
function showStatus() {
	$('#status').show();
}
	
function hideStatus() { 
	$('#status').hide();
	$('#plantillaResInicialModal').modal('hide');	
}

//Para ajustar el height de la página según aparezcan mensajes
function bindEventClose(alert) {
	alert.bind('closed.bs.alert', function () {
    	var margin = $('#contenido').css('marginTop').replace("px", "");
    	var marginTop = parseInt(margin);
    	var height = alert.outerHeight(true);
    	$('#contenido').css('marginTop', marginTop - height);
    });
}

function setMarginTop(alert) {
    var margin = $('#contenido').css('marginTop').replace("px", "");
    var height = alert.outerHeight(true);

    var marginTop = parseInt(margin) + height;
    $('#contenido').css('marginTop', marginTop + "px");
}


//para crear mensajes de alerta
function createAutoClosingAlert(msg, styleClass, delay) {
	if (!delay) {
		delay = 5000;
	}
	
	var message = $('<div class="alert ' + styleClass + ' fade in alert-dismissable" style="display: none;" >');
	var close = $('<button type="button" class="close" data-dismiss="alert">&#215;</button>');
	message.append(close); // boton de cerrar
	message.append(msg); // agregando el mensaje
	message.appendTo($('#contenedorMensajes')).fadeIn(300); //mostrar el mensaje
	setMarginTop(message);
	bindEventClose(message);
	message.delay(delay).fadeOut("slow", function () { $(this).alert('close'); });

}


function validateExtras(args) {
	console.log('validateExtras');
	if  (args.validationDetails) {
		var obj = JSON.parse(args.validationDetails);
		//"{'checks':[{'resultados': false}]}");
		console.log(obj);
		$.each(obj, function(key, val) {
			if (key == "checks") {
				$.each(val, function(key, value) {
					console.log(key);
					console.log(value);
					for (var property in value) {
						if (value[property]) {
							$('#' + property + "_data").addClass('has-success');
							console.log($('#' + property + "_data"));
						} else {
							console.log('#' + property + "_data");
							$('#' + property + "_data").addClass('has-error');
							console.log($('#' + property + "_data"));
						}
					}
					
					
				});
			}
		});
	}
}


//handle request para los Ajax de primefaces.
//Despliega los mensajes de alertas según el resultado
function handleSaveRequest(xhr, status, args, mensaje) {  
	/*console.log(xhr);
	console.log(status);
	console.log(args);*/
	if (args.validationFailed) {
		console.log("validation fail");
	}
	
	validateExtras(args); 
	
    if (status === "success") {
    	 if (args.exito)  {
    		 if (mensaje) {
    			 createAutoClosingAlert(mensaje, "alert-success", 5000);
    		 } else if (args.mensaje){
    			 createAutoClosingAlert(args.mensaje, "alert-success", 5000);
    		 }
    		
    	 } else if (args.excepcion) {
    		 createAutoClosingAlert("Ha ocurrido un problema", "alert-danger", 5000);
    	 } else if (args.validationFailed) {
    		 if (args.mensaje) {
    			 createAutoClosingAlert(args.mensaje, "alert-warning", 5000);
    		 } else if (mensaje){
    			 createAutoClosingAlert("Errores en el formulario", "alert-warning", 2000);
    		 } else {
    			 createAutoClosingAlert("Errores en el formulario", "alert-warning", 2000);
    		 }
    		 
    	 } else if (args.finalizarFailed) {
    		 createAutoClosingAlert(args.mensaje, "alert-danger", 5000); 
    	 } else if(args.tipoDocInvalido) {
    		 createAutoClosingAlert("Debe seleccionar un documento con extensión .docx", "alert-warning", 2000);
    	 } else if(args.descripcionInvalida) {
    		 createAutoClosingAlert("Debe agregar una descripción", "alert-warning", 2000);
    	 } else if(args.eliminarParteFailed){
    		 createAutoClosingAlert("No se puede eliminar la parte porque está siendo referenciada por un evento", "alert-warning", 2000);
    	 }
    	
    } else {
    	createAutoClosingAlert("Ha ocurrido un problema", "alert-danger", 5000);
    }

}

function disableBtn(arg){
	if(arg){
		$('.btn').not('.descargar').attr('disabled', 'disabled');
		//$(':checkbox').attr('disabled', 'disabled');
		//$('textarea').attr('disabled', 'disabled');
	} else {
		$('.btn').attr('disabled', 'disabled');
		//$(':checkbox').attr('disabled', 'disabled');
	}
	
}

function enableBtn(arg){
	if(arg){
		$('.btn').not('.descargar').removeAttr('disabled');
		//$(':checkbox').removeAttr('disabled');
		//$('textarea').removeAttr('disabled');
	}else{
		$('.btn').removeAttr('disabled');
		//$(':checkbox').removeAttr('disabled');
	}
	
}


function checks() {
	var a = $('.check-group');
	
	a.each(function() {
		var checked = $(this).find(':checked');
		var size = $(this).find(':checkbox');
		console.log(checked);
		console.log(checked.length);
		console.log(size.length);
		if (checked.length == 0) {
			$(this).css('color', 'red');
		}
	});
}