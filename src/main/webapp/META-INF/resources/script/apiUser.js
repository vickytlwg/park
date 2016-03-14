var apiUserApp = angular.module("apiUserApp", ['ui.bootstrap']);

apiUserApp.controller("apiUserCtrl", ['$scope', '$http',  '$uibModal', function($scope, $http, $uibModal){
	$scope.apiUsers = [];
	$scope.currentCheckedUserIndex = -1;
	
	$scope.tempUser = {
			'username':'',
			'companyInfo':'',
			'contact':'',
			'number':'',			
	};
	
	
	$scope.userCount = 0;
	$scope.currentPage = 1;
	$scope.pageSize = 20;
	$scope.pageNumber = 0;
	
	$scope.showGetUserError = false;
	
	
	$scope.refreshUser = function(){
		
		$http.post('items', {'start': ($scope.currentPage - 1) * $scope.pageSize, 'len': $scope.pageSize})
		.success(function(response){
			$scope.showGetUserLoading = false;
			if(response.status == 1001){
				$scope.apiUsers = response.body.users;
				$scope.showGetUserError = false;			
			}else{
				$scope.showGetUserError = true;
			}		
		})
		.error(function(){
			$scope.showGetUserError = true;
		});
		
		$scope.getUserCount();
	};
	
	
	$scope.getUserCount = function(){
		$http.get('count')
		.success(function(response){
			if(response.status == 1001){
				$scope.userCount = response.body.count;
				$scope.pageNumber = $scope.userCount / $scope.pageNumber + 1;
			}else{
				$scope.userCount = 0;
				$scope.pageNumber = 0;
			}
		})
		.error(function(){
			$scope.userCount = 0;
			$scope.pageNumber = 0;
		});
	};
	
	$scope.addUserModal = function(){
		$scope.tempUser.username='';
		$scope.tempUser.companyInfo='';
		$scope.tempUser.contact='';
		$scope.tempUser.number='';		
		$uibModal.open({
			templateUrl: '/park/views/template/ucApiUser.html',
			controller: 'userModifyController',
			scope:$scope			
		});
	};
	
	$scope.updateUserModal = function(){
		$scope.tempUser = $scope.$scope.apiUsers[$scope.currentCheckedUserIndex];		
		$uibModal.open({
			templateUrl: '/park/views/template/ucApiUser.html',
			controller: 'userModifyController',
			scope:$scope			
		});
	};
	
	$scope.modalContent = "";
	
	$scope.deleteUserModal = function(){
		$scope.modalContent="确定删除?"
		$scope.tempUser = $scope.$scope.apiUsers[$scope.currentCheckedUserIndex];		
		$uibModal.open({
			templateUrl: 'template/ucApiUserToken.html',
			controller: 'userModifyController',
			scope:$scope			
		});
	};
	

	$scope.tokenModal = function(){
		var tokenId = $scope.apiUsers[$scope.currentCheckedUserIndex].tokenId;
		$http.get('/park/token/' + tokenId).success(function(response){
			if(response.status == 1001)
				$scope.modalContent = response.body.token;
			else
				alert('error');
		}).error(function(){
			alert("error");
		});
		
	};
	
	
	$scope.checked = function(index){
		$scope.currentCheckedUserIndex = index;
	};
	
	$scope.nextPage = function(index){
		$scope.currentPage = index;
		$scope.refreshUser();
	}
	
	$scope.refreshUser();
	
}])
.controller('userModifyController', ['$scope', '$http', function($scope, $http){
	
	
	$scope.insertUser = function(){
		$http.post('insert', $scope.tempUser)
		.success(function(response){
			alert("success");
		}).error(function(){
			alert("failed");
		});
	};
	
	$scope.updateUser = function(){
		var checkedUser = $scope.apiUsers[$scope.currentCheckedUserIndex];
		$http.post('update', checkedUser)
		.success(function(response){
			alert("success");
		}).error(function(){
			alert("failed");
		});
		
	};
	
	$scope.deleteUser = function(){
		if($scope.currentCheckedUserIndex <= 0)
			return;
		var userId = $scope.apiUsers[$scope.currentCheckedUserIndex].id;
		$http.get('delete/' + userId)
		.success(function(response){
			if(response.status == 1001)
				alert('delete sucessully');
			else
				alert('delete failed');
		}).error(function(){
			alert('delete failed');
		});
		
	};
}]);