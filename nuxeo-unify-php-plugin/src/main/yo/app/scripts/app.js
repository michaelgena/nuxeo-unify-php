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
    'nuxeo'
  ])
  .service('nuxeoClient', function(nuxeoClientFactory) {
	  var client = nuxeoClientFactory({
        // Comment lines below to use prod instance
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
      .when('/list/:query', {
          templateUrl: 'views/list.html',
          controller: 'ListCtrl',
       	  controllerAs: 'list'
      })
      .otherwise({
        redirectTo: '/'
      });
  });
