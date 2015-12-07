'use strict';

angular.module('frontApp')
	.controller('SalesCircularsCtrl', function($scope, nuxeoClient) {
	//$scope, $routeParams, $sce, $location, nuxeoClient
	// Init vars
	$scope.documentsRelease = {};
	$scope.documentsPush = {};
	$scope.documentsPhaseOut = {};

	$scope.sortType     = 'title'; // set the default sort type
	$scope.sortReverse  = false;  // set the default sort order


	$scope.formatDate = function(date){
				var dateOut = new Date(date);
				return dateOut;
	};

	$('.ui.top.attached.tabular.menu .item')
  .tab({
    cache: false,
    // faking API request
    apiSettings: {
      loadingDuration : 300,
      mockResponse    : function(settings) {
        var response = {
          first  : 'AJAX Tab One',
          second : 'AJAX Tab Two',
          third  : 'AJAX Tab Three'
        };
        return response[settings.urlData.tab];
      }
    },
    context : 'parent',
    auto    : true,
    path    : '/'
  });

	  // Main callback
	  function callbackRelease(result) {
	    $scope.documentsRelease = result.entries;
	    $("#loader").hide("");
	  }

		// Main callback
	  function callbackPush(result) {
	    $scope.documentsPush = result.entries;
	    $("#loader").hide("");
	  }
		// Main callback
	  function callbackPhaseOut(result) {
	    $scope.documentsPhaseOut = result.entries;
	    $("#loader").hide("");
	  }
	  // Entry point
	  nuxeoClient.request('query/QueryForSalesCircularDocuments?queryParams='+encodeURIComponent('%release')).schema('file').get().then(callbackRelease);
	  nuxeoClient.request('query/QueryForSalesCircularDocuments?queryParams='+encodeURIComponent('%push')).schema('file').get().then(callbackPush);
	  nuxeoClient.request('query/QueryForSalesCircularDocuments?queryParams='+encodeURIComponent('%phaseout')).schema('file').get().then(callbackPhaseOut);

});
