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
  $scope.selectedAll = false;
  $scope.notificationScopes = {};
  $scope.notificationDefaultScope = '';

  $scope.checkAll = function () {
       $("#subscription").html("Subscribe for update notification").removeClass("positive");
       if ($scope.selectedAll) {
          $scope.selectedAll = true;
          $("#subscription").removeClass("disabled");
       } else {
          $scope.selectedAll = false;
          $("#subscription").addClass("disabled");
       }
       angular.forEach($scope.products, function (product) {
           product.selected = $scope.selectedAll;
       });
   };

   $scope.check = function () {
        $("#subscription").html("Subscribe for update notification").removeClass;
        var hasAtLeastOneChecked = false;
        angular.forEach($scope.products, function (product) {
            if (product.selected) {
              hasAtLeastOneChecked = true;
            }
        });
        if (hasAtLeastOneChecked) {
           $("#subscription").removeClass("disabled");
        } else {
           $("#subscription").addClass("disabled");
        }
    };

    $scope.selectScope = function () {
        $('.ui.modal').modal('show');
    };

    $scope.subscribe = function () {
        angular.forEach($scope.products, function (product) {
            if (product.selected) {

              //Call the REST API here
              nuxeoClient.operation("javascript.subscribe").param("sku", product.attributes.sku).param("title", product.attributes.product_version_name).param("user", $cookies.get('login')).param("scope", $scope.notificationScope).execute();
            }
        });
        $("#subscription").html("Subscribed").addClass("positive");
    };


  // Main callback
  function callbackGPA(result) {
    //console.log("result:" +result);
    var json = JSON.parse(result);
    $scope.products = json.products;
    $("#loader").hide("");
    //console.log("Login:" +$cookies.get("login"));
  }

  function callbackNotifScope(result) {
    $scope.notificationScopes = result.entries;
  }

  // Entry point
  //nuxeoClient.operation("GPA.QueryAndFetch").param("query", "Z001KYJF").execute(callbackGPA);
  nuxeoClient.operation("AKENEO.QueryAndFetch").param("pid", "").param("type", "").param("object_type", "").param("code", "").param("category_code", "").param("version_to_product", "").execute().then(callbackGPA);

  nuxeoClient.request('directory/Notification_Scope').get().then(callbackNotifScope);

});
