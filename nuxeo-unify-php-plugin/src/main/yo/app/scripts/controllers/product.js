'use strict';

angular.module('frontApp')
	.controller('ProductCtrl', function($sce, $scope, $routeParams, nuxeoClient) {
	//$scope, $routeParams, $sce, $location, nuxeoClient
	// Init vars
	$scope.product = {};
	$scope.documents = {};

	$scope.formatDate = function(date){
  	var dateOut = new Date(date);
    return dateOut;
  };

	$scope.renderHTML = function(html_code){
    var decoded = angular.element('<textarea />').html(html_code).text().replace("<br>","");
    return $sce.trustAsHtml(decoded);
  };

	$scope.typeMatcher = function(typeFilter) {
  	return function(doc) {
			//console.log("result:"+typeFilter+" "+doc.properties['Document_Attributes:Document_Mapping']);
    	return doc.properties['Document_Attributes:Document_Mapping'] === typeFilter;
	  }
	};

	$("#loader").hide("");

	$('.ui.top.attached.tabular.menu .item').tab();

	$('.ui.bottom.attached.tabular.menu .item').tab();
	//query/QueryForProductDocuments?queryParams=515258

	// Main callbacks
	function callbackLists(lists) {
		$scope.documents = lists.entries;
		//console.log(lists.entries);
	}

	function errorLists(err) {
		console.log('Error while fetching lists of documents: ' + err);
	}

	// Main callback
  function callbackAkeneo(result) {
    var json = JSON.parse(result);
    $scope.product = json.products[0];
		//console.log($scope.product.attributes.version_description[""].en_US);
    $("#loader").hide("");
  }

	nuxeoClient.request('query/QueryForProductDocuments?queryParams='+$routeParams.pid).schema('file').get().then(callbackLists, errorLists);
	nuxeoClient.operation("AKENEO.QueryAndFetch").param("pid", $routeParams.pid).param("type", "").param("object_type", "").param("code", "").param("category_code", "").param("version_to_product", "").execute().then(callbackAkeneo);
});
