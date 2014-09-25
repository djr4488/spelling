/**
 * Created by IMac on 9/24/2014.
 */
var findParentChildrenService = angular.module('findParentChildrenService', []);

findParentChildrenService.service('findParentChildrenService', function ($http, $rootScope) {
    this.findParentChildren = function () {
        var config = {headers: { 'trackingId': $rootScope.trackingId, 'auth-token': $rootScope.authToken }};
        var url = '/api/parent/findParentChildren/' + $rootScope.parentId;
        console.log('url: ' + url);
        return $http.get(url, config);
    };
});