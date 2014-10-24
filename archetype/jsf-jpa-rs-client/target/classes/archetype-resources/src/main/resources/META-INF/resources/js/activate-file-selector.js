/*
(document)
    .on('change', '.btn-file :file', function() {
        var input = $(this),
            numFiles = input.get(0).files ? input.get(0).files.length : 1,
            label = input.val().replace(/\\/g, '/').replace(/.*\//, '');
            console.log(numFiles);
            console.log(label);
        input.trigger('fileselect', [numFiles, label]);
});

$(document).ready( function() {
    $('.btn-file :file').on('fileselect', function(event, numFiles, label) {
    	console.log(event);
    	console.log(numFiles);
        console.log(label);
        $(".fileselected").val(label);
    });
});
*/
$(function() {
	$("#fileUpload").hide();
	$("#examinar").on("click", function() {
		$("#fileUpload_input").click();
	});
});

/*
$('.ui-fileupload-upload')[0].click()

uploader.files[0]
*/