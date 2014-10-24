
$('.input-group.date').datepicker({
    language: "es", autoclose: true, todayHighlight: true, format: "dd-mm-yyyy"
});

$(document).ready( function () {
	$('div.bfh-datepicker').each(function () {
		var $datepicker;
		$datepicker = $(this);
		$datepicker.bfhdatepicker($datepicker.data());
	});
});

$(document).ready(function() {
	$('div.bfh-timepicker').each(function() {
		var $timepicker;
		$timepicker = $(this);
		$timepicker.bfhtimepicker($timepicker.data());
	});
});

$('.fecha').attr("readonly", true);
$('.hora').attr("readonly", true);

$('.submit').click( function() {
	$('.fecha').attr("readonly", false);
	$('.hora').attr("readonly", false);
}); 
