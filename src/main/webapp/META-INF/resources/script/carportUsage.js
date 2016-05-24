(function($){
	$.fn.carportUsage={};
	$.fn.carportUsage.initial=function(){
		dateInitialparkcharge();
		dateInitial();
		dateInitialparkcharge();
		
		chartPark();		
		//chartParkPeriodCharge();
		getParkChargeData();
//		chartCarportCharge();
//		chartCarportUsage();	
		chartCarportPeriodCharge();
		getTotalCharge();
		getcarportCount();
		getCarport();
		getCarportCharge();
		getCarportChargeData();
		
	//	dateInitialparkcharge();
		$('#date').on('change', $(this), function(){
			getTotalCharge();
			getCarportCharge();
			chartPark();
		//	calZhouzhuanlv();
		//	getcarportCount();
		});
		$('#park-select').on('change', $(this), function(){
			getTotalCharge(); 
			getcarportCount();
			getCarport();
			getParkChargeData();
			getCarportCharge();
			chartPark();
			//calZhouzhuanlv();
		});
		$('#parkMonth').on('change', $(this), function(){
			getParkChargeData();
		});
		$('#carport-select').on('change',$(this),function(){
		    getCarportCharge();
		    getCarportChargeData();
		    chartCarport();
		})
		$('#carportMonth').on('change',$(this),function(){
		    renderCarportStatusChart();
		    getCarportChargeData();
		    chartCarport();
		})
	};
	
	var dateInitial=function(){
		$('.date').val(new Date().format('yyyy-MM-dd'));
		$('.date').datepicker({
			autoClose: true,
		    dateFormat: "yyyy-mm-dd",
		    days: ["星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"],
		    daysShort: ["周日", "周一", "周二", "周三", "周四", "周五", "周六"],
		    daysMin: ["日", "一", "二", "三", "四", "五", "六"],
		    months: ["一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月"],
		    monthsShort: ["1月", "2月", "3月", "4月", "5月", "6月", "7月", "8月", "9月", "10月", "11月", "12月"],
		    showMonthAfterYear: true,
		    viewStart: 0,
		    weekStart: 1,
		    yearSuffix: "年",
		    isDisabled: function(date){return date.valueOf() > Date.now() ? true : false;}
		
		});
	};
	var dateInitialparkcharge=function(){
		$('#parkMonth,#carportMonth').val(new Date().format('yyyy-MM'));
		$('#parkMonth,#carportMonth').datepicker({
			autoClose: true,
		    dateFormat: "yyyy-mm",
		//    days: ["星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"],
		//    daysShort: ["周日", "周一", "周二", "周三", "周四", "周五", "周六"],
		//    daysMin: ["日", "一", "二", "三", "四", "五", "六"],
		    months: ["一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月"],
		    monthsShort: ["1月", "2月", "3月", "4月", "5月", "6月", "7月", "8月", "9月", "10月", "11月", "12月"],
		    showMonthAfterYear: true,
		    viewStart: 1,
		    weekStart: 1,
		    yearSuffix: "年",
		    isDisabled: function(date){return date.valueOf() > Date.now() ? true : false;}
		
		});
	};
	var chartCarport=function(){
		var title="停车位使用率";
		var dateselect=$('#parkMonth').val();
        var parkid=$('#park-select').val();
        var carportId= $('#carport-select').find("option:selected").attr("id");
        var dateStart=new Date(dateselect.substring(0,7)+'-01');
        var dateEnd=dateStart;
        dateEnd.setMonth(dateStart.getMonth()+2);
        var unused=0;
        var used=0;
        $.ajax({
            url:$.fn.config.webroot+"/getCarportUsage?carportId="+carportId+"&startDay="+dateselect.substring(0,7)+'-01'+"&endDay="+dateEnd.getFullYear()+'-'+dateEnd.getMonth()+'-01',
            type:'get',
            success:function(data){
                data=data['body'];
                unused=parseFloat(data['unusage']);
                used=parseFloat(data['usage']);                 
            var data=[{
            type: 'pie',
            name: '使用率',
            data: [
                {name:'空闲率',   y:unused,color:"#778588"},
                {
                    name: '停车率',
                    y: used,
                    color:"#F73809",
                    sliced: true,
                    selected: true
                },
            ]
        }]
        var chartposition=$('#chart-content-carport')
        renderchart(title,chartposition,data);
                }}) 
		
	};
	var getCarportCharge=function(){
	    var date = $('#date').val();
        var parkid=$('#park-select').val();
        var carportId=$('#carport-select').val();
        var dateStart=date;
        var dateInit=new Date(date);
        dateInit.setDate(dateInit.getDate()+1);
        var dateEnd=dateInit.getFullYear()+"-"+(dateInit.getMonth()+1)+"-"+dateInit.getDate();
        var data={"parkId":parkid,"startDay":dateStart,"endDay":dateEnd,"carportId":carportId};
        $.ajax({
            url: '/park/pos/selectPosdataByParkAndCarportId',
            type: 'post',
            contentType: 'application/json;charset=utf-8',          
            datatype: 'json',
            data:$.toJSON(data),
            success: function(data){
                data=data['body'];
                var chargeTotal=0;
                var realReceiveMoney=0;
                if(data!=undefined&&data!=null){
                   for (var i = 0; i < data.length; i++) {
                    if (data[i]['mode']==1) {
                        chargeTotal=chargeTotal+data[i]['money'];
                        realReceiveMoney=realReceiveMoney+data[i]['giving']+data[i]['realmoney']-data[i]['returnmoney'];
                        
                    }}}
                
                    $.fn.carportUsage.carportTotalMoney=chargeTotal;
                    $.fn.carportUsage.carportRealMoney=realReceiveMoney;
                    chartCarportCharge();
                    renderCarportStatusChart();
            }});
	};
	var getTotalCharge=function(){
		var date = $('#date').val();
		var parkid=$('#park-select').val();
		var dateStart=date;
		var dateInit=new Date(date);
		dateInit.setDate(dateInit.getDate()+1);
		var dateEnd=dateInit.getFullYear()+"-"+(dateInit.getMonth()+1)+"-"+dateInit.getDate();
		var data={"parkId":parkid,"startDay":dateStart,"endDay":dateEnd};
		$.ajax({
			url: '/park/pos/selectPosdataByParkAndRange',
			type: 'post',
			contentType: 'application/json;charset=utf-8',			
			datatype: 'json',
			data:$.toJSON(data),
			success: function(data){
				data=data['body'];
				var chargeTotal=0;
				var realReceiveMoney=0;
				var parkTimes=0;
				for (var i = 0; i < data.length; i++) {
					if (data[i]['mode']==1) {
						chargeTotal=chargeTotal+data[i]['money'];
						realReceiveMoney=realReceiveMoney+data[i]['giving']+data[i]['realmoney']-data[i]['returnmoney'];
						
					}
					if (data[i]['mode']==0) {
						parkTimes++;
					}
				}
				$.fn.carportUsage.chargeTotal=chargeTotal;
				$.fn.carportUsage.realReceiveMoney=realReceiveMoney;
				$('#totalMoney').text(realReceiveMoney);
				$('#totalparkTimes').text(parkTimes);
				calZhouzhuanlv();
			}
		})
	}
	
	var getcarportCount=function(){	
		var parkid=$('#park-select').val();	
		$.ajax({
			url: '/park/getBusinessCarportCount?parkId='+parkid,
			type: 'get',
			contentType: 'application/json;charset=utf-8',			
			datatype: 'json',			
			success: function(data){
				data=data['body'];			
				$('#carportCount').text(data['count']);
				calZhouzhuanlv();
			}
		})
		
	}
	var calZhouzhuanlv=function(){
		var corportCount=parseFloat($('#carportCount').text());
		var totalparkTime=parseFloat($('#totalparkTimes').text());
		var tmpresult=totalparkTime/corportCount;
		$('#zhouzhuanlv').text(tmpresult.toFixed(2));
		chartParkCharge();
	}
	var chartPark=function(){
		var title="停车场使用率";
		var date = $('#date').val();
        var parkid=$('#park-select').val();
        var dateStart=date;
        var dateInit=new Date(date);
        dateInit.setDate(dateInit.getDate()+1);
        var dateEnd=dateInit.getFullYear()+"-"+(dateInit.getMonth()+1)+"-"+dateInit.getDate();
        var unused=0;
        var used=0;
		$.ajax({
		    url:$.fn.config.webroot+"/getParkUsage?parkId="+parkid+"&startDay="+dateStart+"&endDay="+dateEnd,
		    type:'get',
		    success:function(data){
		        data=data['body'];
		        unused=parseFloat(data['unusage']);
		        used=parseFloat(data['usage']);
		        
		    var data=[{
            type: 'pie',
            name: '使用率',         
            data: [
                {name:'整体空闲', y:unused,color:"#22DD6D"},
                {
                    name: '整体使用',
                    color:"#CC3370",
                    y: used,
                    sliced: true,
                    selected: true
                },
            ]
        }]
        var chartposition=$('#chart-content-park');
        renderchart(title,chartposition,data);
		    }
		})
		
	}
	var getCarport=function(){
		var parkId=$('#park-select').val();
		$.ajax({
			url:$.fn.config.webroot + "/getBusinessCarportDetail?low=" + 0 + "&count=" + 300 +  "&parkId=" + parkId +"&_t=" + (new Date()).getTime(),
			type: 'get',
			success: function(data){
				data=data['body'];
				data.sort(
					function(a,b){
						return a['carportNumber']>b['carportNumber']? 1:-1;
					}	
				);
				var carportSelect=$('#carport-select');
				carportSelect.html('');
				for(var i = 0; i < data.length; i++){					
					carportSelect.append($('<option value = ' + data[i]['carportNumber']+' id='+data[i]['id'] + '>' + data[i]['carportNumber'] +'</option>'));																		
				}
				chartCarport();
		//		renderCarportStatusChart();
			}
		});
	}
	var chartParkCharge=function(){
		var title="停车场费用";
		var data=[{
            name: "停车场费用",
            data: [{color:"#DDDF00",y:$.fn.carportUsage.chargeTotal},{color:"#3D11EE",y:$.fn.carportUsage.realReceiveMoney}]
        }]
		var chartposition=$('#chart-content-park-charge');
		renderchartcolumn(title,chartposition,data);
	}	
	//获取停车场的收费信息
	var getParkChargeData=function(){	
		$.fn.loader.appendLoader($('#chart-park-period-charge'));
		var dateselect=$('#parkMonth').val();
		var parkid=$('#park-select').val();
		var dateStart=new Date(dateselect.substring(0,7)+'-01');
		var dateEnd=dateStart;
			dateEnd.setMonth(dateStart.getMonth()+2);

		var data={"parkId":parkid,"startDay":dateselect.substring(0,7)+'-01',"endDay":dateEnd.getFullYear()+'-'+dateEnd.getMonth()+'-01'};
		$.ajax({
			url:"/park/pos/getParkChargeByRange",
			type: 'post',
			contentType: 'application/json;charset=utf-8',			
			datatype: 'json',
			data:$.toJSON(data),
			success: function(data){
				$.fn.loader.removeLoader($('#chart-park-period-charge'));
				var catagory=[];
				var totalMoney=[];
				var realMoney=[];
				$.each(data,function(name,value){
					var date=new Date(parseInt(name));
					var month=date.getMonth()+1;
					var day=date.getDate();
					catagory.push(month+'-'+day);
					totalMoney.push(value['totalMoney']);
					realMoney.push(value['realMoney']);
				})
				var chartposition = $('#chart-park-period-charge');
				var title="停车场费用";
				chartParkPeriodCharge(chartposition,catagory,totalMoney,realMoney,title);
			}
		})
	}
	
	  //获取停车位的收费信息 
	   var getCarportChargeData=function(){   
        $.fn.loader.appendLoader($('#carportChargeChart'));
        var dateselect=$('#parkMonth').val();
        var parkid=$('#park-select').val();
        var carportId=$('#carport-select').val();
        var dateStart=new Date(dateselect.substring(0,7)+'-01');
        var dateEnd=dateStart;
            dateEnd.setMonth(dateStart.getMonth()+2);

        var data={"parkId":parkid,"carportId":carportId,"startDay":dateselect.substring(0,7)+'-01',"endDay":dateEnd.getFullYear()+'-'+dateEnd.getMonth()+'-01'};
        $.ajax({
            url:"/park/pos/getCarportChargeByRange",
            type: 'post',
            contentType: 'application/json;charset=utf-8',          
            datatype: 'json',
            data:$.toJSON(data),
            success: function(data){
                $.fn.loader.removeLoader($('#carportChargeChart'));
                var catagory=[];
                var totalMoney=[];
                var realMoney=[];
                $.each(data,function(name,value){
                    var date=new Date(parseInt(name));
                    var month=date.getMonth()+1;
                    var day=date.getDate();
                    catagory.push(month+'-'+day);
                    totalMoney.push(value['totalMoney']);
                    realMoney.push(value['realMoney']);
                })
                var chartposition = $('#carportChargeChart');
                var title="停车位费用";
                chartParkPeriodCharge(chartposition,catagory,totalMoney,realMoney,title);
            }
        });
    }
    
	var renderCarportStatusChart = function(){
        event.stopPropagation();
        var id = $('#carport-select').find("option:selected").attr("id");
        var dateselect=$('#carportMonth').val();
        var parkid=$('#park-select').val();
        var dateStart=new Date(dateselect.substring(0,7)+'-01');
        var dateEnd=dateStart;
            dateEnd.setMonth(dateStart.getMonth()+2);
            
        var startDay = dateselect.substring(0,7)+'-01';
        var endDay = dateEnd.getFullYear()+'-'+dateEnd.getMonth()+'-01';
        $.ajax({
            url:$.fn.config.webroot + "/getDayCarportStatusDetail?carportId="+ id + "&startDay=" + startDay + "&endDay=" + endDay,
            type:'get',
            success: function(data){
                var carportUsage = data['body']['carportStatusDetail'];
                if(carportUsage.length == 0){
                    $('#carportUsageChart').html('');
                    return;
                }                    
                var chartData = [];
                var parsedStartDay = Date.parse(startDay);
                var parsedEndDay = Date.parse(endDay);
                chartData.push([parsedStartDay, null, null]);
                for(var i = 0; i < carportUsage.length; i++){
                    var startTime = carportUsage[i]['startTime'];
                    var endTime = carportUsage[i]['endTime'];
                    if(startTime == undefined || endTime == undefined)
                        continue;
                    var startMilliSec = Date.parse(startTime);
                    startMilliSec = startMilliSec > parsedStartDay ? startMilliSec : parsedStartDay;                    
                    var endTimeMillSec = Date.parse(endTime);
                    endTimeMillSec = endTimeMillSec < parsedEndDay ? endTimeMillSec : parsedEndDay;
                    chartData.push([startMilliSec,null, null ]);
                    chartData.push([startMilliSec,0, 1]);
                    chartData.push([endTimeMillSec,0, 1]);
                    chartData.push([endTimeMillSec,null, null ]);
                }
                chartData.push([parsedEndDay, null, null]);
                $('#carportUsageChart').highcharts({
                     chart: {
                            type: 'arearange',
                            zoomType: 'x'
                        },
                        
                        title: {
                            text: '停车位使用分布情况'
                        },
                    
                        xAxis: {
                            type: 'datetime'
                        },
                        
                        yAxis: {
                              max:1,
                            title: {
                                text: null
                            }
                        },
                    
                        tooltip: {
                            crosshairs: true,
                            shared: true,
                            valueSuffix: ''
                        },
                        
                        legend: {
                            enabled: false
                        },
                        series: [{
                            name: '正在使用',
                            data: chartData
                        }]
                });
            }
        });
    };
	var chartParkPeriodCharge = function(chartposition,catagory,totalMoney,realMoney,title){				
		chartposition.highcharts({
	        chart: {
	            type: 'line'
	        },
	        title: {
	            text: title
	        },
	        subtitle: {
	            text: ''
	        },
	        xAxis: {
	        		categories:catagory
	        	//  categories: ['一月', '二月', '三月','四月','五月','六月','七月','八月','九月','十月','十一月','十二月']
	        },
	        yAxis: {
	            title: {
	                text: '元',
	            },
	       
	        },
	        tooltip: {
	            crosshairs: true,
	            shared: true
	        },
	        plotOptions: {
	            line: {
	                dataLabels: {
	                    enabled: true
	                },
	             //   enableMouseTracking: true
	            }
	        },
	        series: [{
	            name: '应收金额',
	            color:'red',
	            data: totalMoney
	        },{
	            name: '实收金额',
	            color:'blue',
	            data: realMoney
	        }]
	    });
	};
	
	var chartCarportPeriodCharge = function(){
		var title="停车位费用";
		var chartposition = $('#chart-carport-period-charge');
		chartposition.highcharts({
	        chart: {
	            type: 'line'
	        },
	        title: {
	            text: title
	        },
	        subtitle: {
	            text: ''
	        },
	        xAxis: {
	            categories: ['一月', '二月', '三月','四月','五月','六月','七月','八月','九月','十月','十一月','十二月']
	        },
	        yAxis: {
	            title: {
	                text: '万元'
	            }
	        },
	        tooltip: {
	            enabled: false,
	            formatter: function() {
	                return '<b>'+ this.series.name +'</b><br/>'+this.x +': '+ this.y + "万元";
	            }
	        },
	        legend: {
		        enabled: false
		    },
	        plotOptions: {
	            line: {
	                dataLabels: {
	                    enabled: true
	                },
	                enableMouseTracking: false
	            }
	        },
	        series: [{
	            name: '停车位',
	            data: [0.4, 0.9, 0.5, 0.8, 0.4, 0.5, 0.2, 0.5, 0.3, 0.3, 0.9, 0.6]
	        }]
	    });
	};
	
	var chartCarportCharge=function(){
		var title="停车位费用";
		var data=[{
            data: [{color:"#F7F709",y:$.fn.carportUsage.carportTotalMoney}, {color:"#1A1AE6",y:$.fn.carportUsage.carportRealMoney}]
        }]
		var chartposition=$('#chart-content-carport-charge');
		
		renderchartcolumn(title,chartposition,data);
	}
	var chartCarportUsage=function(){
		var title="停车位时段使用率";
		var data=[];
		var chartpostion=$('#chart-content-carport-usage');
//		renderChartArea(title,chartpostion,data);
	};
	
	var renderchart=function(title,chartposition,data){
		chartposition.highcharts({
		        chart: {
		            plotBackgroundColor: null,
		            plotBorderWidth: null,
		            plotShadow: false
		        },
		        title: {
		            text: title
		        },
		        tooltip: {
		    	    pointFormat: '{series.name}: <b>{point.percentage:.1f}%</b>'
		        },
		        legend: {
			        enabled: false
			    },
		        plotOptions: {
		            pie: {
		                allowPointSelect: true,
		                cursor: 'pointer',
		                dataLabels: {
		                    enabled: true,
		                    color: '#000000',
		                    connectorColor: '#000000',
		                    format: '<b>{point.name}</b>: {point.percentage:.1f} %'
		                }
		            }
		        },
		        series: data,
		    });
	}
	var renderchartcolumn=function(title,chartposition,data){
		chartposition.highcharts({
	        chart: {
	            type: 'column'
	        },
	        title: {
	            text: title
	        },	      
	        xAxis: {
	        	  labels: {
	  	        	style: {
	  	        		fontSize:'18px',
	  	        		fontWeight:'bold'
	  	        	},
	  	        },
	            categories: [
	                '应收金额',
	                '实收金额',	      
	            ]
	        },
	        yAxis: {
	            min: 0,
	            title: {
	                text: '元'
	            },      
	        },
	        legend: {
		        enabled: false
		    },
	        plotOptions: {
	            column: {
	                pointPadding: 0.2,
	                borderWidth: 0
	            }
	        },
	        series: data
	    });
	}
	
})(jQuery)