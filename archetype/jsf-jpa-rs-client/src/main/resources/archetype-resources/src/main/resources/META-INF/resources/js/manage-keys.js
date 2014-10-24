$(document).ready(function () {
	 $('.ui-autocomplete-input').keydown(function(event) {
         //if (event.keyCode == 13 && $('#noEnter').length) {
         if (event.keyCode == 13) {
        	 console.log("enter apretado");
        	 $('.next-autocomplete').first().focus();
             event.preventDefault();
             return false;
         }
     });
	 
	 $('input').on("keypress", function(e) {
         // ENTER PRESSED
         if (e.keyCode == 13) {
             // FOCUS ELEMENT 
        	 //console.log("enter apretado input");
             var inputs = $(this).parents("form").eq(0).find(":input");
             var idx = inputs.index(this);
             
             //console.log(inputs);
             if (idx == inputs.length - 1) {
                 inputs[0].select();
             } else {
                 inputs[idx + 1].focus(); //  handles submit buttons
                 inputs[idx + 1].select();
             }
             return false;
         }
     });
});

