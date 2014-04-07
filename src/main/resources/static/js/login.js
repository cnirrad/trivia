$(document).ready(function() {  
	
	// approximation of a GUID taken from 
	// http://stackoverflow.com/a/105074
	var guid = function() {
		  function s4() {
		    return Math.floor((1 + Math.random()) * 0x10000)
		               .toString(16)
		               .substring(1);
		  }
		  return s4() + s4() + '-' + s4() + '-' + s4() + '-' +
		         s4() + '-' + s4() + s4() + s4();
		}
	
	//TODO: read from cookie, create and save if not present
	var password = guid();
	
	// Disable the button until the user has entered a name
	$('#username').on('keyup blur', function(){
		if (this.value.trim().length == 0) {
			$('#nameBtn').addClass('ui-disabled');
		} else if ($('#username').val() == "admin") {
			$('#password').attr('type', 'password').focus();
			
		} else {
			$('#nameBtn').removeClass('ui-disabled');
		}
	});
	$('#nameBtn').addClass('ui-disabled');
	

	
	$('#password').val(password);
});