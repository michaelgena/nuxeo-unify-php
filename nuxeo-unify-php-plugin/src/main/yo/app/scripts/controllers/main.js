'use strict';

/**
 * @ngdoc function
 * @name frontjcdApp.controller:MainCtrl
 * @description
 * # MainCtrl
 * Controller of the frontApp
 */
angular.module('frontApp')
  .controller('MainCtrl', function ($scope, nuxeoClient) {
 //$scope, $routeParams, $sce, $location, nuxeoClient

  $scope.products = {};

  $scope.sortType     = 'attributes.product_name'; // set the default sort type
  $scope.sortReverse  = false;  // set the default sort order
  $scope.searchProduct   = '';     // set the default search/filter term


  // Main callback
  function callbackGPA(err, result) {
    var json = JSON.parse(result);
    $scope.products = json.products;
    $("#loader").hide("");
  }

  // Entry point
  //nuxeoClient.operation("GPA.QueryAndFetch").param("query", "Z001KYJF").execute(callbackGPA);
  nuxeoClient.operation("AKENEO.QueryAndFetch").execute(callbackGPA);
});
