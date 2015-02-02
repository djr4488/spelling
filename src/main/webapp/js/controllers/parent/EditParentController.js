/**
 * Created by IMac on 1/31/2015.
 */
var editParentController = angular.module("editParentController", []);

editChildController.controller('EditChildCtrl', ['$rootScope', '$scope', '$http',
    function($rootScope, $scope, $http) {
        $scope.url = '/api/parent/sp/'+$rootScope.parentId+'/edit';
        $scope.req = {
            editParentRequest: {
                originalPassword: "",
                password: "",
                confirmPassword: ""
            }
        }
        $scope.resp = {
            editParentResponse: {
                errorMsg: "",
                errorBold: "",
                forwardTo: "",
                authToken: "",
                id: ""
            }
        }
        $scope.errorResp = {
            errorResponse: {
                errorMsg: "",
                errorBold: "",
                forwardTo: "",
                authToken: "",
                id: ""
            }
        }

        $scope.editParent = function() {
            console.log("editParent() id:" + $rootScope.parentId);
            var config = {headers: { 'trackingId': $rootScope.trackingId, 'auth-token': $rootScope.authToken }};
            $http.post($scope.url, $scope.req.editParentRequest, config).success(
                function(data, status) {
                    $scope.status = status;
                    $scope.resp.editParentResponse = data;
                    console.log("editParent() status:" + status + " resp:" + $scope.resp);
                    if ($scope.resp.editParentResponse.errorMsg != null &&
                        $scope.resp.editParentResponse.errorMsg.length > 0) {
                        $scope.errorMsg = $scope.resp.editParentResponse.errorMsg;
                        $scope.errorBold = $scope.resp.editParentResponse.errorBold;
                    } else {
                        $rootScope.authToken = $scope.resp.editParentResponse.authToken;
                        $scope.successMsg = 'Password change accepted';
                        $scope.successBold = 'Alright!';
                        window.location.replace('#edit-parent');
                    }
                }
            ).error(
                function(data, status) {
                    console.log("Failed request");
                    console.log(data);
                    console.log(status);
                    $scope.data.errorResp = data || "Request failed.";
                    $scope.status = status;
                    $scope.errorMsg = $scope.errorResp.errorResponse.errorMsg;
                    $scope.errorBold = $scope.errorResp.errorResponse.errorBold;
                }
            )
        }
    }
]);
