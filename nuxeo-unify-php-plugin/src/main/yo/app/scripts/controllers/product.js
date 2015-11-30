'use strict';

angular.module('frontApp')
	.controller('ProductCtrl', function($scope) {
	//$scope, $routeParams, $sce, $location, nuxeoClient
	// Init vars
	$scope.product = {};
	$("#loader").hide("");
	//$('.menu .item').tab();

	$('.menu .item')
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

	//nuxeoClient.operation("AKENEO.GetProduct").execute(callbackGPA);
});
