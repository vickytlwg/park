var chargeApp = angular.module("feeCriterionApp", ['ui.bootstrap']);

chargeApp.controller("feeCriterionCtrl", ['$scope', '$http',  'textModal', '$modal', '$timeout', function($scope, $http, textModal,$uibModal, $timeout){
	
	$scope.criterion = {
			items:[],
			checkedIndex:-1
	};
	
	$scope.criterion.checked = function(index){
		if($scope.criterion.items[index].checked){
			$scope.criterion.checkedIndex = index;
			return;
		}
			
		for(var i = 0; i < $scope.criterion.items.length; i++){
			var item = $scope.criterion.items[i];
			if(item.checked != undefined && item.checked == true){
				$scope.criterion.checkedIndex = i;
				return;
			}
		}
		$scope.criterion.checkedIndex = -1;
			
	};
	
	$scope.criterion.remove = function(){
		if($scope.criterion.checkedIndex == -1)
			return;
		var id = $scope.criterion.items[$scope.criterion.checkedIndex].id;
		$http.get('delete/' + id)
		.success(function(response){
			if(response.status == 1001)
				textModal.open($scope, "成功","操作成功");
			else
				textModal.open($scope, "失败","操作失败");
			$scope.criterion.refresh();
		})
		.error(function(response){
			textModal.open($scope, "失败","操作失败");
		});
	};
	
	$scope.criterion.refresh = function(){
		$http.get('get')
		.success(function(response){
			if(response.status == 1001)
				$scope.criterion.items = response.body;
			else
				textModal.open($scope, "失败","操作失败");
		})
		.error(function(response){
			textModal.open($scope, "失败","操作失败");
		});
	};
	
	$scope.criterion.insert = function(){	
		$uibModal.open({
			templateUrl: '/park/views/template/ucNewFeeCriterion.html',
			controller: 'modifyCrtl',
			scope:$scope,
			resolve: {
				index: function(){
						return undefined;
				}
			}
			
		});
	};
	
	$scope.criterion.update = function(){	
		if($scope.criterion.checkedIndex == -1)
			return;
		$uibModal.open({
			templateUrl: '/park/views/template/ucNewFeeCriterion.html',
			controller: 'modifyCrtl',
			scope:$scope,
			resolve: {
				index: function(){
						return $scope.criterion.checkedIndex;
				}
			}
		});
	};

	$scope.criterion.refresh();
	
}]);

chargeApp.controller('modifyCrtl',  function($scope, $modalInstance, $http, $timeout, index){
	
	var url = 'insert';
	if(index != undefined){
		$scope.tempCriterion = $scope.$parent.criterion.items[index];
		url = 'modify';
	}
	else{
		/**
		 * 		
		 */
		$scope.tempCriterion = {	
				id:0,
				name:"",
				freeMins:0,
				startExpense:0.00,
				timeoutPrice:5.00,
				timeoutPriceInterval:15,
				maxExpense:100.00,
				nightStartTime:"20:00",
				nightEndTime: "08:00",
				isOneTimeExpense:false,
				oneTimeExpense: 5.00
		};
	}
	
	
	$scope.loading = false;
	$scope.submitted = false;
	$scope.result = "";
	
	
	$scope.submit = function(){
		$scope.loading = true;
		var args = {
				id:parseInt($scope.tempCriterion.id),
				name:$scope.tempCriterion.name,
				freeMins:parseInt($scope.tempCriterion.freeMins),
				startExpense:parseFloat($scope.tempCriterion.startExpense),
				timeoutPrice:parseFloat($scope.tempCriterion.timeoutPrice),
				timeoutPriceInterval:parseInt($scope.tempCriterion.timeoutPriceInterval),
				maxExpense:parseFloat($scope.tempCriterion.maxExpense),
				nightStartTime:$scope.tempCriterion.nightStartTime,
				nightEndTime: $scope.tempCriterion.nightEndTime,
				isOneTimeExpense:$scope.tempCriterion.isOneTimeExpense == true ? 1 : 0,
				oneTimeExpense: parseFloat($scope.tempCriterion.oneTimeExpense)
		};
		$http.post(url, args)
		.success(function(response){	
			$scope.loading = false;
			$scope.submitted = true;
			if(response.status == 1001){
				$scope.result="成功";
				$timeout(function(){
					$scope.result="";
					$modalInstance.close('ok');
					$scope.$parent.criterion.refresh();
				}, 2000);
			}else{
				$scope.result="失败:" + response.status;
				
			}			
		}).error(function(){
			$scope.loading = false;
			$scope.submitted = true;
			$scope.result="失败";
			//$modalInstance.close('faled');
		});
	};
	
	$scope.close = function(){
		$modalInstance.close('cancel');
	};

});


var feeCriterion = angular.module('feeCriterionApp');

feeCriterion.service('textModal',  ['$uibModal', function($uibModal){
	
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

//show message dilog controller
feeCriterion.controller('textCtrl',  function($scope, $uibModalInstance, $http, msg){
	$scope.text = msg;
	
	$scope.close = function(){
		$uibModalInstance.close('cancel');
	};
});

