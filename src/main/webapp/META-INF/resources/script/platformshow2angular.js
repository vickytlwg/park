angular.module("parkBigDataApp", []).
controller("parkBigDataCtrl", ['$scope','$interval','httpService',
function($scope,$interval,httpService) {
var data={low:0,count:10};
$scope.items={};
var lastestId=0;
httpService.getPosData(data).then(function(response){  
    lastestId=response[0].id;
     angular.forEach(response,function(item){
        item.isNew=false;
    });
     $scope.items=response;
}); 
$interval(function(){
    httpService.getPosData(data).then(function(response){  
    angular.forEach(response,function(item){
        if(item.id>lastestId){
             item.isNew=true;
        }
        else{
             item.isNew=false;
        }
    });
    $scope.items=response;
});
},2000);
}])
.service("httpService",function($http,$q){
    return {
        getPosData:function(data){
            var deferred=$q.defer();
            var promise=deferred.promise; 
            $http({
                url:"/park/pos/charge/getByCount",
                method:"post",
                data:angular.toJson(data)
            }).success(function(response){
                if (response.status==1001) {
                    deferred.resolve(response.body);
                } else {
                  deferred.reject(response);
                }
            }).error(function(){
              deferred.reject();
            });
            return promise;
        }
    };
}).controller("getdataCtrl",["$scope",'$interval',"httpService2",function($scope,$interval,httpService){
      $scope.processData=function(data){
      $scope.dayAmountMoney=0;
      $scope.dayRealMoney=0;
      $scope.dayOutCount=0;
      $scope.dayParkCount=0;
      $scope.dayCarportCount=0;
      $scope.dayOnlineCarportCount=0;
      $scope.dayCarportLeftCount=0;
      angular.forEach(data,function(value){
          $scope.dayAmountMoney+=value.amountmoney;
          $scope.dayRealMoney+=value.realmoney;
          $scope.dayParkCount+=value.parkcount;
          $scope.dayCarportCount+=value.carportcount;
          $scope.dayOnlineCarportCount+=value.onlinecarportcount;
          $scope.dayCarportLeftCount+=value.carportleftcount;
          $scope.dayOutCount+=value.outcount;
      });
  };
  
  $scope.items={};

httpService.getPosData({low:0,count:10}).then(function(response){  
     $scope.items=response;
}); 
$interval(function(){
    httpService.getZoneCenterInfo().then(function(result){
      $scope.processData(result);
  });
},6000);
    
  
}]).
service("httpService2",['$http','$q',function($http,$q){
      return {
        getZoneCenterInfo:function() {
        var deferred = $q.defer();
        var promise = deferred.promise;
        $http({
            headers:{'token':'f9e08257652deaa76de81c8225801482'},
            url : "/park/outsideParkInfo/zoneCenterInfo",
            method : 'post',
            params : {
                start : 0,
                count : 100
            }
        }).success(function(response) {
            if(response.status==1001){
                deferred.resolve(response.body);
            }            
        });
        return promise;
    },
    getPosData:function(data){
            var deferred=$q.defer();
            var promise=deferred.promise; 
            $http({
                headers:{'token':'f9e08257652deaa76de81c8225801482'},
                url:"/park/pos/charge/getByCount",
                method:"post",
                data:angular.toJson(data)
            }).success(function(response){
                if (response.status==1001) {
                    deferred.resolve(response.body);
                } else {
                  deferred.reject(response);
                }
            }).error(function(){
              deferred.reject();
            });
            return promise;
        }
    };
}]);
