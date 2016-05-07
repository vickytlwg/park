(function($){
	$.fn.carportUsage={};
	$.fn.carportUsage.initial=function(){
		dateInitial();
		chartCarport();
		chartPark();
	};
	
	var dateInitial=function(){
		$('#date').val(new Date().format('yyyy-MM-dd'));
		$('#date').datepicker({
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
	}
	var chartCarport=function(){
		var title="停车位使用率";
		var data=[{
            type: 'pie',
            name: '使用率',
            data: [
                ['空闲时间',   70],
                {
                    name: '停车时间',
                    y: 30,
                    sliced: true,
                    selected: true
                },
            ]
        }]
		var chartposition=$('#chart-content-carport')
		renderchart(title,chartposition,data);
	}
	var chartPark=function(){
		var title="停车场使用率";
		var data=[{
            type: 'pie',
            name: '使用率',
            data: [
                ['整体空闲',   55],
                {
                    name: '整体使用',
                    y: 45,
                    sliced: true,
                    selected: true
                },
            ]
        }]
		var chartposition=$('#chart-content-park');
		renderchart(title,chartposition,data);
	}
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
})(jQuery)