'use strict';

/**
 * @ngdoc function
 * @name frontjcdApp.controller:MainCtrl
 * @description
 * # MainCtrl
 * Controller of the frontApp
 */
angular.module('frontApp')
  .controller('SearchProductsCtrl', function ($scope, $cookies, nuxeoClient, $routeParams) {
 //$scope, $routeParams, $sce, $location, nuxeoClient

  $scope.products = {};
  $scope.documents = {};

  $scope.sortType     = 'title'; // set the default sort type
  $scope.sortReverse  = false;  // set the default sort order
  $scope.searchProduct   = '';     // set the default search/filter term
  $scope.searchDocument   = '';     // set the default search/filter term

  $scope.formatDate = function(date){
        var dateOut = new Date(date);
        return dateOut;
  };

  // Main callback
  function callback(result) {
    $scope.products = result.entries;
    $("#loader").hide("");
  }

  // Main callback
  function callbackDocuments(result) {
    $scope.documents = result.entries;
    $("#loader").hide("");
  }

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



  // Entry point
  nuxeoClient.request('query/QueryForSearchProducts').get().then(callback);
  nuxeoClient.request('query/QueryForSearchDocuments').get().then(callbackDocuments);
});
