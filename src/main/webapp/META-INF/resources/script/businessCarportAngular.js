var businessCarportApp = angular.module('businessCarportApp', []);
businessCarportApp.controller('businessCarportCtrl', ['$scope', '$http', '$timeout','$interval',
function($scope, $http, $timeout,$interval) {
    $scope.carportDetails = [];
    $scope.selectedIndex = -1;
    $scope.selectValue=-1;
    $scope.parks=[];
  //  $scope.parks.push({name:"所有停车场",id:"-1"});
    $scope.getParks=function(){
        $http({
            url:'getParks?_t=' + (new Date()).getTime(),
            method:'get'
        }).success(function(response){
            if(response.status=1001){
                var body=response.body;
                for(var i=0;i<body.length;i++){
                    if(body[i].type==3){
                        $scope.parks.push(body[i]);
                    }
                }
                $scope.selectValue=$scope.parks[0];
                $scope.refreshData();
            }
        });
    };
    $scope.selectChange=function(){
        $scope.refreshData();
    };
    
    $scope.refreshData = function() {     
        $http.get('getBusinessCarportDetail?low=0&count=50&parkId='+$scope.selectValue.id).success(function(response) {
            if (response.status == 1001) {
                $scope.carportDetails = response.body;
            }
        });
        
    };
     $scope.getParks();
      $interval(function(){
          $scope.refreshData();
      },8000);
    
   
    $scope.checked = function(index){
        $scope.selectedIndex = index;
        if($scope.parks[index].checked==false)
        $scope.parks[index].checked=true;
        else{
            $scope.parks[index].checked=false;
        }
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

