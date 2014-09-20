/**
 * Created by IMac on 9/20/2014.
 */
var trackingIdParentService = angular.module('trackingIdParentService', []);

trackingIdParentService.service('trackingIdParentService', function ($http) {
    this.getTrackingId = function () {
        return $http.get('/api/parent/getTrackingId');
    };
});