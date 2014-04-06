$(document).ready(function() {  
	var stompClient = null;
	

	
	var onTriviaMessage = function(message) {
		var response = document.getElementById('question');
        var p = document.createElement('p');
        p.style.wordWrap = 'break-word';
        p.appendChild(document.createTextNode(message));
        response.appendChild(p);
	}
	
	var onConnection = function(frame) {
        //setConnected(true);
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/trivia', function(greeting){
        	onTriviaMessage(JSON.parse(greeting.body));
        });
       
	}
	
	var onConnectionError = function(error) {
		console.log('Connection failed!', error);
	}
	
	var connect = function() {
		var socket = new SockJS('/trivia');
        stompClient = Stomp.over(socket);            
        stompClient.connect({}, onConnection);
	}
	
	connect();
});