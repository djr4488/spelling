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
            when('/parentHome', {
                templateUrl: 'parentHome.html',
                controller: 'TrackingIdCtrl'
            });
    }
]);

spellingApp.service('getTrackingId', function ($http) {
    this.getGameOptions = function () {
        return $http.get('/api/parent/getTrackingId');
    };
});