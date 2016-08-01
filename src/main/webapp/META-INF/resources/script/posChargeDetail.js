(function($){	
	$.fn.park = {};	
	parkId=-1;
	$.fn.park.initial = function(){
		renderPark(-1,0, $.fn.page.pageSize);
		renderPagination();
		intervalRenderPagination();
		rendnumcard();
		bindParkChange();
	};	
	
	var rendnumcard=function(){
		$.ajax({
			url:$.fn.config.webroot + "/pos/getCountsByCard" ,
			type:'get',
			datatype:'json',
			success:function(data){
				if (data['status']==1001) {
					data=data['body'];
					var strp='';
				    for(var i=0;i<data.length;i++){
				        strp+="车牌号: "+data[i]['CardSnr']+"  次数:"+data[i]['num']+"    ";
				    }	
				    $('#carnumshow').text(strp);
				}
			}
		});
	};
	
	var bindParkChange=function(){
		$('#parkNamesearch').on('change',function(){
		 parkId=$('#parkNamesearch').val();
		renderPark(parkId,0, $.fn.page.pageSize);
        renderPagination();
		});
	};
	/*** get table park item ***/
	var renderPark = function(parkId,low, count){
		var cols = $('#parkTable').find('thead tr th').length;
		$("#parkBody").html('<tr><td colspan="' + cols + '"></td></tr>');		
		var loader = new $.Loader();
		var loaderTd = $('#parkBody').find('td');
		loaderTd.append(loader.get());
		loader.show();	
		var url="/pos/selectPosdataByPage";
		var data = {'low': low, 'count': count, 'T': new Date().getTime()};
		if(parkId!=-1){
		    url="/pos/selectPosdataByPageAndPark";
		    data = {'parkId':parseInt(parkId),'low': low, 'count': count, 'T': new Date().getTime()};
		}
		$.ajax({
			//url:$.fn.config.webroot + "/getParkDetail?low=" + low + "&count=" + count + "&_t=" + new Date().getTime(),
			url: $.fn.config.webroot + url ,
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
	var renderParkByPark=function(parkId,low,count){
	    var cols = $('#parkTable').find('thead tr th').length;
        $("#parkBody").html('<tr><td colspan="' + cols + '"></td></tr>');       
        var loader = new $.Loader();
        var loaderTd = $('#parkBody').find('td');
        loaderTd.append(loader.get());
        loader.show();  
        var data = {'parkId':parkId,'low': low, 'count': count, 'T': new Date().getTime()};
        $.ajax({
            //url:$.fn.config.webroot + "/getParkDetail?low=" + low + "&count=" + count + "&_t=" + new Date().getTime(),
            url: $.fn.config.webroot + "/pos/selectPosdataByPageAndPark" ,
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
			var endtime=data[i]['endtime']
			if (data[i]['endtime']==undefined) {
			    endtime='';
			};
			tr.append('<td>' + endtime+ '</td>');
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
	
	function renderPagination(){
	   var url='/pos/getPosdataCount';
         if(parkId!=-1)
         {
             url='/pos/getPosdataCountByPark/'+parkId;
         }  
		$.ajax({
			url:$.fn.config.webroot + url,
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
		setInterval(renderPagination,5000);
	};
	var pageClickFunc = function(index){
		renderPark(parkId,$.fn.page.pageSize * ($.fn.page.currentPage - 1), $.fn.page.pageSize);
	};	
})(jQuery);