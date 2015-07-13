(function($){
	
	$.fn.channel = {};
	
	$.fn.channel.initial = function(){
		bindTrClick();
		bindRefreshClick();
		bindAddBtnClick();
		bindUpdateBtnClick();
		bindSubmitChannelBtnClick();
		bindDeleteBtnClick();
		bindSeacherBtnClick();
		renderchannel(0, $.fn.page.pageSize);
		renderPagination();
	};
	
	/**bind tr click*/
	var bindTrClick = function(){
		var channelBody = $("#channelBody");
		channelBody.on('click', 'tr', function(event){
			if(event.target.nodeName.toLowerCase() != 'input')
				$(this).find('input[type="checkbox"]').click();
		});
	};
	
	/**bind refresh click***/
	var bindRefreshClick = function(){
		var refreshBtn = $('#refresh');
		refreshBtn.on('click', $(this), function(){
			renderchannel($.fn.page.pageSize * ($.fn.page.currentPage - 1), $.fn.page.pageSize);
			$(this).blur();
		});
		
	};
	
	/**bind add Channel click**/
	var bindAddBtnClick = function(){
		$('#addChannel').on('click', $(this), function(){
			addBtnClickHandle();
		});
	};
	
	var addBtnClickHandle = function(){
		$('#addChannelForm')[0].reset();
		$('select#parkName').removeAttr('ChannelId');
		$('select#parkName').removeAttr('disabled');
		$('select#parkName').html('');
		$('#addChannelResult').html('');	
		$('#addChannelModal').modal('show');
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
			url:$.fn.config.webroot + '/getUnBoundHardwares/1?_t=' + (new Date()).getTime(),
			type: 'get',
			contentType: 'application/json;charset=utf-8',
			success: function(data){successFunc(data);},
			error: function(data){errorFunc(data);}
		});
	};
	/**bind update Channel click **/
	var bindUpdateBtnClick = function(){
		$('#updateChannel').on('click', $(this), function(){
			updateBtnClickHandle();
		});
	};
	
	var updateBtnClickHandle = function(){
		$('#addChannelForm')[0].reset();
		$('#addChannelResult').html('');
		var checkedTr = $('#channelBody').find('input[type="checkbox"]:checked').parents('tr');
		if(checkedTr.length == 0){
			alert('请选择一个列表项');
			return;
		}else
			assignAddChannelForm($(checkedTr[0]));
		$('#addChannelModal').modal('show');
	};
	
	var assignAddChannelForm = function(checkedTr){
		
		var tds = checkedTr.find('td');
		$('select#parkName').attr('ChannelId', parseInt($(tds[1]).text()));
		$('select#parkName').html('');
		$('select#parkName').append($('<option value=-1>' + $(tds[2]).text() + '</option>'));
		$('select#parkName').attr('disabled', 'true');
		$('input#channelNumber').val(parseInt($(tds[3]).text()));
		$('select#macId').val(parseInt($(tds[4]).text()));
		$('select#channelFlag').val(parseInt($(tds[5]).attr('data')));
		$('select#channelStatus').val(parseInt($(tds[6]).attr('data')));
		$('input#channelDesc').val($(tds[7]).text());
		var additionalOption = [];
		additionalOption.push($('<option value=' + parseInt($(tds[4]).attr('data')) + ' selected>' + $(tds[4]).text() + '</option>'));
		fillMacInfo(additionalOption);
			
	};
	
	/**bind submit button of adding and updating Channel */
	var bindSubmitChannelBtnClick = function(){
		
		$('#submitChannelBtn').on('click', $(this), function(){		
			var url = '';
			var ChannelFields = getAddChannelFormValue();
			if(ChannelFields['id'] != undefined && ChannelFields['id'] != null ){
				url = $.fn.config.webroot + '/update/channel';
			}else{
				url = $.fn.config.webroot + '/insert/channel';
			}
			
			var addChannelResultDiv = $('#addChannelResult');
			$.fn.loader.appendLoader(addChannelResultDiv);
			
			$.ajax({
				url:url,
				type: 'post',
				contentType: 'application/json;charset=utf-8',			
				datatype: 'json',
				data: $.toJSON(ChannelFields),
				success: function(data){
					if(data['status'] = 1001){
						$.fn.loader.removeLoader(addChannelResultDiv);
						addChannelResultDiv.append($.fn.tip.success('提交操作完成'));
						setTimeout('$("#addChannelResult").html(""); $("#addChannelModal").modal("hide");$("#refresh").click()', 500);
					}
				},
				error: function(data){
					$.fn.loader.removeLoader(addChannelResultDiv);
					addChannelResultDiv.append($.fn.tip.error('提交操作未完成'));
					setTimeout('$("#addChannelResult").html("");', 3000);
				}
			});
		});
	};
	
	var getAddChannelFormValue = function(){
		var ChannelFields = {};
		var ChannelId = $('select#parkName').attr('ChannelId');
		if( ChannelId != undefined && ChannelId != null){
			ChannelFields['id'] = parseInt(ChannelId);
		}
			
		ChannelFields['parkId'] = parseInt($('select#parkName').val());
		ChannelFields['channelId'] = parseInt($('input#channelNumber').val());
		ChannelFields['macId'] = parseInt($('select#macId').val());
		ChannelFields['channelFlag'] = parseInt($('select#channelFlag').val());
		ChannelFields['status'] = parseFloat($('select#channelStatus').val());
		ChannelFields['isEffective'] = 1
		ChannelFields['description'] = $('input#channelDesc').val();
		ChannelFields['date'] = (new Date()).format('yyyy-MM-dd hh:mm:ss');
		return ChannelFields;

	};
	
	/**bind delete Channel click **/
	var bindDeleteBtnClick = function(){
		$('#deleteChannel').on('click', $(this), function(){
			var checkedTr = $('#channelBody').find('input[type="checkbox"]:checked').parents('tr');
			
			if(checkedTr > 1)
				return;
			var modal = new $.Modal("channelDelete", "删除通道", "是否删除出(入)口!");
			$('#showMessage').html(modal.get());
			modal.show();
			var callback = deleteClickHandle;
			modal.setSubmitClickHandle(callback);
		});
	};
	
	var deleteClickHandle = function(){
		var checkedTr = $('#channelBody').find('input[type="checkbox"]:checked').parents('tr');
		var id = parseInt($(checkedTr.find('td')[1]).text());
		$.ajax({
			url:$.fn.config.webroot + '/delete/channel/' + id,
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
	
	/**bind search Channel click **/
	var bindSeacherBtnClick = function(){
		
	};
	
	/*** get table channel item ***/
	var renderchannel = function(low, count){
		
		var cols = $('#channelTable').find('thead tr th').length;
		$("#channelBody").html('<tr><td colspan="' + cols + '"></td></tr>');
		$.fn.loader.appendLoader($('#channelBody').find('td'));

		$.ajax({
			url:$.fn.config.webroot + "/getchannelDetail?low=" + low + "&count=" + count + "&_t=" + (new Date()).getTime(),
			type: 'get',
			success: function(data){
				fillchannelTbody(data);
			},
			error: function(data){
				errorHandle(data);
			}
		});
	};
	
	
	var fillchannelTbody = function(data){
		var channelBody = $("#channelBody");
		$.fn.loader.removeLoader($('#channelDiv'));
		channelBody.html('');
		data = $.parseJSON(data["body"]);
		for(var i = 0; i < data.length; i++){
			var tr = $("<tr></tr>");
			tr.append('<td><input type="checkbox" /></td>');
			tr.append('<td>' + data[i]['id']+ '</td>');
			tr.append('<td>' + data[i]['parkName']+ '</td>');
			tr.append('<td>' + data[i]['channelId']+ '</td>');
			tr.append('<td data=' + data[i]['macId'] + '>' + data[i]['mac']+ '</td>');
			tr.append('<td data=' + data[i]['channelFlag'] + '>' + (data[i]['channelFlag']==1?"入口":"出口")+ '</td>');
			tr.append('<td data=' + data[i]['status'] + '>' + (data[i]['status'] == 1 ? "可用":"不可用")+ '</td>');
			
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
			channelBody.append(tr);
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
			url:$.fn.config.webroot + "/getchannelCount",
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
		renderchannel($.fn.page.pageSize * ($.fn.page.currentPage - 1), $.fn.page.pageSize);
	};
	
})(jQuery);