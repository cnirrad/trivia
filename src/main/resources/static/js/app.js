'use strict';

var triviaApp = angular.module('triviaApp', [
                                             "triviaControllers",
                                             "wsStomp"
                                             ]);


angular.module('wsStomp', []).factory('wsStompService', function() {
	var state = {};
	var stompClient = null;
	
	return {
		connect: function(path, headers, onConnection, onClose) {
			var socket = new SockJS(path);
			
			socket.onclose = function() {
				onClose();
			}
			
	        stompClient = Stomp.over(socket);            
	        stompClient.connect(headers, onConnection, onClose);
		},
		
		subscribe: function(path, callback) {
			stompClient.subscribe(path, callback);
		}
	};
});