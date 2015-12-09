'use strict';

/**
 * @ngdoc function
 * @name frontjcdApp.controller:MainCtrl
 * @description
 * # MainCtrl
 * Controller of the frontApp
 */
angular.module('frontApp')
  .controller('CurrentPortfolioCtrl', function ($scope, $cookies, nuxeoClient, $routeParams) {
 //$scope, $routeParams, $sce, $location, nuxeoClient

  $scope.products = {};

  // Main callback
  function callback(result) {
    $scope.products = result.entries;
    $("#loader").hide("");
  }

  // Entry point
  nuxeoClient.request('query/QueryForSearchProductsByFamily?queryParams='+encodeURIComponent('%'+$routeParams.productfamily+'%')).get().then(callback);
});
