(function($){
	
	$.fn.access = {};
	$.fn.access.initial = function(){
		bindTrClick();
		bindRefreshClick();
		renderAccess(0, $.fn.page.pageSize);
		renderPagination();
	};
	
	/**bind tr click*/
	var bindTrClick = function(){
		var accessBody = $("#access_body");
		accessBody.on('click', 'tr', function(event){
			if(event.target.nodeName.toLowerCase() != 'input')
				$(this).find('input[type="checkbox"]').click();
		});
	};
	
	var bindRefreshClick = function(){
		var refreshBtn = $('#refresh');
		refreshBtn.on('click', $(this), function(){
			renderAccess($.fn.page.pageSize * ($.fn.page.currentPage - 1), $.fn.page.pageSize);
			$(this).blur();
		});
		
	};
	
	/*** get table access item ***/
	var renderAccess = function(low, count){
		
		var cols = $('#accessTable').find('thead tr th').length;
		$("#access_body").html('<tr><td colspan="' + cols + '"></td></tr>');
		$.fn.loader.appendLoader($('#access_body').find('td'));

		var accessBody = $("#access_body");
		$.ajax({
			url:$.fn.config.webroot + "/getAccessDetail?low=" + low + "&count=" + count,
			type: 'get',
			success: function(data){
				fillAccessTbody(data);
			},
			error: function(data){
				errorHandle(data);
			}
		});
	};
	
	
	var fillAccessTbody = function(data){
		var accessBody = $("#access_body");
		$.fn.loader.removeLoader($('#accessDiv'));
		accessBody.html('');
		data = $.parseJSON(data["body"]);
		for(var i = 0; i < data.length; i++){
			var tr = $("<tr></tr>");
			tr.append('<td><input type="checkbox" /></td>');
			tr.append('<td>' + data[i]['id']+ '</td>');
			tr.append('<td>' + data[i]['name']+ '</td>');
			tr.append('<td>' + data[i]['channelNumber']+ '</td>');
			tr.append('<td>' + data[i]['date']+ '</td>');
			if( i % 2 == 0){
				tr.addClass('success');
			}else{
				tr.addClass('active');
			}
			accessBody.append(tr);
		}
	};
	
	var errorHandle = function(data){
		alert("get accesses failed");
	};
	
	/***render pagination****/
	var renderPagination = function(){

		$.ajax({
			url:$.fn.config.webroot + "/getAccessCount",
			type: 'get',
			success: function(data){
				data = $.parseJSON(data["body"]);
				var paginationUl = $.fn.page.initial(data['count'], pageClickFunc);
				$('#pagination').append(paginationUl);
			},
			error: function(data){
				
			}
		});
	};
	
	var pageClickFunc = function(index){
		renderAccess($.fn.page.pageSize * ($.fn.page.currentPage - 1), $.fn.page.pageSize);
	};
	
})(jQuery);