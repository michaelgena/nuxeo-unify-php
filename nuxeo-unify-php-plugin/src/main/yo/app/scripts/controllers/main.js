'use strict';

/**
 * @ngdoc function
 * @name frontjcdApp.controller:MainCtrl
 * @description
 * # MainCtrl
 * Controller of the frontApp
 */
angular.module('frontApp')
  .controller('MainCtrl', function () {
 //$scope, $routeParams, $sce, $location, nuxeoClient
  $("#loader").hide("");
    // Main callbacks
   /*function callbackAssets(assets) {
     $scope.assets = assets.entries;
     $("#loader").hide("");
   }

   function errorAssets(err) {
     console.log('Error while fetching assets: ' + err);
     $("#loader").hide("");
   }*/
    // Entry point
  	//nuxeoClient.request('query/AssetsForFrontEnd').schema('file').header("X-NXContext-Category","thumbnail").get().then(callbackAssets, errorAssets);

  });
