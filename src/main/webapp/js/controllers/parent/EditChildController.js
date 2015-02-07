var editChildController = angular.module("editChildController", []);

editChildController.controller('EditChildCtrl', ['$rootScope', '$scope', '$http', 'findParentChildrenService',
    function($rootScope, $scope, $http, findParentChildrenService) {
        $scope.baseUrl = '/api/parent/sp/'+$rootScope.parentId+'/editChild/';
        $scope.req = {
            editChildRequest: {
                originalPassword: "",
                password: "",
                confirmPassword: "",
                stateAbbr:"",
                cityName:"",
                schoolName:"",
                grade:""
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
                    parentChild: [{ }]
                }
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
        $scope.editChild = function(childId, stateAbbr, cityName, schoolName, grade) {
            console.log("editChild() childId:" + childId);
            var config = {headers: { 'trackingId': $rootScope.trackingId, 'auth-token': $rootScope.authToken }};
            $scope.url = $scope.baseUrl + childId;
            $scope.req.editChildRequest.stateAbbr = stateAbbr;
            $scope.req.editChildRequest.cityName = cityName;
            $scope.req.editChildRequest.schoolName = schoolName;
            $scope.req.editChildRequest.grade = grade;
            $http.post($scope.url, $scope.req.editChildRequest, config).success(
                function (data, status) {
                    $scope.status = status;
                    $scope.resp.editChildResponse = data;
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
                    $scope.errorResp.errorResponse = data || "Request failed.";
                    $scope.status = status;
                    $scope.errorMsg = $scope.errorResp.errorResponse.errorMsg;
                    $scope.errorBold = $scope.errorResp.errorResponse.errorBold;
                }
            )
        }
    }

]);