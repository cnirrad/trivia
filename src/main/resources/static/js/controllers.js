var triviaControllers = angular.module('triviaControllers', ['wsStomp']);

triviaControllers.controller('AppCtrl', function($scope, $log, $http, wsStompService) {
	
	$scope.game = {state: 'NOT_STARTED'};
	
	$scope.dataSource =  [
	                                      { country: 'Africa', area: 20.2 },
	                                      { country: 'Antarctica', area: 8.9 },
	                                      { country: 'Asia', area: 30 },
	                                      { country: 'Australia', area: 5.3 },
	                                      { country: 'Europe', area: 6.7 },
	                                      { country: 'North America', area: 16.5 },
	                                      { country: 'South America', area: 12 }
	                                  ];
	
	$scope.onMessage = function(msg) {
    	$scope.msg = JSON.parse(JSON.parse(msg.body));
    	
    	if ($scope.msg.type == 'WAIT') {
    		//$(".app").pagecontainer("change", "#wait", { transition: 'slide' });
    		
//    		var data = [{option: $scope.msg.question.optionA, num: $scope.msg.guesses[0]},
//    	    		                 {option: $scope.msg.question.optionB, num: $scope.msg.guesses[1]},
//    					    		 {option: $scope.msg.question.optionC, num: $scope.msg.guesses[2]},
//    					             {option: $scope.msg.question.optionD, num: $scope.msg.guesses[3]}];
    		
    		var data = [[ $scope.msg.question.optionA, $scope.msg.guesses[0] ],
		                 [$scope.msg.question.optionB,  $scope.msg.guesses[1]],
			    		 [$scope.msg.question.optionC,  $scope.msg.guesses[2]],
			             [$scope.msg.question.optionD,  $scope.msg.guesses[3]]];
//    		
//    		$.jqplot('chart', data, {
//    			title: 'Guesses',
//    			seriesDefaults: {
//		          shadow: false, 
//		          renderer: jQuery.jqplot.PieRenderer, 
//		          rendererOptions: { 
//		            sliceMargin: 4, 
//		            showDataLabels: true
//		          } 
//		        }, 
//		        legend: { show:true, location: 'e' }
//    		});
    		$.jqplot('chart',  [[[1, 2],[3,5.12],[5,13.1],[7,33.6],[9,85.9],[11,219.9]]]);
    		
    		$.mobile.changePage('#wait');
    	} else if ($scope.msg.type == 'QUESTION') {
    		// changePage is deprecated, but using pagecontainer is causing me issues
    		//$(".app").pagecontainer("change", "#question", { transition: 'slide' });
    		
    		// clear the last guess
    		$scope.game.lastGuess = null;
    		
    		$scope.question = $scope.msg.question;
    		$scope.game.correctAnswer = $scope.guessToQuestionText($scope.msg.question.correctAnswer);
    		$.mobile.changePage('#question');
    	} else if ($scope.msg.type == 'GUESS') {
    		// Don't show them yet, just save this off for when we get the WAIT message.
    		$scope.game.lastGuess = msg;
    	}
    	
    	$scope.$apply();
	}
	
	$scope.makeGuess = function(guess) {
		var msg = JSON.stringify({'questionNumber': $scope.question.id, 'guess': guess});
		$scope.game.lastGuess = {'guess': guess};
		
		//wsStompService.send('/app/guess', msg);
		
		// TODO: using http instead of WS because I can't get a request-reply pattern to work
		$http.post('/guess', msg).success(function(response) {
			console.log(response);
			$scope.game.lastGuess = response
			
			// Mark it as correct if the guess is equal to the correct answer AND 
			// the server recognized it as correct (and sent in time) by giving us points.
			$scope.game.lastGuess.correct = (guess == $scope.question.correctAnswer && response.points > 0);
		});
		
		$.mobile.changePage('#alreadyGuessed');
	}
	
	$scope.guessToQuestionText = function(guess) {
		var answerText = "";
		if (guess == "A") {
			answerText = $scope.question.optionA;
		} else if (guess == "B") {
			answerText = $scope.question.optionB;
		} else if (guess == "C") {
			answerText = $scope.question.optionC;
		} else if (guess == "D") {
			answerText = $scope.question.optionD;
		}
		return answerText;
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
		$.mobile.changePage('#errorPanel');
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