(function($){
	
	$.fn.mac = {};
	
	$.fn.mac.initial = function(){
		bindTrClick();
		bindRefreshClick();
		bindAddBtnClick();
		bindUpdateBtnClick();
		bindSubmitMacBtnClick();
		bindSearchSubmitMacBtnClick();
		bindDeleteBtnClick();
		bindSeacherBtnClick();
		renderMac(0, $.fn.page.pageSize);
		renderPagination();
	};
	
	/**bind tr click*/
	var bindTrClick = function(){
		var macBody = $("#macBody");
		macBody.on('click', 'tr', function(event){
			if(event.target.nodeName.toLowerCase() != 'input')
				$(this).find('input[type="checkbox"]').click();
		});
	};
	
	/**bind refresh click***/
	var bindRefreshClick = function(){
		var refreshBtn = $('#refresh');
		refreshBtn.on('click', $(this), function(){
			renderMac($.fn.page.pageSize * ($.fn.page.currentPage - 1), $.fn.page.pageSize);
			$(this).blur();
		});
		
	};
	
	/**bind add Mac click**/
	var bindAddBtnClick = function(){
		$('#addMac').on('click', $(this), function(){
			addBtnClickHandle();
		});
	};
	
	var addBtnClickHandle = function(){	
		$('#addMacForm')[0].reset();
		$('#addMacResult').html('');	
		$('#addMacModal').modal('show');
	};
	
	/**bind update Mac click **/
	var bindUpdateBtnClick = function(){
		$('#updateMac').on('click', $(this), function(){
			updateBtnClickHandle();
		});
	};
	
	var updateBtnClickHandle = function(){
		$('#addMacForm')[0].reset();
		$('#addMacResult').html('');
		var checkedTr = $('#macBody').find('input[type="checkbox"]:checked').parents('tr');
		if(checkedTr.length == 0){
			alert('请选择一个列表项');
			return;
		}else
			assignAddMacForm($(checkedTr[0]));
		$('#addMacModal').modal('show');
	};
	
	var assignAddMacForm = function(checkedTr){
		
		var tds = checkedTr.find('td');
		$('input#mac').val($(tds[2]).text());
		$('input#mac').attr('macId', (parseInt($(tds[1]).text())));
		$('select#macType').val(parseInt($(tds[3]).attr('data')));
		$('select#macStatus').val(parseInt($(tds[4]).attr('data')));
		$('input#macDesc').val($(tds[5]).text());	
			
	};
	
	/**bind submit button of adding and updating Mac */
	var bindSubmitMacBtnClick = function(){
		
		$('#submitMacBtn').on('click', $(this), function(){		
			var url = '';
			var MacFields = getAddMacFormValue();
			if(MacFields['id'] != undefined && MacFields['id'] != null ){
				url = $.fn.config.webroot + '/update/hardware';
			}else{
				url = $.fn.config.webroot + '/insert/hardware';
			}
			
			var addMacResultDiv = $('#addMacResult');
			
			$.fn.loader.appendLoader(addMacResultDiv);
			
			$.ajax({
				url:url,
				type: 'post',
				contentType: 'application/json;charset=utf-8',			
				datatype: 'json',
				data: $.toJSON(MacFields),
				success: function(data){
					if(data['status'] = 1001){
						$.fn.loader.removeLoader(addMacResultDiv);
						addMacResultDiv.append($.fn.tip.success('提交操作完成'));
						setTimeout('$("#addMacResult").html(""); $("#addMacModal").modal("hide");$("#refresh").click()', 500);
					}
				},
				error: function(data){
					$.fn.loader.removeLoader(addMacResultDiv);
					addMacResultDiv.append($.fn.tip.error('提交操作未完成'));
					setTimeout('$("#addMacResult").html("");', 3000);
				}
			});
		});
	};
	
var bindSearchSubmitMacBtnClick = function(){
		
		$('#submitSearchMacBtn').on('click', $(this), function(){	
			
			var url = '';
			var MacFields = getSearchMacFormValue();
			
			url = $.fn.config.webroot + '/search/hardware';
			//var addMacResultDiv = $('#addMacResult');
			var data1='';
			data1="mac="+$('input#macsearch').val();
			//$.fn.loader.appendLoader(addMacResultDiv);
			
			$.ajax({
				url:url,
				type: 'post',
				contentType: 'application/x-www-form-urlencoded;charset=utf-8',			
				datatype: 'json',
				data: data1,
				success: function(data){
					//alert("success!!");
				//	alert(JSON.stringify(data));
					if(data['state'] == 1001){
						//$.fn.loader.removeLoader(addMacResultDiv);
						//addMacResultDiv.append($.fn.tip.success('提交操作完成'));
				//		alert("success!!");
						fillMacTbody(data);
						
					}
				},
				error: function(data){
					alert("fail!!");
					$.fn.loader.removeLoader(addMacResultDiv);
					addMacResultDiv.append($.fn.tip.error('操作未完成'));
					setTimeout('$("#addMacResult").html("");', 3000);
				}
			});
		});
	};
	var getAddMacFormValue = function(){
		var MacFields = {};
		var MacId = $('input#mac').attr('macId');
		if( MacId != undefined && MacId != null){
			MacFields['id'] = parseInt(MacId);
		}
			
		MacFields['mac'] = $('input#mac').val();
		MacFields['type'] = parseInt($('select#macType').val());
		MacFields['status'] = parseFloat($('select#macStatus').val());
		MacFields['description'] = $('input#macDesc').val();
		MacFields['date'] = (new Date()).format('yyyy-MM-dd hh:mm:ss');
		return MacFields;

	};
	var getSearchMacFormValue = function(){
		var MacFields = {};
	//	var MacId = $('input#mac').attr('macId');
	//	if( MacId != undefined && MacId != null){
		//	MacFields['id'] = parseInt(MacId);
	//	}
			
		MacFields['mac'] = $('input#macsearch').val();
	//	MacFields['type'] = parseInt($('select#macType').val());
		//MacFields['status'] = parseFloat($('select#macStatus').val());
		//MacFields['description'] = $('input#macDesc').val();
		//MacFields['date'] = (new Date()).format('yyyy-MM-dd hh:mm:ss');
		return MacFields;

	};
	
	/**bind delete Mac click **/
	var bindDeleteBtnClick = function(){
		$('#deleteMac').on('click', $(this), function(){
			var checkedTr = $('#macBody').find('input[type="checkbox"]:checked').parents('tr');
			
			if(checkedTr > 1)
				return;
			var modal = new $.Modal("macDelete", "删除通道", "是否删除出(入)口!");
			$('#showMessage').html(modal.get());
			modal.show();
			var callback = deleteClickHandle;
			modal.setSubmitClickHandle(callback);
		});
	};
	
	var deleteClickHandle = function(){
		var checkedTr = $('#macBody').find('input[type="checkbox"]:checked').parents('tr');
		var id = parseInt($(checkedTr.find('td')[1]).text());
		$.ajax({
			url:$.fn.config.webroot + '/delete/hardware/' + id,
			type: 'get',
			contentType: 'application/json;charset=utf-8',
			success: function(data){
				if(data['status'] == "1001"){
					$('#refresh').click();
				}else{
					errorHandle(data);
				}
				
			},
			error: function(data){
				errorHandle(data);
			}
		});
	};
	
	/**bind search Mac click **/
	var bindSeacherBtnClick = function(){
		$("#searchMac").on('click',$(this),function(){
		
			$("#searchMacModal").modal('show');
		});
		
	};
	
	/*** get table mac item ***/
	var renderMac = function(low, count){
		
		var cols = $('#macTable').find('thead tr th').length;
		$("#macBody").html('<tr><td colspan="' + cols + '"></td></tr>');
		$.fn.loader.appendLoader($('#macBody').find('td'));

		$.ajax({
			url:$.fn.config.webroot + "/getHardwareDetail?low=" + low + "&count=" + count + "&_t=" + (new Date()).getTime(),
			type: 'get',
			success: function(data){
				fillMacTbody(data);
			},
			error: function(data){
				errorHandle(data);
			}
		});
	};
	
	
	var fillMacTbody = function(data){
		var macBody = $("#macBody");
		$.fn.loader.removeLoader($('#macDiv'));
		macBody.html('');
		data = data["body"];
		for(var i = 0; i < data.length; i++){
			var tr = $("<tr></tr>");
			tr.append('<td><input type="checkbox" /></td>');
			tr.append('<td>' + data[i]['id']+ '</td>');
			tr.append('<td>' + data[i]['mac']+ '</td>');
			tr.append('<td data=' + data[i]['type'] + '>' + (data[i]['type'] == 0 ? "车位MAC":"出入口MAC") + '</td>');
			tr.append('<td data=' + data[i]['status'] + '>' + (data[i]['status'] == 0 ? "已使用":"未使用")+ '</td>');
			
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
			macBody.append(tr);
		}
	};
	
	var errorHandle = function(data){
		var modal = new $.Modal('errorHandle', "失败", "操作失败" + data['message']);
		$('#showErrorMessage').html(modal.get());
		modal.show();
	};
	
	/***render pagination****/
	var renderPagination = function(){

		$.ajax({
			url:$.fn.config.webroot + "/getHardwareCount",
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
		renderMac($.fn.page.pageSize * ($.fn.page.currentPage - 1), $.fn.page.pageSize);
	};
	
})(jQuery);