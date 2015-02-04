/**
 * Created by IMac on 9/20/2014.
 */
var parentCreateController = angular.module("parentCreateController", []);

parentCreateController.controller('ParentCreateCtrl', ['$rootScope', '$scope', '$http',
    function($rootScope, $scope, $http) {
        $scope.url = '/api/parent/createParent';
        $scope.req = {
            parentCreateRequest: {
                username: "",
                password: "",
                confirmPassword: "",
                emailAddress: ""
            }
        }
        $scope.resp = {
            parentCreateResponse: {
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
        $scope.createParent = function() {
            var config = {headers: { 'trackingId': $rootScope.trackingId }};
            $http.post($scope.url, $scope.req.parentCreateRequest, config).success(
                function (data, status) {
                    $scope.status = status;
                    $scope.resp.parentCreateResponse = data;
                    if ($scope.resp.parentCreateResponse.errorMsg != null &&
                        $scope.resp.parentCreateResponse.errorMsg.length > 0) {
                        $scope.errorMsg = $scope.resp.parentCreateResponse.errorMsg;
                        $scope.errorBold = $scope.resp.parentCreateResponse.errorBold;
                    } else {
                        if ($scope.resp.parentCreateResponse.forwardTo == 'loginLanding') {
                            window.location.replace('#parent-login');
                        }
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