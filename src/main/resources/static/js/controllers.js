var triviaControllers = angular.module('triviaControllers', ['wsStomp']);

triviaControllers.controller('AppCtrl', function($scope, $log, wsStompService) {
	
	$scope.game = {state: 'NOT_STARTED'};
	
	$scope.onMessage = function(msg) {
    	$scope.game = JSON.parse(JSON.parse(msg.body));
    	
    	if ($scope.game.state == 'WAIT') {
    		//$(".app").pagecontainer("change", "#wait", { transition: 'slide' });
    		$.mobile.changePage('#wait');
    	} else if ($scope.game.state == 'QUESTION') {
    		// changePage is deprecated, but using pagecontainer is causing me issues
    		//$(".app").pagecontainer("change", "#question", { transition: 'slide' });
    		$.mobile.changePage('#question');
    	}
    	
    	$scope.$apply();
	}
	
	$scope.onConnection = function(frame) {
        //setConnected(true);
		$scope.game.state = 'CONNECTED';
		$scope.$apply();
        wsStompService.subscribe('/topic/trivia', $scope.onMessage);
       
	}
	
	$scope.onClose = function(error) {
		// TODO: Tell the user!
		$scope.game.state = 'DISCONNECTED';
		$log.warn('Connection failed!', error);
	}
	$( ".app" ).pagecontainer({ defaults: true });
	
	// Start the STOMP service
	wsStompService.connect('/trivia', {}, $scope.onConnection, $scope.onClose);
});

triviaControllers.controller('DashboardCtrl', function($scope, $http) {
	$http.get('/admin/game').success(function(data) {
		$scope.game = data;
		$scope.gameStarted = data.state != 'NOT_STARTED' && data.state != 'FINISHED';
	});
	
	$scope.users = [];
	
	$scope.post = function(url) {
		$http.post(url);
	}
});

triviaControllers.controller('WaitCtrl', function($scope, $http) {
	
});