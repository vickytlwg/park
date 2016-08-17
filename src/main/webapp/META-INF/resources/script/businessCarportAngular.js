var businessCarportApp = angular.module('businessCarportApp', []);
businessCarportApp.controller('businessCarportCtrl', ['$scope', '$http', '$timeout',
function($scope, $http, $timeout) {
    $scope.carportDetails = [];
    $scope.selectedIndex = -1;
    $scope.parks=[];
    $scope.getParks=function(){
        $http({
            url:'getParks?_t=' + (new Date()).getTime(),
            method:'get'
        }).success(function(response){
            if(response.status=1001){
               $scope.parks=response.body;
            }
        });
    };
    $scope.refreshData = function() {     
        $http.get('getBusinessCarportDetail?low=0&count=50&parkId=109').success(function(response) {
            if (response.status == 1001) {
                $scope.carportDetails = response.body;
            }
        });
        
    };
    $scope.refreshData();
    
    $scope.checked = function(index){
        $scope.selectedIndex = index;
    };
    $scope.addData = function() {
        console.log($scope.checked);
           var getdata=$http({
            method:'get',
            url:'http://120.25.159.154/park/getBusinessCarportDetail',
            headers:{"token":"6f13b8f3-cc3f-4e2a-a5b4-01b9cf6b40ca-1458491724564"},
            params:{low:0,count:50,parkId:109}
          }).success(function(data){
            console.log(data);
        });
    };
}]);

