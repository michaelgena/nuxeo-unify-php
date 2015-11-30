'use strict';

/**
 * @ngdoc overview
 * @name frontApp
 * @description
 * # frontApp
 *
 * Main module of the application.
 */
angular
  .module('frontApp', [
    'ngCookies',
    'ngResource',
    'ngRoute',
    'ngSanitize',
    'nuxeo',
    'angular-toArrayFilter'
  ])
  .service('nuxeoClient', function(nuxeoClientFactory) {
	  var client = nuxeoClientFactory({
        // Comment lines below to use prod instance
        //localhost token 3b70ecfe-e9cb-4cc8-8f3b-ed4ecc16628c
        //mchp207a token 9c4d6a63-6c66-496a-9c8a-43cf2172db53
        baseURL: 'http://localhost:8080/nuxeo',
        'auth': {
          'method':'basic',
          'username':'Administrator',
          'password':'Administrator'
        }
  		});
      // fetch all schemas
      client.schema('*');
      return client;
  })
  .config( [
    '$compileProvider',
    function( $compileProvider )
    {
        $compileProvider.aHrefSanitizationWhitelist(/^\s*(https?|ftp|mailto|javascript):/);
        // Angular before v1.2 uses $compileProvider.urlSanitizationWhitelist(...)
    }
])
  .config(function ($routeProvider) {
    $routeProvider
      .when('/', {
        templateUrl: 'views/main.html',
        controller: 'MainCtrl',
        controllerAs: 'main'
      })
      .when('/product/:productId', {
          templateUrl: 'views/product.html',
          controller: 'ProductCtrl',
       	  controllerAs: 'product'
      })
      .when('/list/:query', {
          templateUrl: 'views/list.html',
          controller: 'ListCtrl',
       	  controllerAs: 'list'
      })
      .otherwise({
        redirectTo: '/'
      });
  });
