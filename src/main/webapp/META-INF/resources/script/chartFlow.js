(function($){	
$.fn.parkChart = {};
$.fn.parkChart.theme = Highcharts.getOptions();
$.fn.parkChart.chart;
$.fn.parkChart.initial = function(){
	var chatContent = $('#chart-content');	
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
		
	Highcharts.setOptions(Highcharts.themeGray);
	$.fn.parkChart.renderChart(chatContent);

	$('#parkName').text($('#park-select').find('option:selected').text());
	$('#park-select').on('change', $(this), function(){
		$('#parkName').text($('#park-select').find('option:selected').text());
		$.fn.parkChart.renderChart(chatContent);
	});
	$('#date').on('change', $(this), function(){
		$.fn.parkChart.renderChart(chatContent);
	});
	
	$('#theme').on('change', $(this), function(){
		
		var value = $(this).val();
		var options;
		switch(parseInt(value)){
		case 0:
			options =  Highcharts.setOptions($.fn.parkChart.theme);
			break;
		case 1:
			options = Highcharts.setOptions(Highcharts.themeGray);
			break;
		case 2:
			options = Highcharts.setOptions(Highcharts.themeDarkBlue);
			break;
		case 3:
			options = Highcharts.setOptions(Highcharts.themeDarkGreen);
			break;
		case 4:
			options = Highcharts.setOptions(Highcharts.themeDarkUnica);
			break;
		case 5:
			options = Highcharts.setOptions(Highcharts.themeGrid);
			break;
			default:
				Highcharts.setOptions($.fn.parkChart.theme);
		}	
		$.fn.parkChart.renderChart(chatContent);
	})
};

$.fn.parkChart.renderChart = function(chatContent){
	var parkId = parseInt($('#park-select').val());
	var date = $('#date').val();
	
	var url = $.fn.config.webroot + '/getHourCountByChannel?_t=' + (new Date()).getTime();
		
	$.ajax({
		url:url,
		type: 'post',
		contentType: 'application/json;charset=utf-8',			
		datatype: 'json',
		data: $.toJSON({'parkId':parkId, 'date':date}),
		success: function(data){
			if(data.status == 1001){
			$.fn.parkChart.renderChartContent(data.body, chatContent);
		}
		},
		error: function(data){}
	});
};

var parseAccessData = function(data, isMonthAccess){
	var dateTime = [];
	var series=[];
	var i=0;
	for(var k=0;k<24;k++){
		dateTime.push(k + ":00");
	}
	for(var key in data){		
		var accessFlow = [];
		for(var j = 0; j < 24; j++){
			var flowValue = data[key][j] || 0;					
			accessFlow.push(flowValue);
		}		
		if(accessFlow.length == 0){
			for(var i = 0; i < 24; i++){
				accessFlow.push(0);
			}
		}
		series[i]={ 
				type:'bar',
				name:key,
				data:accessFlow,
		};
		i++;
	}
	return {'dateTime': dateTime, 'series': series};
};
$.fn.parkChart.renderChartContent = function(data, chatContent){
	var chartData = parseAccessData(data);	
	chatContent.highcharts({
		chart: {
			 
			 events: {  
                 load: function() {  
                	 $.fn.parkChart.chart = this;
                	 $.fn.parkChart.updateChart();
                	 setInterval(function(){
                		 $.fn.parkChart.updateChart();
                	 }, 1000 * 10);
                 }                                                               
             }   
		},
	    title: {
	        text: '硬件流量统计'
	    },
	    xAxis: {
	        categories: chartData['dateTime']
	    },
	    series:chartData['series']
	});
};

})(jQuery);