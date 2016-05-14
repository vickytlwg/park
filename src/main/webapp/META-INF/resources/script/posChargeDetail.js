(function($){	
	$.fn.park = {}	
	$.fn.park.initial = function(){
		renderPark(0, $.fn.page.pageSize);
		renderPagination();
		intervalRenderPagination();
	};	
	/*** get table park item ***/
	var renderPark = function(low, count){
		var cols = $('#parkTable').find('thead tr th').length;
		$("#parkBody").html('<tr><td colspan="' + cols + '"></td></tr>');		
		var loader = new $.Loader();
		var loaderTd = $('#parkBody').find('td');
		loaderTd.append(loader.get());
		loader.show();	
		var data = {'low': low, 'count': count, 'T': new Date().getTime()};
		$.ajax({
			//url:$.fn.config.webroot + "/getParkDetail?low=" + low + "&count=" + count + "&_t=" + new Date().getTime(),
			url: $.fn.config.webroot + "/pos/selectPosdataByPage" ,
			type: 'post',
			contentType: 'application/json;charset=utf-8',			
			datatype: 'json',
			data: $.toJSON(data),
			success: function(data){
				loader.remove();
				fillParkTbody(data);
			},
			error: function(data){
				loader.remove();
				errorHandle(data);
			}
		});
	};
	var fillParkTbody = function(data){
		var parkBody = $("#parkBody");
		parkBody.html('');
		data = data["body"];
		for(var i = 0; i < data.length; i++){
			var tr = $('<tr></tr>');
			tr.append('<td><input type="checkbox" /></td>');
			tr.append('<td>' + data[i]['id']+ '</td>');
		//	tr.append('<td>' + data[i]['credencesnr']+ '</td>');
			tr.append('<td>' + data[i]['cardsnr']+ '</td>');
		//	var type=data[i]['cardtype']=="1"?"小车":"大车";
		//	tr.append('<td>' + type + '</td>');
			tr.append('<td>' + data[i]['sitename']+ '</td>');			
			tr.append('<td>' + data[i]['backbyte']+ '</td>');
			var mode=parseInt(data[i]['mode'])==0?"进场":"出场";
			tr.append('<td>' + mode + '</td>');
			tr.append('<td>' + data[i]['userid']+ '</td>');
			tr.append('<td>' + data[i]['possnr']+ '</td>');
			tr.append('<td>' + data[i]['money']+ '</td>');			
			tr.append('<td>' + data[i]['giving']+ '</td>');
			tr.append('<td>' + data[i]['realmoney']+ '</td>');
			tr.append('<td>' + data[i]['returnmoney']+ '</td>');
			tr.append('<td>' + data[i]['starttime']+ '</td>');	
			tr.append('<td>' + data[i]['endtime']+ '</td>');
	//		tr.append('<td>' + data[i]['sysid']+ '</td>');
	//		tr.append('<td>' + data[i]['memo']+ '</td>');			
		
			if( i % 2 == 0){
				tr.addClass('success');
			}else{
				tr.addClass('active');
			}			
			parkBody.append(tr);
		}
	};
	var errorHandle = function(data){
		var modal = new $.Modal('errorHandle', "失败", "操作失败" + data['message']);
		$('#showErrorMessage').html(modal.get());
		modal.show();
	};
	/***render pagination****/
	 $.fn.renderPagination = function(){		
		$.ajax({
			url:$.fn.config.webroot + "/pos/getPosdataCount",
			type: 'get',
			success: function(data){
				
				var paginationUl = $.fn.page.initial(data['count'], pageClickFunc);
				$('#pagination').append(paginationUl);
			},
			error: function(data){		
			}
		});
	};
	function renderPagination(){
		$.ajax({
			url:$.fn.config.webroot + "/pos/getPosdataCount",
			type: 'get',
			success: function(data){				
				var paginationUl = $.fn.page.initial(data['count'], pageClickFunc);
				$('#pagination').append(paginationUl);
			},
			error: function(data){
			}
		});
	}
	var intervalRenderPagination=function(){
		setInterval("$.fn.renderPagination()",5000);
	}
	var pageClickFunc = function(index){
		renderPark($.fn.page.pageSize * ($.fn.page.currentPage - 1), $.fn.page.pageSize);
	};	
})(jQuery);