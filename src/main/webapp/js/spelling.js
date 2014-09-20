/**
 * Created by IMac on 9/17/2014.
 */
var spellingApp = angular.module('spellingApp', [
    'ngRoute',
    'trackingIdController'
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
                templateUrl: 'parentHome.html',
                controller: 'TrackingIdCtrl'
            }).otherwise({
                redirectTo: '/home'
            });
    }
]);

spellingApp.service('trackingIdParentService', function ($http) {
    this.getTrackingId = function () {
        return $http.get('/api/parent/getTrackingId');
    };
});