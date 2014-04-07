//$(document).ready(function() { 
//	
//	$('#startGame').click(function() {
//		$.ajax({
//			url: "/admin/startGame",
//			type: "POST",
//			success: function(result) {
//				$('#gameState').text('STARTING');
//			}
//		})
//	});
//	
//	$('#resetGame').click(function() {
//		$.ajax({
//			url: "/admin/reset",
//			type: "POST",
//			success: function(result) {
//				$('#gameState').text('NOT_STARTED');
//			}
//		})
//	});
//});
var triviaApp = angular.module('triviaApp', []);

triviaApp.controller('DashboardCtrl', function($scope, $http) {
	$http.get('/admin/game').success(function(data) {
		$scope.game = data;
		$scope.gameStarted = data.state != 'NOT_STARTED' && data.state != 'FINISHED';
	});
	
	$scope.users = [];
	
	$scope.post = function(url) {
		$http.post(url);
	}
});

