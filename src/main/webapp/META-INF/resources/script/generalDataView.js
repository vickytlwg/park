var app=angular.module("generalDataViewApp", []);
app.controller("generalDataViewCtrl", ['$scope', '$interval','$timeout','getDataServicePos',
function($scope, $interval,$timeout,getDataServicePos) {
	var contA;
	contA++;
	console.log('contA----'+contA);
//	var timerInterval = null;
//	timerInterval = $interval(function(){
//        $scope.dayAmountMoney ++;
//        $scope.dayOutCount++;
//        $scope.dayOnlineParkCount++;
//        $scope.dayCarportCount++;
//        $scope.dayOnlineCarportCount++;
//        /*$interval.cancel(timerInterval);*/ // 取消定时器
//    },15000); // 每隔 15秒执行一次
	
    $scope.parkdatasArray = [];
         $scope.dayAmountMoney = 0;
        $scope.dayRealMoney = 0;
        $scope.dayOutCount = 0;
         $scope.dayOnlineParkCount = 0;
        $scope.dayParkCount = 0;3
        $scope.dayCarportCount = 0;
        $scope.dayOnlineCarportCount = 0;
        $scope.dayCarportLeftCount = 0;
        $scope.username="";
    $scope.processData = function(data) {
        $scope.dayCarportLeftCount = 0;
        angular.forEach(data, function(value) {});
    }
    ;
    /* 获取当日零点 */
    function getStartDate() {
        //console.log('getStartDate29');
        var start = new Date(new Date(new Date().toLocaleDateString()).getTime());
        var year = start.getFullYear();
        // 得到年份
        var month = start.getMonth();
        // 得到月份
        var date = start.getDate();
        // 得到日期
        var day = start.getDay();
        // 得到周几
        var hour = start.getHours();
        // 得到小时
        var minu = start.getMinutes();
        // 得到分钟
        var sec = start.getSeconds();
        // 得到秒
        month = month + 1;
        if (month < 10)
            month = "0" + month;
        if (date < 10)
            date = "0" + date;
        if (hour < 10)
            hour = "0" + hour;
        if (minu < 10)
            minu = "0" + minu;
        if (sec < 10)
            sec = "0" + sec;
        var start1 = "";
        start1 = year + "-" + month + "-" + date + " " + hour + ":" + minu + ":" + sec;
        return start1;
    }
    //console.log('getStartDate55');
    startDate = getStartDate();
    /**获取当前时间，精确到分*/
    function getCurrentDate() {
        //console.log('getCurrentDate63');
        var now = new Date();
        var year = now.getFullYear();
        // 得到年份
        var month = now.getMonth();
        // 得到月份
        var date = now.getDate();
        // 得到日期
        var day = now.getDay();
        // 得到周几
        var hour = now.getHours();
        // 得到小时
        var minu = now.getMinutes();
        // 得到分钟
        var sec = now.getSeconds();
        // 得到秒
        month = month + 1;
        if (month < 10)
            month = "0" + month;
        if (date < 10)
            date = "0" + date;
        if (hour < 10)
            hour = "0" + hour;
        if (minu < 10)
            minu = "0" + minu;
        if (sec < 10)
            sec = "0" + sec;
        var time = "";
        // 精确到分
        time = year + "-" + month + "-" + date + " " + hour + ":" + minu + ":" + sec;
        return time;
    }
    //console.log('getCurrentDate86');
    endDate = getCurrentDate();
    /*根据时分秒获得金额统计*/
    var getParkChargeByTime = function() {
        //console.log('getParkChargeByTime91');
        getDataServicePos.getParkChargeByTime(startDate,endDate).then(function(result) {
            $scope.data = result['body'];
            console.log($scope.data);
            //console.log($scope.bdata);
            var fakeData = {
            'status': 0,
            'data': [{
                'value': $scope.data.wechartAmount,
                'name': '微信'
            }, {
                'value': $scope.data.alipayAmount,
                'name': '支付宝'
            }, {
                'value': $scope.data.cashAmount,
                'name': '现金'
            }, {
                'value': $scope.data.otherAmount,
                'name': '徽商银行'
            }, {
                'value': $scope.data.cashAmount2,
                'name': '现金2'
            }]
        };
        $scope.edata = fakeData.data;
        });
    };
    getParkChargeByTime();

    var getparks = function() {
        //console.log('getparks100');
        getDataServicePos.getParks().then(function(result) {
            angular.forEach(result, function(value) {
                $scope.parkid = value.id;
                var parkdatas = {};
                $scope.selectPosdataByParkAndRange(parkdatas, value.id);
            });
        });
    };
    getparks();
    // 获取停车场 得到停车位数据
    $scope.getParkById = function(parkdatas, parkid, online) {
        //console.log('getParkById125');

        getDataServicePos.getParkById(parkid).then(function(result) {
            parkdatas.carportsCount = result.portCount;
            $scope.dayCarportCount += parkdatas.carportsCount || 0;
            if (online) {
                $scope.dayOnlineCarportCount += parkdatas.carportsCount || 0;
                $scope.dayOnlineParkCount += 1;
            }
            parkdatas.selectedParkName = result.name;
            parkdatas.selectedParkNum = result.id;
            parkdatas.principalName = result.contact;
            parkdatas.contactNumber = result.number;
        });
    }
    ;
    // 获取posdata 并处理
    $scope.selectPosdataByParkAndRange = function(parkdatas, parkid) {
        //console.log('selectPosdataByParkAndRange149');
        if ($scope.parkid == undefined || $scope.parkid == null) {
            return;
        }
        var dateInit = new Date();
        var date = dateInit.getFullYear() + "-" + (dateInit.getMonth() + 1) + "-" + dateInit.getDate();
        dateInit.setDate(dateInit.getDate() + 1);
        var dateEnd = dateInit.getFullYear() + "-" + (dateInit.getMonth() + 1) + "-" + dateInit.getDate();
        getDataServicePos.selectPosdataByParkAndRange(parkid, date, dateEnd).then(function(data) {
            parkdatas.dayChargeTotal = 0;
            parkdatas.dayRealReceiveMoney = 0;
            parkdatas.dayCarsCount = 0;
            parkdatas.carsInCount = 0;
            parkdatas.carsOutCount = 0;
            if (data.status == 1002) {
                getPosChargeDataByParkAndRange(parkid, date, dateEnd, parkdatas);
                return;
            }
            data = data['body'];
            if (data != null) {
                for (var i = 0; i < data.length; i++) {
                    if (data[i]['mode'] == 1) {
                        parkdatas.dayChargeTotal += data[i]['money'];
                        parkdatas.dayRealReceiveMoney = $scope.dayRealReceiveMoney + data[i]['giving'] + data[i]['realmoney'] - data[i]['returnmoney'];
                        parkdatas.carsOutCount++;
                    }
                    if (data[i]['mode'] == 0) {
                        parkdatas.dayCarsCount++;
                        parkdatas.carsInCount++;
                    }
                }
            }
            $scope.getParkById(parkdatas, parkid, true);
            $scope.dayAmountMoney += parkdatas.dayChargeTotal || 0;
            $scope.dayRealMoney += parkdatas.dayRealReceiveMoney || 0;
            $scope.dayParkCount++;
            var carportscount = parkdatas.carportsCount || 0;
            $scope.dayCarportLeftCount += (parkdatas.carportsCount || 0 + parkdatas.dayCarsCount || 0 - parkdatas.carsInCount);
            $scope.dayOutCount += parkdatas.carsOutCount || 0;

            $scope.parkdatasArray.push(parkdatas);
        });
    }
    ;
    var getPosChargeDataByParkAndRange = function(parkid, startDay, endDay, parkdatas) {
        //console.log('getPosChargeDataByParkAndRange216');

        getDataServicePos.getPosChargeDataByParkAndRange(parkid, startDay, endDay).then(function(data) {
            if (data.status == 1002) {
                $scope.getParkById(parkdatas, parkid, false);
                $scope.dayParkCount++;
                $scope.parkdatasArray.push(parkdatas);
                return;
            }
            data = data['body'];
            parkdatas.dayChargeTotal = 0;
            parkdatas.dayRealReceiveMoney = 0;
            parkdatas.dayCarsCount = 0;
            parkdatas.carsInCount = 0;
            parkdatas.carsOutCount = 0;
            if (data != null) {
                for (var i = 0; i < data.length; i++) {
                    parkdatas.dayChargeTotal += data[i]['chargeMoney'];
                    parkdatas.dayRealReceiveMoney = parkdatas.dayRealReceiveMoney + data[i]['paidMoney'] + data[i]['givenMoney'] - data[i]['changeMoney'];
                    parkdatas.carsInCount++;
                    if (data[i]['exitDate'] != null) {
                        parkdatas.carsOutCount++;
                        parkdatas.dayCarsCount++;
                    }
                }
            }
            $scope.getParkById(parkdatas, parkid, true);
            $scope.dayAmountMoney += parkdatas.dayChargeTotal || 0;
            $scope.dayRealMoney += parkdatas.dayRealReceiveMoney || 0;
            $scope.dayParkCount++;
            $scope.dayCarportLeftCount += (parkdatas.carportsCount || 0 + parkdatas.dayCarsCount || 0 - parkdatas.carsInCount || 0);

            $scope.dayOutCount += parkdatas.carsOutCount || 0;

            $scope.parkdatasArray.push(parkdatas);
        });
    };
    $scope.toParkInfo = function() {
        //console.log('toParkInfo272');

        parent.location.href = "/parkinfo/parkingInfo/";
    }
    ;
}

]);

app.service("getDataServicePos", function($http, $q) {
    var getParkChargeByRange = function(parkId, startDay, endDay) {
        //console.log('getParkChargeByRange427');
        var deferred = $q.defer();
        var promise = deferred.promise;
        $http({
            url: '/parkinfo/pos/getParkChargeByRange',
            method: 'post',
            data: {
                'parkId': parkId,
                'startDay': startDay,
                'endDay': endDay
            }
        }).success(function(response) {
            deferred.resolve(response);
        });
        return promise;
    };
    var count=0;
    var getParkChargeByTime = function(startDate, endDate) {
        //console.log('getParkChargeByTime444');
    	count++;
    	console.log('count-----'+count);
        var deferred = $q.defer();
        var promise = deferred.promise;
        $http({
            url: '/parkinfo/pos/charge/getParkByMoneya',
            method: 'post',
            data: {
                'startDate': startDate,
                'endDate': endDate
            }
        }).success(function(response) {
            console.log(response);
            deferred.resolve(response);
        }).error(function(error) {});

        return promise;
    };
    
    var selectPosdataByParkAndRange = function(parkId, startDay, endDay) {
        var deferred = $q.defer();
        var promise = deferred.promise;
        $http({
            url: '/parkinfo/pos/selectPosdataByParkAndRange',
            method: 'post',
            data: {
                'parkId': parkId,
                'startDay': startDay,
                'endDay': endDay
            }
        }).success(function(response) {
            deferred.resolve(response);
        });
        return promise;
    };
    var getPosChargeDataByParkAndRange = function(parkId, startDay, endDay) {
        //console.log('getPosChargeDataByParkAndRange483');

        var deferred = $q.defer();
        $http({
            url: '/parkinfo/pos/charge/getByParkAndRange',
            method: 'post',
            data: {
                'parkId': parkId,
                'startDay': startDay,
                'endDay': endDay
            }
        }).success(function(response) {
            deferred.resolve(response);
        });
        return deferred.promise;
    };
    var getParkById = function(id) {
        //console.log('getParkById500');

        var deferred = $q.defer();
        var promise = deferred.promise;
        $http({
            url: '/parkinfo/getPark/' + id,
            method: 'get'
        }).success(function(response) {
            if (response.status = 1001) {
                deferred.resolve(response.body);
            }
            ;
        });
        return promise;
    };
    var getParks = function() {
        //console.log('getParks516');

        var deferred = $q.defer();
        var promise = deferred.promise;
        $http({
            url: '/parkinfo/getParks',
            method: 'get'
        }).success(function(response) {
            if (response.status = 1001) {
                deferred.resolve(response.body);
            }
            ;
        });
        return promise;
    };
    return {
        getParkChargeByRange: getParkChargeByRange,
        selectPosdataByParkAndRange: selectPosdataByParkAndRange,
        getParkById: getParkById,
        getPosChargeDataByParkAndRange: getPosChargeDataByParkAndRange,
        getParks: getParks,
        getParkChargeByTime: getParkChargeByTime
    };
});
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
                    series: [{
                        name: '访问来源',
                        type: 'pie',
                        radius: '55%',
                        center: ['50%', '60%'],
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
