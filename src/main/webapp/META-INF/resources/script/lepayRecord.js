angular.module("lepayRecordApp",['ui.bootstrap']).
controller("lepayRecordCtrl",["$scope","lepayRecordHttpService",function($scope,lepayRecordHttpService){
    lepayRecordHttpService.getByCount({start:0,count:1000}).then(function(response){
        $scope.items=response;
    });
}]).
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
        }
        
    };
}]);
