/**
 * Created by IMac on 9/20/2014.
 */
var parentLoginController = angular.module("parentLoginController", []);

parentLoginController.controller('ParentLoginCtrl', ['$rootScope', '$scope', '$http',
    function($rootScope, $scope, $http) {
        $scope.method = 'POST';
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
        console.log("trackingId: " + $rootScope.trackingId);
        $scope.login = function() {
            var config = {headers: { 'trackingId': $rootScope.trackingId }};
            $http.post($scope.url, $scope.req, config).success(
                function (data, status) {
                    console.log("posted - success");
                    console.log(data);
                    $scope.status = status;
                    $scope.resp = data;
                    if ($scope.resp.parentLoginResponse.errorMsg != null &&
                        $scope.resp.parentLoginResponse.errorMsg.length > 0) {
                        console.log("In error page");
                        $scope.errorMsg = $scope.resp.parentLoginResponse.errorMsg;
                        $scope.errorBold = $scope.resp.parentLoginResponse.errorBold;
                    } else {
                        console.log("In redirect page");
                        console.log($scope.resp.parentLoginResponse.forwardTo);
                        $rootScope.authToken = $scope.resp.parentLoginResponse.authToken;
                        $rootScope.parentId = $scope.resp.parentLoginResponse.id;
                        if ($scope.resp.parentLoginResponse.forwardTo == 'parentLanding') {
                            window.location.replace('#parentLanding');
                        }
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
        };
    }
]);