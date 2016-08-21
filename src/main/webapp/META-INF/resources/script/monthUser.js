var monthUserApp=angular.module("monthUserApp",['ui.bootstrap']);
monthUserApp.controller("monthUserCtrl",['$scope', '$http','$modal', 'textModal', '$timeout',
function($scope,$http,$uibModal,textModal,$timeout){
    $scope.users=[];
    $scope.checkedIndex=-1;
    $scope.start=0;
    $scope.count=50;
    $scope.refreshUser=function(){
        $http({
            url:'/park/monthUser/getByStartAndCount',
            method:'post',
            params:{start:$scope.start,count:$scope.count}
        }).success(function(response){
            if(response.status==1001){
                $scope.users=response.body;
            }
            else{
               textModal.open($scope,"错误","数据请求失败");
            }
        }).error(function(){
            textModal.open($scope,"错误","数据请求失败");
        });
    };
    $scope.insertUser=function(){
        $uibModal.open({
            templateUrl: '/park/views/template/ucNewMonthUser.html',
            controller: 'monthUserModify',
            scope:$scope,
            resolve: {
                index: function(){
                        return undefined;
                }
            }
        });
    };
    $scope.updateUser=function(){
        if($scope.checkedIndex==-1){
            alert("请选择");
            return;
        }
        $uibModal.open({
            templateUrl: '/park/views/template/ucNewMonthUser.html',
            controller: 'monthUserModify',
            scope:$scope,
            resolve: {
                index: function(){
                        return $scope.checkedIndex;
                }
            }
        });
    };
    $scope.deleteUser=function(){
        if($scope.checkedIndex==-1){
            alert("请选择");
            return;
        }
        var id=$scope.users[$scope.checkedIndex].id;
        $http({
            url:'/park/monthUser/delete/'+id,
            method:'get'
        }).success(function(response){
            if(response.status==1001)
            {
                textModal.open($scope, "成功","操作成功");
                $scope.refreshUser();
            }
            else{
                textModal.open($scope, "失败","操作失败");
            }
        }).error(function(){
             textModal.open($scope, "失败","操作失败");
        });
    };
        $scope.selectChange=function(index){
        if($scope.users[index].checked){
            $scope.checkedIndex = index;
            return;
        }
        for(var i = 0; i < $scope.users.length; i++){
            var item = $scope.users[i];
            if(item.checked != undefined && item.checked == true){
                $scope.checkedIndex = i;
                return;
            }
        }
        $scope.checkedIndex = -1;
    };
    $scope.refreshUser();
}]);

monthUserApp.controller("monthUserModify",function($scope, textModal,$modalInstance, $http, $timeout, index){
    var url = '/park/monthUser/insert';
    if(index != undefined){
        $scope.tempUser = $scope.$parent.users[index];
        url = '/park/monthUser/update';
    }
    $scope.parks=[];
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
            }
        });
    };
    $scope.getParks();
    $scope.submit = function(){
        $http({
            url:url,
            method:'post',
            data:$scope.tempUser,
        }).success(function(response){
            if(response.status==1001){
               textModal.open($scope, "成功","操作成功");
                $scope.$parent.refreshUser();
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

var monthUserService=angular.module("monthUserApp");
monthUserService.service('textModal',  ['$uibModal', function($uibModal){
    
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


  monthUserService.controller('textCtrl',  function($scope, $uibModalInstance, $http, msg){
    $scope.text = msg;   
    $scope.close = function(){
        $uibModalInstance.close('cancel');
    };
});
