'use strict';

angular.module('frontApp')
	.controller('SubscriptionsCtrl', function($scope, nuxeoClient) {
	//$scope, $routeParams, $sce, $location, nuxeoClient
	// Init vars
	$scope.subscriptions = {};

	  // Main callback
	  function callback(result) {
	    $scope.subscriptions = result.entries;
	    $("#loader").hide("");
	  }

	  // Entry point
	  nuxeoClient.request('query/Subscriptions').schema('file').get().then(callback);

});
