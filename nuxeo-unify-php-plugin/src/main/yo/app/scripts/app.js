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
      .when('/product/:pid', {
          templateUrl: 'views/product.html',
          controller: 'ProductCtrl',
       	  controllerAs: 'product'
      })
      .when('/generaldocumentation', {
          templateUrl: 'views/generaldocumentation.html',
          controller: 'GeneralDocumentationCtrl',
       	  controllerAs: 'generaldocumentation'
      })
      .when('/salescirculars', {
          templateUrl: 'views/salescirculars.html',
          controller: 'SalesCircularsCtrl',
       	  controllerAs: 'salescirculars'
      })
      .when('/searchproducts', {
          templateUrl: 'views/searchproducts.html',
          controller: 'SearchProductsCtrl',
       	  controllerAs: 'searchproducts'
      })
      .when('/currentportfolio/:productfamily', {
          templateUrl: 'views/currentportfolio.html',
          controller: 'CurrentPortfolioCtrl',
       	  controllerAs: 'currentportfolio'
      })
      .when('/subscriptions', {
          templateUrl: 'views/subscriptions.html',
          controller: 'SubscriptionsCtrl',
       	  controllerAs: 'subscriptions'
      })
      .otherwise({
        redirectTo: '/'
      });
  }).filter('groupBy', ['$parse', function ($parse) {
    return function (list, group_by) {
        var filtered = [];
        var prev_item = null;
        var group_changed = false;
        // this is a new field which is added to each item where we append "_CHANGED"
        // to indicate a field change in the list
        //was var new_field = group_by + '_CHANGED'; - JB 12/17/2013
        var new_field = 'group_by_CHANGED';

        // loop through each item in the list
        angular.forEach(list, function (item) {

            group_changed = false;

            // if not the first item
            if (prev_item !== null) {

                // check if any of the group by field changed

                //force group_by into Array
                group_by = angular.isArray(group_by) ? group_by : [group_by];

                //check each group by parameter
                for (var i = 0, len = group_by.length; i < len; i++) {
                    if ($parse(group_by[i])(prev_item) !== $parse(group_by[i])(item)) {
                        group_changed = true;
                    }
                }


            }// otherwise we have the first item in the list which is new
            else {
                group_changed = true;
            }

            // if the group changed, then add a new field to the item
            // to indicate this
            if (group_changed) {
                item[new_field] = true;
            } else {
                item[new_field] = false;
            }

            filtered.push(item);
            prev_item = item;

        });

        return filtered;
    };
}]);
