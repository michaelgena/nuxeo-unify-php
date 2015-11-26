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

  $scope.result = {};
  // Main callback
  function callbackGPA(err, result) {
     console.log("data: "+result);
     //$("#loader").hide("");
  }

  // Entry point
  nuxeoClient.operation("GPA.QueryAndFetch").param("query", "Z001KYJF").execute(callbackGPA);

});
