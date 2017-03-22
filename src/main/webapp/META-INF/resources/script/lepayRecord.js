angular.module("lepayRecordApp",['ui.bootstrap']).
controller("lepayRecordCtrl",["$scope","$uibModal","lepayRecordHttpService",function($scope,$uibModal,lepayRecordHttpService){
    lepayRecordHttpService.getByCount({start:0,count:1000}).then(function(response){
        $scope.items=response;
    });
    $scope.showPosChargeData=function(data){
        console.log(data);
        lepayRecordHttpService.getPosChargeDataById({id:data}).then(function(response){
           $scope.posdata=response;
           console.log(response);
           $uibModal.open({
               templateUrl:"showPosChargeModal",
               scope:$scope,
               controller:"showPosChargeDataCtrl"
           }); 
        });
    };
}]).
controller("showPosChargeDataCtrl",function($scope,$uibModalInstance){
  
    $scope.close=function(){
        $uibModalInstance.close();
    };
}
).
service("lepayRecordHttpService",["$http","$q",function($http,$q){
    return {
        getAmount:function(){
           var deferred=$q.defer();
           var promise=deferred.promise;           
             $http({
                 url:"/park/lepay/record/getAmount",
                 method:'get'
             }).success(function(response){
                 if(response.status==1001){
                    deferred.resolve(response.body); 
                 }
             });
             return promise;
        },
        getByCount:function(data){
            var deferred=$q.defer();
           var promise=deferred.promise;           
             $http({
                 url:"/park/lepay/record/getByCount",
                 method:'post',
                 data:angular.toJson(data)
             }).success(function(response){
                 if(response.status==1001){
                    deferred.resolve(response.body); 
                 }
             });
             return promise;
        },
        getPosChargeDataById:function(data){
           var deferred=$q.defer();
           var promise=deferred.promise;  
           $http({
               url:"/park/pos/charge/getById",
               method:"post",
               data:angular.toJson(data)
           }).success(function(response){
                if(response.status==1001){
                    deferred.resolve(response.body); 
                 }
           }); 
             return promise;
        }
        
    };
}]);
