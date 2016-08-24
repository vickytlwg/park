var streetApp=angular.module("streetApp",['ui.bootstrap']);
streetApp.controller("streetCtrl",['$scope', '$http','$modal', 'textModal', '$timeout',
function($scope,$http,$uibModal,textModal,$timeout){
    $scope.streets=[];
    $scope.checkedIndex=-1;
    $scope.start=0;
    $scope.count=50;
    $scope.refreshStreet=function(){
        $http({
            url:'/park/street/getByStartAndCount',
            method:'post',
            params:{start:$scope.start,count:$scope.count}
        }).success(function(response){
            if(response.status==1001){
                $scope.streets=response.body;
            }
            else{
               textModal.open($scope,"错误","数据请求失败");
            }
        }).error(function(){
               textModal.open($scope,"错误","数据请求失败");
        });
    };
    $scope.insertStreet=function(){
        $uibModal.open({
            templateUrl: '/park/views/template/ucNewStreet.html',
            controller: 'streetModify',
            scope:$scope,
            resolve: {
                index: function(){
                        return undefined;
                }
            }
        });
    };
    $scope.updateStreet=function(){
        if($scope.checkedIndex==-1){
            alert("请选择");
            return;
        }
        $uibModal.open({
            templateUrl: '/park/views/template/ucNewStreet.html',
            controller: 'streetModify',
            scope:$scope,
            resolve: {
                index: function(){
                        return $scope.checkedIndex;
                }
            }
        });
    };
    $scope.deleteStreet=function(){
        if($scope.checkedIndex==-1){
            alert("请选择");
            return;
        }
        var id=$scope.streets[$scope.checkedIndex].id;
        $http({
            url:'/park/street/delete/'+id,
            method:'get'
        }).success(function(response){
            if(response.status==1001)
            {
                textModal.open($scope, "成功","操作成功");
                $scope.refreshStreet();
            }
            else{
                textModal.open($scope, "失败","操作失败");
            }
        }).error(function(){
             textModal.open($scope, "失败","操作失败");
        });
    };
        $scope.selectChange=function(index){
        if($scope.streets[index].checked){
            $scope.checkedIndex = index;
            return;
        }
        for(var i = 0; i < $scope.streets.length; i++){
            var item = $scope.streets[i];
            if(item.checked != undefined && item.checked == true){
                $scope.checkedIndex = i;
                return;
            }
        }
        $scope.checkedIndex = -1;
    };
    $scope.refreshStreet();
}]);

streetApp.controller("streetModify",function($scope, textModal,$modalInstance, $http, $timeout, index){
    var url = '/park/street/insert';
  // $scope.tempStreet={};
    if(index != undefined){
        $scope.tempStreet = $scope.$parent.streets[index];
        url = '/park/street/update';
    }
    $scope.areas=[];
      $scope.refreshArea=function(){
        $http({
            url:'/park/area/getByStartAndCount',
            method:'post',
            params:{start:$scope.start,count:$scope.count}
        }).success(function(response){
            if(response.status==1001){
                $scope.areas=response.body;
            }
            else{
               textModal.open($scope,"错误","数据请求失败");
            }
        }).error(function(){
               textModal.open($scope,"错误","数据请求失败");
        });
    };
    $scope.refreshArea();
    $scope.streetTypes=[{id:0,name:"自营路段"},{id:1,name:'其它路段'}];
    $scope.loading = false;
    $scope.submitted = false;
    $scope.result="";
    $scope.submit = function(){
        $scope.loading = true;
        $scope.submitted = false;
        delete $scope.tempStreet['checked'];
        $http({
            url:url,
            method:'post',
            data:$scope.tempStreet,
        }).success(function(response){          
            if(response.status==1001){ 
               $scope.loading =false;
               $scope.submitted = true;
               $scope.result="成功";             
               $timeout(function(){                                
                   $scope.result="";
                    
                    $modalInstance.close('ok');
               },2000);
                                        
               $scope.$parent.refreshStreet();
              
            }
            else
            {
                textModal.open($scope, "失败","操作失败");
            }
        }).error(function(){
            textModal.open($scope, "失败","操作失败");
        });
    };
    $scope.close = function(){
        $modalInstance.close('cancel');
    };

});

var streetService=angular.module("streetApp");
streetService.service('textModal',  ['$uibModal', function($uibModal){
    
    this.open = function($scope, header, body){
        $scope.textShowModal = $uibModal.open({
            templateUrl: '/park/views/template/text-modal.html',
            controller: 'textCtrl',
            scope: $scope,
            resolve:{
                msg:function(){ return {header:header, body:body}; }
            }
        });
    };
    
    this.close = function($scope, header, body){
        $scope.textShowModal.close('cancel');
    };
  
    
}]);


  streetService.controller('textCtrl',  function($scope, $uibModalInstance, $http, msg){
    $scope.text = msg;   
    $scope.close = function(){
        $uibModalInstance.close('cancel');
    };
});
