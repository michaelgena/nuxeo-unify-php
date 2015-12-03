'use strict';

/**
 * @ngdoc function
 * @name frontjcdApp.controller:MainCtrl
 * @description
 * # MainCtrl
 * Controller of the frontApp
 */
angular.module('frontApp')
  .controller('MainCtrl', function ($scope, $cookies, nuxeoClient) {
 //$scope, $routeParams, $sce, $location, nuxeoClient

  $scope.products = {};

  $scope.sortType     = 'attributes.product_version_name'; // set the default sort type
  $scope.sortReverse  = false;  // set the default sort order
  $scope.searchProduct   = '';     // set the default search/filter term


  // Main callback
  function callbackGPA(result) {
    //console.log("result:" +result);
    var json = JSON.parse(result);
    $scope.products = json.products;
    $("#loader").hide("");
    //console.log("Login:" +$cookies.get("login"));
  }

  // Entry point
  //nuxeoClient.operation("GPA.QueryAndFetch").param("query", "Z001KYJF").execute(callbackGPA);
  nuxeoClient.operation("AKENEO.QueryAndFetch").param("query", "").execute().then(callbackGPA);
});
