/**
 * Created by IMac on 9/17/2014.
 */
var trackingIdController = angular.module('trackingIdController', []);

trackingIdController.controller('TrackingIdCtrl', function ($rootScope, $scope, trackingIdService, $filter) {
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
        trackingIdService.getTrackingId.then(
            function(data) {
                $scope.resp.trackingIdResponse = data.data.trackingIdResponse;
                if ($scope.resp.trackingIdResponse.trackingId != null &&
                    $scope.resp.trackingIdResponse.trackingId.length == 0) {
                    $rootScope.trackingId = $scope.resp.trackingIdResponse.trackingId;
                }
            }
        );
    }
});