var app = angular.module("presentDataApp", []);
app.controller("presentDataCtrl", ['$scope', 'getDataService', function ($scope, getDataService) {
  $scope.processData = function (data) {
    $scope.dayAmountMoney = 0;
    $scope.dayRealMoney = 0;
    $scope.dayOutCount = 0;
    $scope.dayParkCount = 0;
    $scope.dayCarportCount = 0;
    $scope.dayOnlineCarportCount = 0;
    $scope.dayCarportLeftCount = 0;
    angular.forEach(data, function (value) {
      $scope.dayAmountMoney += value.amountmoney;
      $scope.dayRealMoney += value.realmoney;
      $scope.dayParkCount += value.parkcount;
      $scope.dayCarportCount += value.carportcount;
      $scope.dayOnlineCarportCount += value.onlinecarportcount;
      $scope.dayCarportLeftCount += value.carportleftcount;
      $scope.dayOutCount += value.outcount;
      $scope.dayAmountMoney=parseInt($scope.dayAmountMoney);
    });
  };

  getDataService.getZoneCenterInfo().then(function (result) {
    $scope.zoneCenters = result;
    $scope.processData(result);
    //console.log($scope.dayOnlineCarportCount + "  " + $scope.dayOutCount);
  });
  startDate = new Date().format('yyyy-MM-dd 00:00:00');
  //console.log(startDate);
  endDate = new Date().format('yyyy-MM-dd hh:mm:ss');
  //console.log(endDate);
  /*根据时分秒获得金额统计*/
  var getParkChargeByTime = function () {
    getDataService.getParkChargeByTime(startDate, endDate).then(function (result) {
      $scope.data = result['body'];
      $scope.data.totalAmount=parseInt($scope.data.totalAmount);
      //console.log($scope.data);
      var fakeData = {
        'status': 0,
        'data': [{
          'value': $scope.data.wechartAmount,
          'name': '微信'
        }, {
          'value': $scope.data.alipayAmount,
          'name': '支付宝'
        }, {
          'value': $scope.data.cashAmount2,
          'name': '现金'
        }, {
          'value': $scope.data.otherAmount,
          'name': '徽商银行'
        }, {
          'value': $scope.data.cbcAmount,
          'name': '工商银行'
        },{
          'value': $scope.data.appAmount,
          'name': 'APP'
        }]
      };
      $scope.edata = fakeData.data;
    });
  };
  getParkChargeByTime();
}]);
app.service('getDataService', ['$http', '$q', function ($http, $q) {
  var getParkChargeByTime = function (startDate, endDate) {
    var deferred = $q.defer();
    $http({
      url: '/park/pos/charge/getParkByCountMoney',
      method: 'post',
      data: {
        'startDate': startDate,
        'endDate': endDate
      }
    }).success(function (response) {
      //console.log(response);
      deferred.resolve(response);
    });
    return deferred.promise;
  };
  var getZoneCenterInfo = function () {
    var deferred = $q.defer();
    var promise = deferred.promise;
    $http({
      url: "/park/outsideParkInfo/zoneCenterInfo",
      method: 'post',
      params: {
        start: 0,
        count: 100
      }
    }).success(function (response) {
      if (response.status == 1001) {
        //console.log(response);
        deferred.resolve(response.body);
      }
    });
    return promise;
  };
  return {
    getZoneCenterInfo: getZoneCenterInfo,
    getParkChargeByTime: getParkChargeByTime
  };
}]);
app.directive('pieChart', function($window) {
  //定义饼图指令
  return {
    restrict: 'A',
    link: function($scope, element, attrs) {
      //attrs是DOM元素的属性集合
      var myChart = echarts.init(element[0]);
      $scope.$watch(attrs.eData, function(newValue, oldValue, scope) {
        var legend = [];
        angular.forEach(function(val) {
          legend.push(val.name);
        });
        var option = {
          tooltip: {
            trigger: 'item',
            formatter: "{a} <br/>{b} : {c} ({d}%)"
          },
          legend: {
            orient: 'vertical',
            x: 'left', // 'center' | 'left' | {number},
            y: '35%', // 'center' | 'bottom' | {number}
            //left: '20%',
            data: newValue
          },
          color:['#6abe83','#5291b5', '#FF9900', '#979798', '#e56056'],
          series: [{
            name: '访问来源',
            type: 'pie',
            radius: '55%',
            center: ['60%', '60%'],
            data: newValue,
            itemStyle: {
              emphasis: {
                shadowBlur: 10,
                shadowOffsetX: 0,
                shadowColor: 'rgba(0, 0, 0, 0.5)'
              }
            }
          }]
        };
        myChart.setOption(option);
      }, true);
      window.addEventListener("resize", function() {
        //这里使用$window.onresize方法会使前面的图表无法调整大小
        myChart.resize();
      });
    }
  };
});