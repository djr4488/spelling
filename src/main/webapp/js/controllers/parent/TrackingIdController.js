/**
 * Created by IMac on 9/17/2014.
 */
var trackingIdController = angular.module('trackingIdController', []);

trackingIdController.controller('TrackingIdCtrl', function ($rootScope, $scope, trackingIdParentService, $filter) {
    $scope.resp = {
        trackingIdResponse: {
            forwardTo: "",
            trackingId: "",
            errorMsg: "",
            errorBold: ""
        }
    }
    init();
    function init() {
        trackingIdParentService.getTrackingId().then(
            function(data) {
                console.log("getTrackingId() " + data);
                $scope.resp.trackingIdResponse = data.data;
                if ($scope.resp.trackingIdResponse.trackingId != null &&
                    $scope.resp.trackingIdResponse.trackingId.length != 0) {
                    $rootScope.trackingId = $scope.resp.trackingIdResponse.trackingId;
                }
            }
        );
    }
});