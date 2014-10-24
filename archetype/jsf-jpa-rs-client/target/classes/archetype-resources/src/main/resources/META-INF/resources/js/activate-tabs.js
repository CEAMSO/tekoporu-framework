
$('.nav-tabs a').click(function (e) {
  e.preventDefault();
  $(this).tab('show');
});
/*
$('#guardar').click(function (e) {
  e.preventDefault();
  $('#tabPrincipal a[href="#documentos"]').tab('show');
  $(".alert").alert();
});
*/
/* si tiene hash se muestra el mensaje */
/*
var pos =  window.location.search.substring(1).search("hash");

if (pos >= 0) {
	$('.alert').show();
}
*/
// store the currently selected tab in the hash value
$("ul.nav-tabs > li > a").on("shown.bs.tab", function (e) {
    var id = $(e.target).attr("href");//.substr(1);
    console.log(id);
    window.location.replace(id);
});

// on load of the page: switch to the currently selected tab
var hash = window.location.hash;
$(document).ready(function() {
    if(location.hash) {
       $('a[href=' + location.hash + ']').tab('show');
    }
});

//sacar ticket de CAS 

var query = window.location.search.substring(1);
var ticket = query.search("ticket");

if (ticket > 0) {
	location.search=location.search.replace(/&?ticket=([^&]$|[^&]*)/i, "");
} else if (ticket == 0 ) {
	var vars = query.split("&");
	if (vars.length > 1) {
		location.search=location.search.replace(/&?ticket=([^&]$|[^&]*)/i, "");
	} else {
		window.location = window.location.pathname;	
	}
}


/*
$('#guardar').click( function() {
	window.location.hash = 'documentos';   
}); 
*/

//$('#tabPrincipal a[href="' + hash + '"]').tab('show');
