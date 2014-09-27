/**
 * Created by IMac on 9/20/2014.
 */
var createChildController = angular.module("createChildController", []);

createChildController.controller('CreateChildCtrl', ['$rootScope', '$scope', '$http',
    function($rootScope, $scope, $http) {
        $scope.url = '/api/parent/'+$rootScope.parentId+'/createChildUser';
        $scope.req = {
            childUserCreateRequest: {
                username: "",
                password: "",
                confirmPassword: "",
                state: "",
                city: "",
                schoolName: "",
                gradeName: "",
                isPrivate: false,
                isHome: false
            }
        }
        $scope.resp = {
            childUserCreateResponse: {
                errorMsg: "",
                errorBold: "",
                forwardTo: "",
                authToken: "",
                id: ""
            }
        }
        $scope.createChild = function() {
            var config = {headers: { 'trackingId': $rootScope.trackingId, 'auth-token': $rootScope.authToken }};
            $http.post($scope.url, $scope.req, config).success(
                function (data, status) {
                    $scope.status = status;
                    $scope.resp = data;
                    if ($scope.resp.childUserCreateResponse.errorMsg != null &&
                        $scope.resp.childUserCreateResponse.errorMsg.length > 0) {
                        $scope.errorMsg = $scope.resp.childUserCreateResponse.errorMsg;
                        $scope.errorBold = $scope.resp.childUserCreateResponse.errorBold;
                    } else {
                        $rootScope.authToken = $scope.resp.childUserCreateResponse.authToken;
                        if ($scope.resp.childUserCreateResponse.forwardTo == 'createChildLanding') {
                            window.location.replace('#create-child');
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
        }
    }
]);