/**
 * Created by IMac on 9/20/2014.
 */
var parentLoginController = angular.module("parentLoginController", []);

parentLoginController.controller('ParentLoginCtrl', ['$rootScope', '$scope', '$http',
    function($rootScope, $scope, $http) {
        $scope.url = '/api/parent/login';
        $scope.req = {
            parentLoginRequest: {
                emailAddress: "",
                password: ""
            }
        }
        $scope.resp = {
            parentLoginResponse: {
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
        $scope.login = function() {
            var config = {headers: { 'trackingId': $rootScope.trackingId }};
            console.log("login() config:"+config+" req:"+$scope.req.parentLoginRequest);
            $http.post($scope.url, $scope.req.parentLoginRequest, config).success(
                function (data, status) {
                    console.log("posted - success");
                    console.log(data);
                    $scope.status = status;
                    $scope.resp.parentLoginResponse = data;
                    console.log("In redirect page");
                    console.log($scope.resp.parentLoginResponse.forwardTo);
                    $rootScope.authToken = $scope.resp.parentLoginResponse.authToken;
                    $rootScope.parentId = $scope.resp.parentLoginResponse.id;
                    if ($scope.resp.parentLoginResponse.forwardTo == 'parentLanding') {
                        window.location.replace('#parent-landing');
                    }
                }
            ).error(
                function (data, status) {
                    console.log("Failed request");
                    console.log(data);
                    console.log(status);
                    $scope.errorResp.errorResponse = data || "Request failed.";
                    $scope.status = status;
                    $scope.resp.errorResp = data;
                    $scope.errorMsg = $scope.errorResp.errorResponse.errorMsg;
                    $scope.errorBold = $scope.errorResp.errorResponse.errorBold;
                }
            )
        };
    }
]);