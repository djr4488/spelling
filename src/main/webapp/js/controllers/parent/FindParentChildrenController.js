/**
 * Created by IMac on 9/24/2014.
 */
var findParentChildrenController = angular.module('findParentChildrenController', []);

findParentChildrenController.controller('FindParentChildrenCtrl', function ($rootScope, $scope, findParentChildrenService, $filter) {
    $scope.resp = {
        findChildrenResponse: {
            forwardTo: "",
            trackingId: "",
            errorMsg: "",
            errorBold: "",
            authToken: "",
            parentChildren: {
                child: [{ }]
            }
        }
    }
    init();
    function init() {
        findParentChildrenService.findParentChildren().then(
            function(data) {
                $scope.resp.findChildrenResponse = data.data.findChildrenResponse;
                $scope.children = data.data.findChildrenResponse.parentChildren.child;
                console.log("findChildrenResponse: ");
                console.log(data);
            }
        );
    }
});