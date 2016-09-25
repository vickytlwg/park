angular.module("outsideParkStatus1App",['ui.bootstrap'])
.controller('outsideParkStatus1Ctrl',['$scope','getDataService',function($scope,getDataService){
  $scope.isShow=1;
  getDataService.getZoneCenterInfo().then(function(result){
      $scope.zoneCenters=result;
  });
  $scope.areas=[];
    $scope.getAreaInfo=function(zoneid){
        $scope.isShow=2;
        getDataService.getAreaInfo(zoneid).then(function(result){
            $scope.areas=result;
        });
    };
    $scope.getStreetInfo=function(areaid){
        $scope.isShow=3;
        getDataService.getStreetInfo(areaid).then(function(result){
            $scope.streets=result;
        });
    };
    $scope.getParkInfo=function(streetid){
        $scope.isShow=4;
        getDataService.getParkInfo(streetid).then(function(result){
            $scope.parks=result;
        });
    };
}])
.service('getDataService',['$http','$q',function($http,$q){
    
    var getZoneCenter = function() {
        var deferred = $q.defer();
        var promise = deferred.promise;
        $http({
            url : "/park/zoneCenter/getByStartAndCount",
            method : 'post',
            params : {
                start : 0,
                count : 100
            }
        }).success(function(response) {
            deferred.resolve(response.body);
        });
        return promise;
    };
     var getZoneCenterInfo = function() {
        var deferred = $q.defer();
        var promise = deferred.promise;
        $http({
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
    };
     var getArea = function(zoneid) {
        var deferred = $q.defer();
        var promise = deferred.promise;
        $http({
            url : '/park/area/getByZoneCenterId/' + zoneid,
            method : 'get',
        }).success(function(response) {
            if (response.status == 1001) {
                deferred.resolve(response.body);
            }
        }).error(function(){
            deferred.reject("error");
        });
        return promise;
    };
    var getAreaInfo=function(zoneid){
        var deferred = $q.defer();
        var promise = deferred.promise;
        $http({
            url : '/park/outsideParkInfo/areaInfo/' + zoneid,
            method : 'get',
        }).success(function(response) {
            if (response.status == 1001) {
                deferred.resolve(response.body);
            }
        }).error(function(){
            deferred.reject("error");
        });
        return promise;
    };
    var getStreetInfo=function(areaid){
        var deferred = $q.defer();
        var promise = deferred.promise;
        $http({
            url : '/park/outsideParkInfo/streetInfo/' + areaid,
            method : 'get',
        }).success(function(response) {
            if (response.status == 1001) {
                deferred.resolve(response.body);
            }
        }).error(function(){
            deferred.reject("error");
        });
        return promise;
    };
     var getParkInfo=function(streetid){
        var deferred = $q.defer();
        var promise = deferred.promise;
        $http({
            url : '/park/outsideParkInfo/parkInfo/' + streetid,
            method : 'get',
        }).success(function(response) {
            if (response.status == 1001) {
                deferred.resolve(response.body);
            }
        }).error(function(){
            deferred.reject("error");
        });
        return promise;
    };
     var getStreetByAreaId = function(areaid) {       
        var deferred = $q.defer();
        var promise = deferred.promise;
        $http({
            url : "/park/street/getByAreaid/" + areaid,
            method : 'get'
        }).success(function(response) {
            if (response.status == 1001) {
                deferred.resolve(response.body);
            }
        });
        return promise;
    };
    
    var getOutsideParkByStreetId = function(streetId) {
        var deferred = $q.defer();
        var promise = deferred.promise;
        $http({
            url : "/park/getOutsideParkByStreetId/" + streetId,
            method : 'get'
        }).success(function(response) {
            if (response.status == 1001) {
                deferred.resolve(response.body);
            }
        });
        return promise;
    };
    
    return{
        getZoneCenter:getZoneCenter,
        getZoneCenterInfo:getZoneCenterInfo,
        getAreaInfo:getAreaInfo,
        getStreetInfo:getStreetInfo,
        getParkInfo:getParkInfo,
        getAreaByZoneId:getArea,
        getStreetByAreaId:getStreetByAreaId,
        getOutsideParkByStreetId:getOutsideParkByStreetId
    };
}]);
