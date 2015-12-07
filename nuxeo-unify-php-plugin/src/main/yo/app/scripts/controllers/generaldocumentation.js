'use strict';

/**
 * @ngdoc function
 * @name frontjcdApp.controller:MainCtrl
 * @description
 * # MainCtrl
 * Controller of the frontApp
 */
angular.module('frontApp')
  .controller('GeneralDocumentationCtrl', function ($scope, $cookies, nuxeoClient) {
 //$scope, $routeParams, $sce, $location, nuxeoClient

  $scope.documents = {};

  $scope.sortType     = 'title'; // set the default sort type
  $scope.sortReverse  = false;  // set the default sort order
  $scope.searchDocument   = '';     // set the default search/filter term

  $scope.formatDate = function(date){
        var dateOut = new Date(date);
        return dateOut;
  };

  // Main callback
  function callback(result) {
    $scope.documents = result.entries;
    $("#loader").hide("");
  }

  // Entry point
  nuxeoClient.request('query/QueryForGeneralDocumentations').schema('file').get().then(callback);
});
