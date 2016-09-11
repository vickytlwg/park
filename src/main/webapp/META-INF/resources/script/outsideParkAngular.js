var outsideParkApp= angular.module("outsideParkApp",['ui.bootstrap']);
outsideParkApp.controller("outsideParkCtrl",function($scope, $http,$timeout,$q,getPositionData){
   $scope.streetid;
   getPositionData.getZoneCenter().then(function(result){
            $scope.zoneCenters=result;
        });
	$scope.getAreas=function(){
		getPositionData.getArea($scope.zoneCenterId).then(function(result){
		    $scope.areas=result;
		});
	};
	$scope.getStreets=function(){
		getPositionData.getStreetById($scope.areaId).then(function(result){
		    $scope.streets=result;
		});
	};
	$scope.clearPosition=function(){
	     $scope.areas=[];
	     $scope.streets=[];
	     $scope.zoneCenterId=-1;
	};
	 
});
outsideParkApp.factory("getPositionData",function($http,$q){

	  var getZoneCenter=function(){
	        var deferred=$q.defer();
            var promise=deferred.promise;
	        $http({
	            url:"/park/zoneCenter/getByStartAndCount",
	            method:'post',
	            params:{start:0,count:100}
	        }).success(function(response){	 
	                deferred.resolve(response.body);           
	        });
	        return promise;
	    };
	   var getArea=function(zoneid){
	       var deferred=$q.defer();
           var promise=deferred.promise;
		   if (!zoneid) {
			return;
		}
	        $http({
	            url:'/park/area/getByZoneCenterId/'+zoneid,
	            method:'get',
	        }).success(function(response){
	            if(response.status==1001){
	                 deferred.resolve(response.body);  
	            }
	        });
	        return promise;
	    };
	    var getStreetById=function(areaid){
	       var deferred=$q.defer();
           var promise=deferred.promise;	       
	         $http({
	             url:"/park/street/getByAreaid/"+areaid,
	             method:'get'
	         }).success(function(response){
	             if(response.status==1001){
	                deferred.resolve(response.body); 
	             }
	         });
	         return promise;
	     }; 
	     return {
	         getZoneCenter:getZoneCenter,
	         getArea:getArea,	         
	         getStreetById:getStreetById
	     };
	      
});