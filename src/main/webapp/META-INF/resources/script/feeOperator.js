var feeOperatorApp=angular.module("feeOperatorApp",['ui.bootstrap']);
feeOperatorApp.controller("feeOperatorCtrl",['$scope', '$http','$modal', 'textModal', '$timeout',
function($scope,$http,$uibModal,textModal,$timeout){
    $scope.feeOperatores=[];
    $scope.checkedIndex=-1;
    $scope.start=0;
    $scope.count=50;
    $scope.refreshFeeOperator=function(){
        $http({
            url:'/park/feeOperator/getByStartAndCount',
            method:'post',
            params:{start:$scope.start,count:$scope.count}
        }).success(function(response){
            if(response.status==1001){
                $scope.feeOperatores=response.body;
            }
            else{
               textModal.open($scope,"错误","数据请求失败");
            }
        }).error(function(){
               textModal.open($scope,"错误","数据请求失败");
        });
    };
    $scope.insertFeeOperator=function(){
        $uibModal.open({
            templateUrl: '/park/views/template/ucNewFeeOperator.html',
            controller: 'feeOperatorModify',
            scope:$scope,
            resolve: {
                index: function(){
                        return undefined;
                }
            }
        });
    };
    $scope.updateFeeOperator=function(){
        if($scope.checkedIndex==-1){
            alert("请选择");
            return;
        }
        $uibModal.open({
            templateUrl: '/park/views/template/ucNewFeeOperator.html',
            controller: 'feeOperatorModify',
            scope:$scope,
            resolve: {
                index: function(){
                        return $scope.checkedIndex;
                }
            }
        });
    };
    $scope.deleteFeeOperator=function(){
        if($scope.checkedIndex==-1){
            alert("请选择");
            return;
        }
        var id=$scope.feeOperatores[$scope.checkedIndex].id;
        $http({
            url:'/park/feeOperator/delete/'+id,
            method:'get'
        }).success(function(response){
            if(response.status==1001)
            {
                textModal.open($scope, "成功","操作成功");
                $scope.refreshFeeOperator();
            }
            else{
                textModal.open($scope, "失败","操作失败");
            }
        }).error(function(){
             textModal.open($scope, "失败","操作失败");
        });
    };
        $scope.selectChange=function(index){
        if($scope.feeOperatores[index].checked){
            $scope.checkedIndex = index;
            return;
        }
        for(var i = 0; i < $scope.feeOperatores.length; i++){
            var item = $scope.feeOperatores[i];
            if(item.checked != undefined && item.checked == true){
                $scope.checkedIndex = i;
                return;
            }
        }
        $scope.checkedIndex = -1;
    };
    $scope.refreshFeeOperator();
}]);

feeOperatorApp.controller("feeOperatorModify",function($scope, textModal,$modalInstance, $http, $timeout, index){
    var url = '/park/feeOperator/insert';
    if(index != undefined){
        $scope.tempFeeOperator = $scope.$parent.feeOperatores[index];
        url = '/park/feeOperator/update';
    }
    $scope.signs=[{value:false,status:'未签到'},{value:true,status:'已签到'}];

    $scope.streets=[];
    $scope.getStreets=function(){
        $http({
            url:'/park/street/getByStartAndCount',
            method:'post',
            params:{start:0,count:50}
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
    $scope.getStreets();
    
    $scope.loading = false;
    $scope.submitted = false;
    $scope.result="";
    $scope.submit = function(){
        $scope.loading = true;
        $scope.submitted = false;
        delete $scope.tempFeeOperator['checked'];
        $http({
            url:url,
            method:'post',
            data:$scope.tempFeeOperator,
        }).success(function(response){          
            if(response.status==1001){ 
               $scope.loading =false;
               $scope.submitted = true;
               $scope.result="成功";             
               $timeout(function(){                                
                   $scope.result="";                   
                   $modalInstance.close('ok');
               },2000);
                                        
               $scope.$parent.refreshFeeOperator();
              
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

var feeOperatorService=angular.module("feeOperatorApp");
feeOperatorService.service('textModal',  ['$uibModal', function($uibModal){
    
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


  feeOperatorService.controller('textCtrl',  function($scope, $uibModalInstance, $http, msg){
    $scope.text = msg;   
    $scope.close = function(){
        $uibModalInstance.close('cancel');
    };
});
