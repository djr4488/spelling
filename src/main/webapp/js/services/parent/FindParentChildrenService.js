/**
 * Created by IMac on 9/24/2014.
 */
var findParentChildrenService = angular.module('findParentChildrenService', []);

findParentChildrenService.service('findParentChildrenService', function ($http, $rootScope) {
    this.findParentChildren = function () {
        console.log("findParentChildren() trackingId:" + $rootScope.trackingId + " auth-token:" + $rootScope.authToken);
        var config = {headers: { 'trackingId': $rootScope.trackingId, 'auth-token': $rootScope.authToken }};
        var url = '/api/parent/sp/'+$rootScope.parentId+'/children';
        return $http.get(url, config);
    };
});