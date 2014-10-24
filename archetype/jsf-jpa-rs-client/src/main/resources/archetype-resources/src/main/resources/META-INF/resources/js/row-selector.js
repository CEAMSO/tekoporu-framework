
$(function() {
	$('#tablaLotes tr').on('click', function() {
		$('#tablaLotes tr.selected-row').removeClass("selected-row");
		$(this).addClass("selected-row"); 
	});
});

