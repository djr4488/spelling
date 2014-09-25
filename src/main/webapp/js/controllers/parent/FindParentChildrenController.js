/**
 * Created by IMac on 9/24/2014.
 */
var findParentChildrenController = angular.module('findParentChildrenController', []);

findParentChildrenController.controller('FindParentChildrenCtrl', function ($rootScope, $scope, findParentChildrenService, $filter) {
    $scope.resp = {
        findParentChildrenResponse: {
            forwardTo: "",
            trackingId: "",
            errorMsg: "",
            errorBold: "",
            authToken: "",
            parentChildren: {
                child: [{username: "", id: ""}]
            }
        }
    }
    init();
    function init() {
        findParentChildrenService.findParentChildren().then(
            function(data) {
                $scope.resp.findParentChildrenResponse = data.data.findParentChildrenResponse;
                console.log("findParentChildrenResponse: ");
                console.log(data);
            }
        );
    }
});