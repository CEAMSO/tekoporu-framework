var replaceHtmlEntites = (function() {
	  var translate_re = /&(nbsp|amp|quot|lt|gt);|(“)|(”)/g;
	  var translate = {
	    "nbsp": "&#160;",
	    "amp" : "&#38;",
	    "quot": "&#34;",
	    "lt"  : "&#60;",
	    "gt"  : "&#62;",
	    "“" : "&#8220;",
	    "”" : "&#8221;"
	  };
	  return function(s) {
	    return ( s.replace(translate_re, function(match, entity) {
	    	//var a = translate[entity||match];
	    	//console.log(a);
	    	//return a;
	    	return translate[entity||match];
	    }) );
	  }
})();

/*
$(document).ready(function() {
	CKEDITOR.replaceAll( 'ck-editor' );
	for(var i in CKEDITOR.instances) {
		var actual = CKEDITOR.instances[i];
		actual.on('change', function( evt ) {
			// getData() returns CKEditor's HTML content.
			var data = actual.getData();
			var inputArea = $("#" + actual.name);
			inputArea.val(replaceHtmlEntites(data));
		});
	}

});
*/
function destroyCKEditors() {

	for(var i in CKEDITOR.instances) {
		var inputArea = $("#" + CKEDITOR.instances[i].name);
		
		CKEDITOR.instances[i].destroy(true);
		inputArea.hide();
	}
}

function createCKEditors() {
	var editors = $('.ck-editor').not(':disabled');
	
	editors.each(function() {
		createCKEditorWithTextArea(this);
	});
}


function createCKEditorsReadOnly() {

	var editors = $('.ck-editor:disabled');
	
	editors.each(function() {
		var id = this.getAttribute('id');
		var editor = CKEDITOR.instances[id];
		
	    if (editor) { 
	    	editor.destroy(true);
	    }
	    
		editor = CKEDITOR.replace(this);
		
	});
	
	
}



function createCKEditorWithID(id) {
	var editor = CKEDITOR.instances[id];
	
    if (editor) { 
    	editor.destroy(true);
    }
    
	editor = CKEDITOR.replace(id);
    var inputArea = $("#" + id);
    editor.on('change', function() {
    	data = editor.getData();
    	inputArea.val(replaceHtmlEntites(data));
    });
}

function createCKEditorWithTextArea(textarea) {
	var id = textarea.getAttribute('id');
	
	if (textarea) {
		
		var editor = CKEDITOR.instances[id];
		
	    if (editor) { 
	    	editor.destroy(true);
	    }
	    
		editor = CKEDITOR.replace(textarea);
		var inputArea = $('#' + id);
	    editor.on('change', function() {
	    	data = editor.getData();
	    	inputArea.val(replaceHtmlEntites(data));
	    });
	}
}



function fixHtmlEntities() {
    var inputArea = $('.ck-editor');
    inputArea.each(function(index, element) {
    	data = this.val();
    	this.val(replaceHtmlEntites(data));
    });
}

$(document).ready(function() {
	createCKEditors();
	createCKEditorsReadOnly();
});