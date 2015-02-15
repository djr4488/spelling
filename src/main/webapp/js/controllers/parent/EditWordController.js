/**
 * Created by IMac on 2/8/2015.
 */
var editWordController = angular.module("editWordController", []);

editWordController.controller('EditWordCtrl', ['$rootScope', '$scope', '$http',
    function($rootScope, $scope, $http) {
        $scope.url = '/api/parent/sp/'+$rootScope.parentId+'/editWord';
        $scope.req = {
            editWordRequest: {
                word: "",
                editedWord: "",
                sentence: ""
            }
        }
        $scope.resp = {
            editWordResponse: {
                errorMsg: "",
                errorBold: "",
                successMsg: "",
                successBold: "",
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

        $scope.editWord = function() {
            console.log("editWord() id:" + $rootScope.parentId);
            var config = {headers: { 'trackingId': $rootScope.trackingId, 'auth-token': $rootScope.authToken }};
            $http.post($scope.url, $scope.req.editParentRequest, config).success(
                function(data, status) {
                    $scope.status = status;
                    $scope.resp.editParentResponse = data;
                    console.log("editWord() status:" + status + " resp:" + $scope.resp);
                    if ($scope.resp.editWordResponse.errorMsg != null &&
                        $scope.resp.editWordResponse.errorMsg.length > 0) {
                        $scope.errorMsg = $scope.resp.editWordResponse.errorMsg;
                        $scope.errorBold = $scope.resp.editWordResponse.errorBold;
                    } else {
                        $rootScope.authToken = $scope.resp.editWordResponse.authToken;
                        $scope.successMsg = $scope.resp.editWordResponse.successMsg;
                        $scope.successBold = $scope.resp.editWordResponse.successBold;
                        window.location.replace('#edit-word');
                    }
                }
            ).error(
                function(data, status) {
                    console.log("editWord() Failed request");
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