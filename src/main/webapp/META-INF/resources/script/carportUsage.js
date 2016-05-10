(function($){
	$.fn.carportUsage={};
	$.fn.carportUsage.initial=function(){
		dateInitial();
		chartCarport();
		chartPark();
		chartParkCharge();
		chartParkPeriodCharge();
		chartCarportCharge();
		chartCarportUsage();	
		chartCarportPeriodCharge();
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
	}
	var chartCarport=function(){
		var title="停车位使用率";
		var data=[{
            type: 'pie',
            name: '使用率',
            data: [
                {name:'空闲时间',   y:70,color:"#778588"},
                {
                    name: '停车时间',
                    y: 30,
                    color:"#F73809",
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
                {name:'整体空闲', y:55,color:"#22DD6D"},
                {
                    name: '整体使用',
                    color:"#CC3370",
                    y: 45,
                    sliced: true,
                    selected: true
                },
            ]
        }]
		var chartposition=$('#chart-content-park');
		renderchart(title,chartposition,data);
	}
	var chartParkCharge=function(){
		var title="停车场费用";
		var data=[{
            name: "停车场费用",
            data: [{color:"#DDDF00",y:842},{ color:"#3D11EE",y:735}]
        }]
		var chartposition=$('#chart-content-park-charge');
		renderchartcolumn(title,chartposition,data);
	}
	
	var chartParkPeriodCharge = function(){
		var title="停车场费用";
		var chartposition = $('#chart-park-period-charge');
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
	        plotOptions: {
	            line: {
	                dataLabels: {
	                    enabled: true
	                },
	                enableMouseTracking: false
	            }
	        },
	        series: [{
	            name: '停车场',
	            data: [7.0, 6.9, 9.5, 8.5, 10.4, 11.5, 6.2, 7.5, 11.3, 10.3, 9.9, 7.6]
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
            name: "停车位费用",
            data: [{color:"#FF9655",y:89}, {color:"#F73809",y:71.5}]
        }]
		var chartposition=$('#chart-content-carport-charge');
		
		renderchartcolumn(title,chartposition,data);
	}
	var chartCarportUsage=function(){
		var title="停车位时段使用率";
		var data=[];
		var chartpostion=$('#chart-content-carport-usage');
		renderChartArea(title,chartpostion,data);
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
	        colors:[
	                'red',
	                'yellow'
	                ],
	        xAxis: {
	            categories: [
	                '应收金额',
	                '实收金额',	      
	            ]
	        },
	        yAxis: {
	            min: 0,
	            title: {
	                text: '元'
	            }
	        },
	        tooltip: {
	            headerFormat: '<span style="font-size:10px">{point.key}</span><table>',
	            pointFormat: '<tr><td style="color:{series.color};padding:0">{series.name}: </td>' +
	                '<td style="padding:0"><b>{point.y:.1f} </b></td></tr>',
	            footerFormat: '</table>',
	            shared: true,
	            useHTML: true
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
	

	var renderChartArea=function(title,chartposition,data){
		chartposition.highcharts({
	        chart: {
	            zoomType: 'x',
	            spacingRight: 20
	        },
	        title: {
	            text: title
	        },
	       
	        xAxis: {
	            type: 'datetime',
	            maxZoom: 14 * 24 * 3600000, // fourteen days
	            title: {
	                text: null
	            }
	        },
	        yAxis: {
	            title: {
	                text: 'Exchange rate'
	            }
	        },
	        tooltip: {
	            shared: true
	        },
	        legend: {
	            enabled: false
	        },
	        plotOptions: {
	            area: {
	                fillColor: {
	                    linearGradient: { x1: 0, y1: 0, x2: 0, y2: 1},
	                    stops: [
	                        [0, Highcharts.getOptions().colors[0]],
	                        [1, Highcharts.Color(Highcharts.getOptions().colors[0]).setOpacity(0).get('rgba')]
	                    ]
	                },
	                lineWidth: 1,
	                marker: {
	                    enabled: false
	                },
	                shadow: false,
	                states: {
	                    hover: {
	                        lineWidth: 1
	                    }
	                },
	                threshold: null
	            }
	        },

	        series: [{
	            type: 'area',
	            name: 'USD to EUR',
	            pointInterval: 24 * 3600 * 1000,
	            pointStart: Date.UTC(2006, 0, 01),
	            data: [
	                
	               
	                0.7878, 0.7868, 0.7883, 0.7893, 0.7892, 0.7876, 0.785, 0.787, 0.7873, 0.7901,
	                0.7936, 0.7939, 0.7938, 0.7956, 0.7975, 0.7978, 0.7972, 0.7995, 0.7995, 0.7994,
	                0.7976, 0.7977, 0.796, 0.7922, 0.7928, 0.7929, 0.7948, 0.797, 0.7953, 0.7907,
	                0.7872, 0.7852, 0.7852, 0.786, 0.7862, 0.7836, 0.7837, 0.784, 0.7867, 0.7867,
	                0.7869, 0.7837, 0.7827, 0.7825, 0.7779, 0.7791, 0.779, 0.7787, 0.78, 0.7807,
	                0.7803, 0.7817, 0.7799, 0.7799, 0.7795, 0.7801, 0.7765, 0.7725, 0.7683, 0.7641,
	                0.7639, 0.7616, 0.7608, 0.759, 0.7582, 0.7539, 0.75, 0.75, 0.7507, 0.7505,
	                0.7516, 0.7522, 0.7531, 0.7577, 0.7577, 0.7582, 0.755, 0.7542, 0.7576, 0.7616,
	                0.7648, 0.7648, 0.7641, 0.7614, 0.757, 0.7587, 0.7588, 0.762, 0.762, 0.7617,
	                0.7618, 0.7615, 0.7612, 0.7596, 0.758, 0.758, 0.758, 0.7547, 0.7549, 0.7613,
	                0.7655, 0.7693, 0.7694, 0.7688, 0.7678, 0.7708, 0.7727, 0.7749, 0.7741, 0.7741,
	                0.7732, 0.7727, 0.7737, 0.7724, 0.7712, 0.772, 0.7721, 0.7717, 0.7704, 0.769,
	                0.7711, 0.774, 0.7745, 0.7745, 0.774, 0.7716, 0.7713, 0.7678, 0.7688, 0.7718,
	                0.7718, 0.7728, 0.7729, 0.7698, 0.7685, 0.7681, 0.769, 0.769, 0.7698, 0.7699,
	                0.7651, 0.7613, 0.7616, 0.7614, 0.7614, 0.7607, 0.7602, 0.7611, 0.7622, 0.7615,
	                0.7598, 0.7598, 0.7592, 0.7573, 0.7566, 0.7567, 0.7591, 0.7582, 0.7585, 0.7613,
	                0.7631, 0.7615, 0.76, 0.7613, 0.7627, 0.7627, 0.7608, 0.7583, 0.7575, 0.7562,
	                0.752, 0.7512, 0.7512, 0.7517, 0.752, 0.7511, 0.748, 0.7509, 0.7531, 0.7531,
	                0.7527, 0.7498, 0.7493, 0.7504, 0.75, 0.7491, 0.7491, 0.7485, 0.7484, 0.7492,
	                0.7471, 0.7459, 0.7477, 0.7477, 0.7483, 0.7458, 0.7448, 0.743, 0.7399, 0.7395,
	                0.7395, 0.7378, 0.7382, 0.7362, 0.7355, 0.7348, 0.7361, 0.7361, 0.7365, 0.7362,
	               	             
	             
	            ]
	        }]
	    });
	}
})(jQuery)