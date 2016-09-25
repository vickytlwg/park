var chargeApp = angular.module("feeDetailApp", ['ui.bootstrap']);

chargeApp.controller("feeDetailCtrl", ['$scope', '$http', 'textModal', 'textModalTest','$modal', '$timeout',
function($scope, $http, textModal,textModalTest, $uibModal, $timeout) {

    //define table content
    $scope.detail = {
        items : [],
        loading : false,
        page : {
            hidden : true,
            allCounts : 0,
            size : 100,
            indexRange : [1],
            index : 1
        }
    };

    $scope.detail.getCount = function() {
        $http.get('count').success(function(response) {
            if (response.status == 1001) {
                $scope.detail.page.hidden = false;
                $scope.detail.page.allCounts = response.body;
                var maxIndex = Math.ceil($scope.detail.page.allCounts / $scope.detail.page.size);
                $scope.detail.page.indexRange = [1];
                for (var i = 2; i <= maxIndex; i++)
                    $scope.detail.page.indexRange.push(i);

            } else
                textModal.open($scope, "错误", "获取计费错误: " + response.status);
        }).error(function(response) {
            textModal.open($scope, "错误", "获取计费错误: " + response.status);

        });
    };

    //previous page
    $scope.detail.previousPage = function() {
        if ($scope.detail.page.index <= 1)
            return;
        $scope.detail.page.index--;
        $scope.detail.getPage();
    };

    //first page
    $scope.detail.firstPage = function() {
        if ($scope.detail.page.index <= 1)
            return;
        $scope.detail.page.index = 1;
        $scope.detail.getPage();
    };

    //next page
    $scope.detail.nextPage = function() {
        if ($scope.detail.page.index >= $scope.detail.page.indexRange.length)
            return;
        $scope.detail.page.index++;
        $scope.detail.getPage();
    };

    //last page
    $scope.detail.lastPage = function() {
        if ($scope.detail.page.index >= $scope.detail.page.indexRange.length)
            return;
        $scope.detail.page.index = $scope.detail.page.indexRange.length;
        $scope.detail.getPage();
    };

    //get one page detail
    $scope.detail.getPage = function() {
        if ($scope.detail.page.index > $scope.detail.page.indexRange.length) {
            textModal.open($scope, "错误", "当前页不存在!");
            return;
        }
        $scope.detail.loading = true;

        var pageArgs = {
            page : {
                index : $scope.detail.page.index,
                size : $scope.detail.page.size
            },
            property : ["id", "name", "sex", "phone", "createTime"],
            order : {
                createTime : true
            }
        };

        $http.post('page', {
            low : ($scope.detail.page.index - 1) * $scope.detail.page.size,
            count : $scope.detail.page.size
        }).success(function(response) {
            $scope.detail.loading = false;

            if (response.status == 1001) {
                $scope.detail.items = [];
                $scope.detail.items = response.body;
            } else
                textModal.open($scope, "错误", "获取计费信息错误:" + response.status);

        }).error(function(response) {
            $scope.detail.loading = false;
            textModal.open($scope, "错误", "获取计费信息错误: " + +response.status);

        });
    };

    //init page
    $scope.detail.getCount();
    $scope.detail.refresh = $scope.detail.getPage;
    $scope.detail.getPage();

}]);

var feeDetail = angular.module('feeDetailApp');

feeDetail.service('textModal', ['$uibModal',
function($uibModal) {

    this.open = function($scope, header, body) {
        $scope.textShowModal = $uibModal.open({
            templateUrl : '/park/views/template/text-modal.html',
            controller : 'textCtrl',
            scope : $scope,
            resolve : {
                msg : function() {
                    return {
                        header : header,
                        body : body
                    };
                }
            }
        });
    };

    this.close = function($scope, header, body) {
        $scope.textShowModal.close('cancel');
    };

}]);
feeDetail.service('textModalTest', function($uibModal) {
    var modalInstance;
    var open=function($scope){
    modalInstance = $uibModal.open({
        templateUrl:'myModalTest',
        scope : $scope,
  //      controller:'feeDetailCtrl',
        backdrop:'static'
    });
    modalInstance.opened.then(function(        
    ){
       console.log('modalInstance is opened'); 
    });
    modalInstance.result.then(function(result){
        console.log(result);
    });
};
    
    var close=function(result){
        modalInstance.close(result);
        return '能得到消息吗';
    };
    return{
        open:open,
        close:close
    };
});
//show message dilog controller
feeDetail.controller('textCtrl', function($scope, $uibModalInstance, $http, msg) {
    $scope.text = msg;
    $scope.close = function() {
        $uibModalInstance.close('cancel');
    };
});

