$(document).ready(function() {  
	
	var createCookie = function(name, value, days) {
	    var expires;

	    if (days) {
	        var date = new Date();
	        date.setTime(date.getTime() + (days * 24 * 60 * 60 * 1000));
	        expires = "; expires=" + date.toGMTString();
	    } else {
	        expires = "";
	    }
	    document.cookie = escape(name) + "=" + escape(value) + expires + "; path=/";
	}

	var readCookie = function(name) {
	    var nameEQ = escape(name) + "=";
	    var ca = document.cookie.split(';');
	    for (var i = 0; i < ca.length; i++) {
	        var c = ca[i];
	        while (c.charAt(0) === ' ') c = c.substring(1, c.length);
	        if (c.indexOf(nameEQ) === 0) return unescape(c.substring(nameEQ.length, c.length));
	    }
	    return null;
	}
	
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
	
	var password = readCookie('triviaName');
	if (password == null) {
		password = guid();
		createCookie('triviaName', password, 1);
	}
	
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