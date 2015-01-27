var editChildController = angular.module("editChildController", []);

editChildController.controller('EditChildCtrl', ['$rootScope', '$scope', '$http', 'findParentChildrenService',
    function($rootScope, $scope, $http, findParentChildrenService) {
        $scope.url = '/api/parent/'+$rootScope.parentId+'/editChild/';
        $scope.req = {
            editChildRequest: {
                originalPassword: "",
                password: "",
                confirmPassword: "",
                childId: ""
            }
        }
        $scope.resp = {
            editChildResponse: {
                errorMsg: "",
                errorBold: "",
                forwardTo: "",
                authToken: "",
                id: ""
            }
        }
        $scope.respInit = {
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
                    $scope.respInit.findChildrenResponse = data.data.findChildrenResponse;
                    $scope.children = data.data.findChildrenResponse.parentChildren.child;
                    console.log("findChildrenResponse: ");
                    console.log(data);
                }
            );
        }
        $scope.editChild = function(childId) {
            console.log("editChild() childId:" + childId);
            var config = {headers: { 'trackingId': $rootScope.trackingId, 'auth-token': $rootScope.authToken }};
            $scope.url = $scope.url + childId;
            $http.post($scope.url, $scope.req, config).success(
                function (data, status) {
                    $scope.status = status;
                    $scope.resp = data;
                    if ($scope.resp.editChildResponse.errorMsg != null &&
                        $scope.resp.editChildResponse.errorMsg.length > 0) {
                        $scope.errorMsg = $scope.resp.editChildResponse.errorMsg;
                        $scope.errorBold = $scope.resp.editChildResponse.errorBold;
                    } else {
                        $rootScope.authToken = $scope.resp.editChildResponse.authToken;
                        window.location.replace('#find-children')
                    }
                }
            ).error(
                function (data, status) {
                    console.log("Failed request");
                    console.log(data);
                    console.log(status);
                    $scope.data = data || "Request failed.";
                    $scope.status = status;
                }
            )
        }
    }

]);