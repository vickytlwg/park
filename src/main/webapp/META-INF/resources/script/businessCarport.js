(function($){
	
	$.fn.businessCarport = {};
	
	$.fn.businessCarport.initial = function(){
		
		bindTrClick();
		bindRefreshClick();
		bindAddBtnClick();
		bindUpdateBtnClick();
		bindSubmitBusinessCarportBtnClick();
		bindDeleteBtnClick();
		bindSeacherBtnClick();
		//renderBusinessCarport(0, $.fn.page.pageSize);					
		fillSearchPark();
		bindSearchParkChange();
	};
	
	/**bind tr click*/
	var bindTrClick = function(){
		var businessCarportBody = $("#businessCarportBody");
		businessCarportBody.on('click', 'tr', function(event){
			if(event.target.nodeName.toLowerCase() != 'input')
				$(this).find('input[type="checkbox"]').click();
		});
	};
	
	var readCookieSetSelect=function(){
		if($.cookie('selectValue')){
			var aa=$.cookie('selectValue');
			$('select#searchPark').val($.cookie('selectValue'));
			if($('select#searchPark').find("option:selected").text().length<3){
				$('select#searchPark option:last').attr('selected','selected');
			}
		}
		
	}
	
	/**bind refresh click***/
	var bindRefreshClick = function(){
		var refreshBtn = $('#refresh');
		refreshBtn.on('click', $(this), function(){
			renderBusinessCarport($.fn.page.pageSize * ($.fn.page.currentPage - 1), $.fn.page.pageSize);
			$(this).blur();
		});
		
	};
	
	/** bind search park change event **/
	var bindSearchParkChange = function(){
		$('select#searchPark').on('change', $(this), function(){
			var aa=$(this).val();
			if(aa!=-1){
				$.cookie('selectValue',$(this).val(),{path:'/',expires:10});
			}
			renderBusinessCarport(0, $.fn.page.pageSize);
		});
	};
	
	/** fill search park **/
	var fillSearchPark = function(){
		var successFunc = function(data){
			data = data['body'];
			var parkNameSelect = $('select#searchPark');
			
			for(var i = 0; i < data.length; i++){
				if(data[i]['type']==3)
				{
				parkNameSelect.append($('<option value = ' + data[i]['id'] + '>' + data[i]['name'] +'</option>'));
				}
			}
			if(data.length > 0)
				parkNameSelect.change();
			readCookieSetSelect();
		};
		var errorFunc = function(data){
		};
		$.ajax({
			url:$.fn.config.webroot + '/getParks?_t=' + (new Date()).getTime(),
			type: 'get',
			contentType: 'application/json;charset=utf-8',			
			success: function(data){successFunc(data);},
			error: function(data){errorFunc(data);}
		});
	};
	
	/**bind add BusinessCarport click**/
	var bindAddBtnClick = function(){
		$('#addBusinessCarport').on('click', $(this), function(){
			addBtnClickHandle();
		});
	};
	
	var addBtnClickHandle = function(){
		$('#addBusinessCarportForm')[0].reset();
		$('select#businessCarportStatus').removeAttr('disabled');
		$('select#parkName').removeAttr('BusinessCarportId');
		$('select#parkName').removeAttr('disabled');
		$('select#parkName').html('');
		$('#addBusinessCarportResult').html('');	
		$('#addBusinessCarportModal').modal('show');
		fillParkName();
		fillMacInfo();
	};
	
	
	var fillParkName = function(){
		$.fn.loader.appendLoader($('#parkNameLoader'));
		var successFunc = function(data){
			$.fn.loader.removeLoader($('#parkNameLoader'));
			data = data['body'];
			var parkNameSelect = $('select#parkName');
			parkNameSelect.html('');
			for(var i = 0; i < data.length; i++){
			
					parkNameSelect.append($('<option value = ' + data[i]['id'] + '>' + data[i]['name'] +'</option>'));
					
				
			}
			if($('#searchPark').val() != -1)
				parkNameSelect.val($('#searchPark').val());
		};
		var errorFunc = function(data){
			$.fn.loader.removeLoader('#parkNameLoader');
			$('#parkNameLoader').append('<span>获取停车场名称失败，请稍后重试</span>');
		};
		$.ajax({
			url:$.fn.config.webroot + '/getParks?_t=' + (new Date()).getTime(),
			type: 'get',
			contentType: 'application/json;charset=utf-8',			
			success: function(data){successFunc(data);},
			error: function(data){errorFunc(data);}
		});
	};
	
	var fillMacInfo = function(additionalOption){
		$.fn.loader.appendLoader($('#macIdLoader'));
		var successFunc = function(data){
			$.fn.loader.removeLoader($('#macIdLoader'));
			data = data['body'];
			var macIdSelect = $('select#macId');
			macIdSelect.html('');
			for(var i = 0; i < data.length; i++){
				macIdSelect.append($('<option value = ' + data[i]['id'] + '>' + data[i]['mac'] +'</option>'));
			}
			if(additionalOption != undefined){
				for(var i = 0; i < additionalOption.length; i++){
					macIdSelect.append(additionalOption[i]);
				}
			}
		};
		var errorFunc = function(data){
			$.fn.loader.removeLoader('#macIdLoader');
			$('#macIdLoader').append('<span>获取mac信息失败，请稍后重试</span>');
		};
		$.ajax({
			url:$.fn.config.webroot + '/getUnBoundHardwares/0?_t=' + (new Date()).getTime(),
			type: 'get',
			contentType: 'application/json;charset=utf-8',
			success: function(data){successFunc(data);},
			error: function(data){errorFunc(data);}
		});
	};
	
	/**bind update BusinessCarport click **/
	var bindUpdateBtnClick = function(){
		$('#updateBusinessCarport').on('click', $(this), function(){
			updateBtnClickHandle();
		});
	};
	
	var updateBtnClickHandle = function(){
		$('#addBusinessCarportForm')[0].reset();
		$('#addBusinessCarportResult').html('');
		var checkedTr = $('#businessCarportBody').find('input[type="checkbox"]:checked').parents('tr');
		if(checkedTr.length == 0){
			alert('请选择一个列表项');
			return;
		}else
			assignAddBusinessCarportForm($(checkedTr[0]));
			$('#businessCarportStatus').attr('disabled', 'disabled');
			$('#addBusinessCarportModal').modal('show');
	};
	
	var assignAddBusinessCarportForm = function(checkedTr){
		
		var tds = checkedTr.find('td');
		$('select#parkName').attr('businessCarportId', parseInt($(tds[1]).text()));
		$('select#parkName').html('');
		$('select#parkName').append($('<option value=-1>' + $(tds[2]).text() + '</option>'));
		$('select#parkName').attr('disabled', 'true');
		$('input#businessCarportNumber').val(parseInt($(tds[3]).text()));
		var additionalOption = [];
		additionalOption.push($('<option value=' + parseInt($(tds[4]).attr('data')) + ' selected>' + $(tds[4]).text() + '</option>'));
		fillMacInfo(additionalOption);
		
		$('select#businessCarportStatus').val(parseInt($(tds[5]).attr('data')));
		$('input#businessCarportFloor').val(parseInt($(tds[6]).text()));
		$('input#businessCarportPosition').val($(tds[7]).text());
		$('input#businessCarportDesc').val($(tds[8]).text());	
			
	};
	
	/**bind submit button of adding and updating BusinessCarport */
	var bindSubmitBusinessCarportBtnClick = function(){
		
		$('#submitBusinessCarportBtn').on('click', $(this), function(){		
			var url = '';
			var BusinessCarportFields = getAddBusinessCarportFormValue();
			if(BusinessCarportFields['id'] != undefined && BusinessCarportFields['id'] != null ){
				url = $.fn.config.webroot + '/update/businessCarport';
			}else{
				url = $.fn.config.webroot + '/insert/businessCarport';
			}
			
			var addBusinessCarportResultDiv = $('#addBusinessCarportResult');
			$.fn.loader.appendLoader(addBusinessCarportResultDiv);
			
			$.ajax({
				url:url,
				type: 'post',
				contentType: 'application/json;charset=utf-8',			
				datatype: 'json',
				data: $.toJSON(BusinessCarportFields),
				success: function(data){
					if(data['status'] = 1001){
						$.fn.loader.removeLoader(addBusinessCarportResultDiv);
						addBusinessCarportResultDiv.append($.fn.tip.success('提交操作完成'));
						setTimeout('$("#addBusinessCarportResult").html(""); $("#addBusinessCarportModal").modal("hide");$("#refresh").click()', 500);
					}
				},
				error: function(data){
					$.fn.loader.removeLoader(addBusinessCarportResultDiv);
					addBusinessCarportResultDiv.append($.fn.tip.error('提交操作未完成'));
					setTimeout('$("#addBusinessCarportResult").html("");', 3000);
				}
			});
		});
	};
	
	var getAddBusinessCarportFormValue = function(){
		var BusinessCarportFields = {};
		var BusinessCarportId = $('select#parkName').attr('businessCarportId');
		if( BusinessCarportId != undefined && BusinessCarportId != null){
			BusinessCarportFields['id'] = parseInt(BusinessCarportId);
		}
			
		BusinessCarportFields['parkId'] = parseInt($('select#parkName').val());
		BusinessCarportFields['carportNumber'] = parseInt($('input#businessCarportNumber').val());
		BusinessCarportFields['macId'] = parseInt($('select#macId').val());
		BusinessCarportFields['status'] = parseFloat($('select#businessCarportStatus').val());
		BusinessCarportFields['floor'] = parseInt($('input#businessCarportFloor').val());
		BusinessCarportFields['position'] = $('input#businessCarportPosition').val();
		BusinessCarportFields['description'] = $('input#businessCarportDesc').val();
		BusinessCarportFields['date'] = (new Date()).format('yyyy-MM-dd hh:mm:ss');
		return BusinessCarportFields;

	};
	
	/**bind delete BusinessCarport click **/
	var bindDeleteBtnClick = function(){
		$('#deleteBusinessCarport').on('click', $(this), function(){
			var checkedTr = $('#businessCarportBody').find('input[type="checkbox"]:checked').parents('tr');
			
			if(checkedTr > 1)
				return;
			var modal = new $.Modal("businessCarportDelete", "删除停车位", "是否删除停车位!");
			$('#showMessage').html(modal.get());
			modal.show();
			var callback = deleteClickHandle;
			modal.setSubmitClickHandle(callback);
		});
	};
	
	var deleteClickHandle = function(){
		var checkedTr = $('#businessCarportBody').find('input[type="checkbox"]:checked').parents('tr');
		var id = parseInt($(checkedTr.find('td')[1]).text());
		$.ajax({
			url:$.fn.config.webroot + '/delete/businessCarport/' + id,
			type: 'get',
			contentType: 'application/json;charset=utf-8',
			success: function(data){
				$('#refresh').click();
			},
			error: function(data){
				errorHandle(data);
			}
		});
	};
	
	/**bind search BusinessCarport click **/
	var bindSeacherBtnClick = function(){
		
	};
	
	/*** get table businessCarport item ***/
	var renderBusinessCarport = function(low, count){
		renderPagination();
		var cols = $('#businessCarportTable').find('thead tr th').length;
		$("#businessCarportBody").html('<tr><td colspan="' + cols + '"></td></tr>');
		$.fn.loader.appendLoader($('#businessCarportBody').find('td'));
		var parkId=-1;
		if($.cookie('selectValue')){
			parkId = $.cookie('selectValue');
		};
		$.ajax({
			url:$.fn.config.webroot + "/getBusinessCarportDetail?low=" + low + "&count=" + count +  "&parkId=" + parkId +"&_t=" + (new Date()).getTime(),
			type: 'get',
			success: function(data){
				fillBusinessCarportTbody(data);
			},
			error: function(data){
				errorHandle(data);
			}
		});
	};
	
	
	var fillBusinessCarportTbody = function(data){
		var businessCarportBody = $("#businessCarportBody");
		$.fn.loader.removeLoader($('#businessCarportDiv'));
		businessCarportBody.html('');
		var wuche="<button type='button' class='btn btn-success'>无车</button>";
		var youche="<button type='button' class='btn btn-danger'>有车</button>";
		data = data["body"];
		for(var i = 0; i < data.length; i++){
			var tr = $("<tr></tr>");
			tr.append('<td><input type="checkbox" /></td>');
			tr.append('<td>' + data[i]['id']+ '</td>');
			tr.append('<td>' + data[i]['parkName']+ '</td>');
			tr.append('<td>' + data[i]['carportNumber']+ '</td>');
			tr.append('<td data=' + data[i]['macId'] + '>' + data[i]['mac']+ '</td>');
			tr.append('<td data=' + data[i]['status'] + '>' + (data[i]['status'] == 0 ? wuche:youche)+ '</td>');
			tr.append('<td>' + data[i]['floor']+ '</td>');
		//	tr.append('<td>' + data[i]['position']+ '</td>');
			
			var desc ='';
			if(data[i]['description'] != undefined)
				desc = data[i]['description'];
			tr.append('<td>' + desc + '</td>');
			
			tr.append('<td>' + data[i]['date']+ '</td>');
			if( i % 2 == 0){
				tr.addClass('success');
			}else{
				tr.addClass('active');
			}
			businessCarportBody.append(tr);
		}
	};
	
	var errorHandle = function(data){
		var modal = new $.Modal('errorHandle', "失败", "操作失败" + data['message']);
		$('#showErrorMessage').html(modal.get());
		modal.show();
	};
	
	/***render pagination****/
	var renderPagination = function(){
		var parkId = $('select#searchPark').val();
		$.ajax({
			url:$.fn.config.webroot + "/getBusinessCarportCount?parkId=" + parkId,
			type: 'get',
			success: function(data){
				data = data["body"];
				var paginationUl = $.fn.page.initial(data['count'], pageClickFunc);
				$('#pagination').append(paginationUl);
			},
			error: function(data){
				
			}
		});
	};
	
	var pageClickFunc = function(index){
		renderBusinessCarport($.fn.page.pageSize * ($.fn.page.currentPage - 1), $.fn.page.pageSize);
	};
	
})(jQuery);