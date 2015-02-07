/**
 * Created by IMac on 2/7/2015.
 */
var addWordController = angular.module("addWordController", []);

addWordController.controller('AddWordCtrl', ['$rootScope', '$scope', '$http', 'findParentChildrenService',
    function($rootScope, $scope, $http, findParentChildrenService) {
        $scope.baseUrl = '/api/parent/sp/'+$rootScope.parentId+'/addWord/';
        $scope.respInit = {
            findChildrenResponse: {
                forwardTo: "",
                trackingId: "",
                errorMsg: "",
                errorBold: "",
                authToken: "",
                parentChildren: {
                    parentChild: [{ }]
                }
            }
        }
        $scope.addWordRequest = {
            word: "",
            sentence: "",
            startOfWeek: "",
            endOfWeek: ""
        }
        $scope.addWordResponse = {
            errorMsg: "",
            errorBold: "",
            forwardTo: "",
            authToken: "",
            id: ""
        }
        $scope.errorResponse = {
            errorMsg: "",
            errorBold: "",
            forwardTo: "",
            authToken: "",
            id: ""
        }

        init();

        function init() {
            findParentChildrenService.findParentChildren().then(
                function(data) {
                    $scope.respInit.findChildrenResponse = data.data;
                    $scope.children = data.data.parentChildren.parentChild;
                    console.log("findChildrenResponse: ");
                    console.log(data);
                }
            );
        }

        $scope.addWordForChild = function(childId) {
            var config = {headers: { 'trackingId': $rootScope.trackingId, 'auth-token': $rootScope.authToken }};
            $scope.url = $scope.baseUrl + childId;
            $http.post($scope.url, $scope.addWordRequest, config).success(
                function (data, status) {
                    $scope.status = status;
                    $scope.addWordResponse = data;
                    if ($scope.addWordResponse.errorMsg != null &&
                        $scope.addWordResponse.errorMsg.length > 0) {
                        $scope.errorMsg = $scope.addWordResponse.errorMsg;
                        $scope.errorBold = $scope.addWordResponse.errorBold;
                    } else {
                        $rootScope.authToken = $scope.addWordResponse.authToken;
                        window.location.replace('#find-children')
                    }
                }
            ).error(
                function (data, status) {
                    console.log("Failed request");
                    console.log(data);
                    console.log(status);
                    $scope.errorResp.errorResponse = data || "Request failed.";
                    $scope.status = status;
                    $scope.errorMsg = $scope.errorResp.errorResponse.errorMsg;
                    $scope.errorBold = $scope.errorResp.errorResponse.errorBold;
                }
            )
        }
    }
]);