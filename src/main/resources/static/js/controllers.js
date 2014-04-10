var triviaControllers = angular.module('triviaControllers', ['wsStomp']);

triviaControllers.controller('AppCtrl', function($scope, $log, $http, $document, wsStompService) {
	
	$scope.game = {state: 'NOT_STARTED'};
		
	$scope.onMessage = function(msg) {
    	$scope.msg = JSON.parse(msg.body);
    	
    	if ($scope.msg.type == 'WAIT') {
    		var data = [{option: $scope.msg.question.optionA, num: $scope.msg.guesses[0]},
    	    		                 {option: $scope.msg.question.optionB, num: $scope.msg.guesses[1]},
    					    		 {option: $scope.msg.question.optionC, num: $scope.msg.guesses[2]},
    					             {option: $scope.msg.question.optionD, num: $scope.msg.guesses[3]}];
    		
    		$scope.game.state = 'WAIT';
    		
    	} else if ($scope.msg.type == 'QUESTION') {
    		// clear the last guess
    		$scope.game.lastGuess = null;
    		
    		$scope.question = $scope.msg.question;
    		$scope.game.correctAnswer = $scope.guessToQuestionText($scope.msg.question.correctAnswer);
    		
    		$scope.game.state = 'QUESTION';
    	} else if ($scope.msg.type == 'GUESS') {
    		// Don't show them yet, just save this off for when we get the WAIT message.
    		$scope.game.lastGuess = msg;
    		$scope.game.state = 'GUESSED';
    	}
    	
    	$scope.$apply();
    	
    	if ($scope.game.state == 'WAIT') {
    		$scope.drawGraph();
    	}
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
			$scope.game.state = 'GUESSED';
		});
		
	}
	
	$scope.startGame = function() {
		$http.post('/admin/start').success(function(response) {
			console.log(response);
		});
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
        wsStompService.subscribe('/topic/trivia', $scope.onMessage);
	}
	
	$scope.onClose = function(error) {
		$scope.game.state = 'ERROR';
		$scope.game.error = 'You have been disconnected!';
	}
	
	// Start the STOMP service
	wsStompService.connect('/trivia', {}, $scope.onConnection, $scope.onClose);
	
	$scope.drawGraph = function() {
		var data =  [{data: [[0, $scope.msg.guesses[0]]], label: $scope.msg.question.optionA},
	                 {data: [[0, $scope.msg.guesses[1]]], label: $scope.msg.question.optionB},
	                 {data: [[0, $scope.msg.guesses[2]]], label: $scope.msg.question.optionC},
	                 {data: [[0, $scope.msg.guesses[3]]], label: $scope.msg.question.optionD}
	                ];
		
		// Explode the piece representing the correct answer
		var correctAnswerIdx = $scope.msg.question.correctAnswer.charCodeAt(0) - 'A'.charCodeAt(0);
		data[correctAnswerIdx].pie = { explode: 20 };
		
		$('#chart').width($('#chart').parent().width());
		
		var graph = Flotr.draw($('#chart').get(0), data,{
			        title: 'How players responded',
	    			HtmlText : false,
	    		    grid : {
	    		      verticalLines : false,
	    		      horizontalLines : false
	    		    },
	    		    xaxis : { showLabels : false },
	    		    yaxis : { showLabels : false },
	    		    pie : {
	    		      show : true, 
	    		      explode : 2
	    		    },
	    		    mouse : { track : true },
	    		    legend : {
	    		      position : 'se',
	    		      backgroundColor : '#D2E8FF'
	    		    }
	    		  });
	}
});

triviaControllers.controller('DashboardCtrl', function($scope, $http, wsStompService) {
	$scope.users = [];
	$scope.game = null;
	
	$scope.sortUsersBy = 'name';
	
	$http.get('/admin/game').success(function(data) {
		$scope.game = data;
		$scope.gameStarted = data.state != 'NOT_STARTED' && data.state != 'FINISHED';
	});
	
	
	$http.get('/admin/users').success(function(data) {
		$scope.users = data;
	});
	
	$scope.post = function(url) {
		$http.post(url);
	}
	
	$scope.editUser = function(user) {
		$scope.error = null;
		$scope.currentUser = user;
		$('#editUser').modal();
	}
	
	$scope.submitUserEdit = function() {
		$http.post('/admin/user', $scope.currentUser)
			.success(function(data) {
				console.log("OK: ", data);
			});
	}
	
	$scope.deleteUser = function(user) {
		$http.post('/admin/users/delete/' + user.id)
			.success(function(data) {
				
				for (var i = 0; i < $scope.users.length; i++) {
					if ($scope.users[i].name == user.name) {
						$scope.users.splice(i, 1);
					}
				}
				
				$('#editUser').modal('hide');
			})
			.error(function(data, status) {
				console.log('Delete User failed', data, status);
				$scope.error = "Unable to delete user."
			});
	}
	
	$scope.resetAllScores = function() {
		$http.post('/admin/users/scores/reset').success(function(data) {
			for (var i = 0; i < $scope.users.length; i++) {
				$scope.users[i].score = 0;
			}
		});
	}
	
	$scope.deleteAllUsers = function() {
		$http.post('/admin/users/delete/all').success(function(data) {
			var admin = $.grep($scope.users, function(u) { return u.name == 'admin'});
			$scope.users = [admin];
		});
	}
	
	
	$scope.editQuestion = function(q) {
		$scope.error = null;
		$scope.currentQuestion = q;
		$('#editQuestion').modal();
	}
	
	$scope.submitQuestionEdit = function() {
		$http.post('/admin/question', $scope.currentQuestion)
			.success(function(data) {
				console.log("OK: ", data);
			});
	}
	
	$scope.editGame = function(g) {
		$scope.error = null;
		$scope.currentGame = g;
		console.log($scope.currentGame);
		$('#editGame').modal();
	}
	
	$scope.submitGameEdit = function() {
		$http({
		    method: 'POST',
		    url: '/admin/game',
		    data: $.param({'numSecondsPerQuestion': $scope.currentGame.numSecondsPerQuestion}),
		    headers: {'Content-Type': 'application/x-www-form-urlencoded'}
		}).error(function(data, status) {
			console.log('Update Game failed', data, status);
			$scope.error = "Unable to update game."
		});
	}
	
	$scope.onMessage = function(rawMsg) {
		var msg = JSON.parse(rawMsg.body);
		$scope.game.state = msg.type;
		
		if (msg.type == 'QUESTION') {
			$scope.game.currentQuestionIdx = msg.question.id;
		} else if (msg.type == 'JOINED') {
			$scope.users.push(msg.user);
		}
		
		$scope.$apply();
	}
	
	$scope.onConnection = function(frame) {
        wsStompService.subscribe('/topic/trivia', $scope.onMessage);
        wsStompService.subscribe('/topic/joined', $scope.onMessage);
	}
	
	$scope.onClose = function(error) {
		$scope.game.state = 'ERROR';
		$scope.game.error = 'You have been disconnected!';
	}
	
	// Start the STOMP service
	wsStompService.connect('/trivia', {}, $scope.onConnection, $scope.onClose);
});
