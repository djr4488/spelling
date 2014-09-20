/**
 * Created by IMac on 9/20/2014.
 */
var parentCreateController = angular.module("parentCreateController", []);

parentCreateController.controller('ParentCreateCtrl', ['$rootScope', '$scope', '$http',
    function($rootScope, $scope, $http) {
        $scope.url = '/api/parent/createParentUser';
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
        $scope.createParent = function() {
            var config = {headers: { 'trackingId': $rootScope.trackingId }};
            $http.put(url, req, config).success(

            ).error(

            )
        }
    }
]);