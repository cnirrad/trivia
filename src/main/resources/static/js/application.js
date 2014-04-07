$(document).ready(function() {  
	var stompClient = null;
	

	
	var onTriviaMessage = function(m) {
		// Not sure why it needs to be parsed again...
		var message = JSON.parse(m);
		
        var state = message["state"];
        console.log("State = " +  message["state"]);
        
        if (state == 'QUESTION') {
        	console.log("New question has been given!");
        	$('#answerAText').text(message.question.optionA);
        	$('#answerBText').text(message.question.optionB);
        	$('#answerCText').text(message.question.optionC);
        	$('#answerDText').text(message.question.optionD);
        	$('#questionText').text(message.question.text);
            $.mobile.changePage("#question", { allowSamePageTransition: true });
        } else if (state == 'WAIT') {
        	$.mobile.changePage("#waiting", { allowSamePageTransition: true });
        }
        
        
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
	
	var connect = function(path, headers, onConnection, onError) {
		var socket = new SockJS(path);
		
		socket.onclose = function() {
			console.log("Socket closed!");
		}
		
        stompClient = Stomp.over(socket);            
        stompClient.connect(headers, onConnection, onError);
	}
	
	connect();
});