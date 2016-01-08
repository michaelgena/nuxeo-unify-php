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

  $scope.categories = {};
  $scope.products = [];
  $scope.productVersions = [];

  $scope.belongsToCategory = function(categories, code){
    var result = false;
    $.each(categories, function(i, item) {
      if(item.code == code){
        result = true;
      }
    });
    return result;
  };

  $scope.belongsToProduct = function(products, sku){
    var result = false;
    $.each(products, function(i, item) {
      if(item.attributes.sku == sku){
        result = true;
      }
    });
    return result;
  };

  // Main callback
  function callback(result) {
    $scope.products = result.entries;
    $("#loader").hide("");
  }

  // Main callback
  function callbackAkeneo(result) {
    var json = JSON.parse("[{\"code\":\"master_technical__communication_systems__openscapebusiness\",\"label\":\"OpenScape Business\"}, {\"code\":\"ic_catalog__devices__desk_phones\",\"label\":\"Desk Phones\"}, {\"code\":\"ic_catalog__devices__gigaset\",\"label\":\"Gigaset\"}, {\"code\":\"gl_catalog__devices__hipath_cordless\",\"label\":\"HiPath Cordless\"}]");
    $scope.categories = json;
    $.each(json, function(i, item) {
      nuxeoClient.operation("AKENEO.QueryAndFetch").param("pid", "").param("type", "").param("object_type", "product").param("code", "").param("category_code", item.code).param("version_to_product", "").execute().then(callbackAkeneoProduct);
    });
    $("#loader").hide("");
  }

  function callbackAkeneoProduct(result) {
    var json = JSON.parse(result);
    if(json.products.length>0){
      $scope.products.push(json.products[0]);
      $.each(json, function(i, item) {
        nuxeoClient.operation("AKENEO.QueryAndFetch").param("pid", item.attributes.sku).param("type", "").param("object_type", "product").param("code", "").param("category_code", "").param("version_to_product", "").execute().then(callbackAkeneoProductVersion);
      });
    }
  }

  function callbackAkeneoProductVersion(result) {
    var json = JSON.parse(result);
    if(json.productVersions.length>0){
      $scope.productVersions.push(json.products[0]);
    }
  }
  // Entry point
  //nuxeoClient.request('query/QueryForSearchProductsByFamily?queryParams='+encodeURIComponent('%'+$routeParams.productfamily+'%')).get().then(callback);
  nuxeoClient.operation("AKENEO.QueryAndFetch").param("pid", "").param("type", "categories").param("object_type", "").param("code", $routeParams.code).param("category_code", "").param("version_to_product", "").execute().then(callbackAkeneo);
});
