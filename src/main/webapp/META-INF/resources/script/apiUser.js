var apiUserApp = angular.module("apiUserApp", []);
apiUserApp.controller("apiUserCtrl", function($scope, $http){
	$scope.apiUsers = [];
	$scope.currentCheckedUserIndex = -1;
	$scope.newUser = {
			'username':'',
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
	
	$scope.insertUser = function(){
		$http.post('insert', $scope.newUser)
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
	
	$scope.getToken = function(){
		var tokenId = $scope.apiUsers[$scope.currentCheckedUserIndex].tokenId;
		$http.get('/park/token/' + tokenId).success(function(response){
			if(response.status == 1001)
				alert(response.body.token);
			else
				alert('error');
		}).error(function(){
			alert("error");
		});
		
	}
	
	$scope.checked = function(index){
		$scope.currentCheckedUserIndex = index;
	}
	
	$scope.nextPage = function(index){
		$scope.currentPage = index;
		$scope.refreshUser();
	}
	
	$scope.refreshUser();
});