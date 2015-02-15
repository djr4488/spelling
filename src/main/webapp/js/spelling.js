/**
 * Created by IMac on 9/17/2014.
 */
var spellingApp = angular.module('spellingApp', [
    'ngRoute',
    'ui.bootstrap',
    'trackingIdParentService',
    'trackingIdController',
    'parentLoginController',
    'parentCreateController',
    'createChildController',
    'findParentChildrenService',
    'findParentChildrenController',
    'editChildController',
    'editParentController',
    'addWordController',
    'editWordController'
]);

spellingApp.config(['$routeProvider',
    function($routeProvider) {
        $routeProvider.
            when('/home', {
                templateUrl: 'home.html'
            }).
            when('/about', {
                templateUrl: 'about.html'
            }).
            when('/about-me', {
                templateUrl: 'aboutMe.html'
            }).
            when('/parent', {
                templateUrl: 'parent/home.html',
                controller: 'TrackingIdCtrl'
            }).
            when('/parent-login', {
                templateUrl: 'parent/login.html',
                controller: 'ParentLoginCtrl'
            }).
            when('/parent-create', {
                templateUrl: 'parent/create.html',
                controller: 'ParentCreateCtrl'
            }).
            when('/parent-landing', {
                templateUrl: 'parent/parentLanding.html'
            }).
            when('/create-child', {
                templateUrl: 'parent/createChild.html',
                controller: 'CreateChildCtrl'
            }).
            when('/find-children', {
                templateUrl: 'parent/findChildren.html',
                controller: 'EditChildCtrl'
            }).
            when('/edit-parent', {
                templateUrl: 'parent/editParentForm.html',
                controller: 'EditParentCtrl'
            }).
            when('/add-word', {
                templateUrl: 'parent/addWordForChild.html',
                controller: 'AddWordCtrl'
            }).
            when('/edit-word', {
                templateUrl: 'parent/editWord.html',
                controller: 'EditWordCtrl'
            }).
            otherwise({
                redirectTo: '/home'
            });
    }
]);
