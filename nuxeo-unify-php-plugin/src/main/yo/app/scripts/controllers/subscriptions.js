'use strict';

angular.module('frontApp')
	.controller('SubscriptionsCtrl', function($scope, nuxeoClient) {
	//$scope, $routeParams, $sce, $location, nuxeoClient
	// Init vars
	$scope.subscriptions = {};
	$scope.notificationScopes = {};

	  // Main callback
	  function callback(result) {
	    $scope.subscriptions = result.entries;
	    $("#loader").hide("");
	  }

		$scope.unsubscribe = function (id) {
				nuxeoClient.operation("javascript.unsubscribe").param("id", id).execute();
				$("#subscription-"+id).html("Unsubscribed").addClass("disabled");
				$("#scope-"+id).addClass("disabled");
		};

		$scope.changeScope = function (id) {

		};

		function callbackNotifScope(result) {
			$scope.notificationScopes = result.entries;
		}

	  // Entry point
	  nuxeoClient.request('query/Subscriptions').schema('file').get().then(callback);

		nuxeoClient.request('directory/Notification_Scope').get().then(callbackNotifScope);

});
