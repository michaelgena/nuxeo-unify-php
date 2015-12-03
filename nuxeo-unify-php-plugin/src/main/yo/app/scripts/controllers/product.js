'use strict';

angular.module('frontApp')
	.controller('ProductCtrl', function($scope, $routeParams, nuxeoClient) {
	//$scope, $routeParams, $sce, $location, nuxeoClient
	// Init vars
	$scope.product = {};
	$scope.documents = {};
	/*$scope.helpers = {
	    download: function(uid, fileName) {
	      url = "../../../nuxeo/nxfile/default/"+uid+"/file:content/"+fileName;
	      document.location.href=url;
	  }
	};*/

	$scope.formatDate = function(date){
        var dateOut = new Date(date);
        return dateOut;
  };

	$("#loader").hide("");

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

	$('.ui.bottom.attached.tabular.menu .item').tab();
	//query/QueryForProductDocuments?queryParams=515258

	// Main callbacks
	function callbackLists(lists) {
		$scope.documents = lists.entries;
		console.log(lists.entries);
	}

	function errorLists(err) {
		console.log('Error while fetching lists of documents: ' + err);
	}

	// Main callback
  function callbackAkeneo(result) {
    var json = JSON.parse(result);
    $scope.product = json.products[0];
		console.log($scope.product.attributes);
    $("#loader").hide("");
    //console.log("Login:" +$cookies.get("login"));
  }

	nuxeoClient.request('query/QueryForProductDocuments?queryParams='+$routeParams.pid).schema('file').get().then(callbackLists, errorLists);
	nuxeoClient.operation("AKENEO.QueryAndFetch").param("query", $routeParams.pid).execute().then(callbackAkeneo);
});
