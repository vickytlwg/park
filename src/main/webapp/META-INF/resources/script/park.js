(function($){
	
	$.fn.park = {}
	
	$.fn.park.initial = function(){
		bindTrClick();
		bindRefreshClick();
		bindAddBtnClick();
		bindSearchBtnClick();
		bindUpdateBtnClick();
		bindSubmitParkBtnClick();
		bindDeleteBtnClick();
		bindSeacherBtnClick();
		renderPark(0, $.fn.page.pageSize);
		renderPagination();
		searchParkByLocation();
		bindUploadPicBtn();
		bindSubmitPicBtn();
	};
	
	var bindUploadPicBtn = function(){
		$("#uploadPic").on('click', $(this), function(){
			var checkedTr = $('#parkBody').find('input[type="checkbox"]:checked').parents('tr');
			if(checkedTr.length != 1)
				return;
			$("#uploadParkPic").modal('show');
			$("#pictureForm")[0].reset();
		});
		
	}
	
	var bindSubmitPicBtn = function(){
		var btn = $("#submitPicBtn");
		btn.on('click', $(this), function(){
			var form = $("#pictureForm");
			var checkedTr = $('#parkBody').find('input[type="checkbox"]:checked').parents('tr');
			var tds = checkedTr.find('td');
			var parkId = parseInt($(tds[1]).text());
			$("#parkId").val(parkId);
			//form.attr("action", form.attr("action") + parkId);
			form[0].submit();
			//windows.location="/parks";
			//$("#uploadParkPic").modal('hide');
		});
		
		
	}
	
	/**bind tr click*/
	var bindTrClick = function(){
		var parkBody = $("#parkBody");
		parkBody.on('click', 'tr', function(event){
			if(event.target.nodeName.toLowerCase() != 'input')
				$(this).find('input[type="checkbox"]').click();
		});
	};
		
	
	/**bind refresh click**/
	var bindRefreshClick = function(){
		var refreshBtn = $('#refresh');
		refreshBtn.on('click', $(this), function(){
			renderPark($.fn.page.pageSize * ($.fn.page.currentPage - 1), $.fn.page.pageSize);
			$(this).blur();
		});
		
	};
	
	/**bind add park click**/
	var bindAddBtnClick = function(){
		$('#addPark').on('click', $(this), function(){
			addBtnClickHandle();
		});
	};
	
	var addBtnClickHandle = function(){
		$('#addParkForm')[0].reset();
		$('input#parkName').removeAttr('parkId');
		$('#addParkResult').html('');
		$('#addParkModal').modal('show');
	};
	
	
	var bindSearchBtnClick=function(){
			
		$('#searchParkl').on('click', $(this), function(){
			$('#searchParkByLocation').modal('show');
		});
	};
	
	/**bind update park click **/
	var bindUpdateBtnClick = function(){
		$('#updatePark').on('click', $(this), function(){
			updateBtnClickHandle();
		});
	};
	
	var searchParkByLocation=function(){
	//	alert('searchParkByLocation');	
		$('#submitSearchParkBtn').on('click', $(this), function(){
			var locationKey={'keywords': $('input#locationKeyWords').val()};
			var url= $.fn.config.webroot + '/search/parkBykeywords';
			$.ajax({
				url:url,
				type: 'post',
				contentType: 'application/json;charset=utf-8',			
				datatype: 'json',
				data:$.toJSON(locationKey),
				success: function(data){
					fillParkTbody(data);
				},
				error: function(data){
					alert("获取数据出错");
				}
			});
		});
	};
	
	var updateBtnClickHandle = function(){
		$('#addParkForm')[0].reset();
		$('#addParkResult').html('');
		var checkedTr = $('#parkBody').find('input[type="checkbox"]:checked').parents('tr');
		if(checkedTr.length == 0)
			return;
		else
			assignAddParkForm($(checkedTr[0]));
		$('#addParkModal').modal('show');
	};
	
	var assignAddParkForm = function(checkedTr){
		
		var tds = checkedTr.find('td');
		$('input#parkName').attr('parkId', parseInt($(tds[1]).text()));
		$('input#parkName').val($(tds[2]).text());		
		$('input#channelCount').val($(tds[3]).text());
		$('input#portCount').val($(tds[4]).text());
		$('input#leftPortCount').val($(tds[5]).text());
		$('input#chargeDaytime').val($(tds[6]).text());
		$('input#chargeNight').val($(tds[7]).text());	
		$('select#parkStatus').val($(tds[8]).attr('data'));
		$('input#isFree')[0].checked = parseInt($(tds[9]).attr('data')) == 1 ? true : false;
		$('input#floorCount').val($(tds[10]).text());
		$('select#parkType').val($(tds[11]).attr('data'));
		var positionInput = $('input#position');
		positionInput.val($(tds[12]).text());
		$('input#longitude').val($(tds[12]).attr('longitude'));
		$('input#latitude').val($(tds[12]).attr('latitude'));
		$('input#mapAddr').val(checkedTr.attr('mapAddr'));
		
		var position = checkedTr.attr('position');
		
		var pos_selects = ['select#seachprov', 'select#seachcity', 'select#seachdistrict'];
		for(var i = 0; i < 3; i++)
			$(pos_selects[i]).val(0);
		$('#positionlast').val("");
		
		var positions = position.split(" ");
		if(positions.length == 2){
			$('#positionlast').val(positions[1]);
		}
		var areas = positions[0].split("-");
		if(areas.length > 1 ){
			
	
			for(var i = 0; i < areas.length; i++){
				var select = $(pos_selects[i]);
				var options = select.find('option');
				for(var j = 0; j < options.length; j++){
					if($(options[j]).text() == areas[i]){
						$(options[j]).attr('selected', 'true');
						select.change();
					}
				}
			}				
			
		}
		
		$('input#number').val(checkedTr.attr('number'));
		$('input#contact').val(checkedTr.attr('contact'));
	};
	
	/**bind submit button of adding and updating park */
	var bindSubmitParkBtnClick = function(){
		
		$('#submitParkBtn').on('click', $(this), function(){		
			var url = '';
			var parkFields = getAddParkFormValue();
			if(parkFields['id'] != undefined && parkFields['id'] != null ){
				url = $.fn.config.webroot + '/update/park';
			}else{
				url = $.fn.config.webroot + '/insert/park';
			}
			
			var addParkResultDiv = $('#addParkResult');
			var loader = new $.Loader();
			addParkResultDiv.append(loader.get());
			loader.show();
			
			$.ajax({
				url:url,
				type: 'post',
				contentType: 'application/json;charset=utf-8',			
				datatype: 'json',
				data: $.toJSON(parkFields),
				success: function(data){
					if(data['status'] = 1001){
						loader.remove();
						addParkResultDiv.append($.fn.tip.success('提交操作完成'));
						setTimeout('$("#addParkResult").html(""); $("#addParkModal").modal("hide");$("#refresh").click()', 500);
					}
				},
				error: function(data){
					loader.remove();
					addParkResultDiv.append($.fn.tip.error('提交操作未完成'));
					setTimeout('$("#addParkResult").html("");', 3000);
				}
			});
		});
	};
	
	var getAddParkFormValue = function(){
		var parkFields = {};
		var parkId = $('input#parkName').attr('parkId');
		if( parkId != undefined && parkId != null){
			parkFields['id'] = parseInt(parkId);
			//parkFields['Id'] = 2;
		}
			
		parkFields['name'] = $('input#parkName').val();
		parkFields['channelCount'] = parseInt($('input#channelCount').val());
		parkFields['portCount'] = parseInt($('input#portCount').val());
		parkFields['portLeftCount'] = parseInt($('input#leftPortCount').val());
		parkFields['charge'] = parseFloat($('input#chargeDaytime').val());
		parkFields['charge1'] = parseFloat($('input#chargeNight').val());
		parkFields['charge2'] = 0.00;
		parkFields['contact'] = $('input#contact').val();
		parkFields['number'] = $('input#number').val();
		parkFields['status'] = parseInt($('select#parkStatus').val());
		parkFields['isFree'] = $('input#isFree')[0].checked ? 1 : 0;
		parkFields['floor'] = parseInt($('input#floorCount').val());
		parkFields['type'] = parseInt($('select#parkType').val());
		parkFields['position'] = getAreaNamebyID(getAreaID())+' '+$('input#positionlast').val();
		parkFields['longitude'] = parseFloat($('input#longitude').val());
		parkFields['latitude'] = parseFloat($('input#latitude').val());
		parkFields['mapAddr'] = $('input#mapAddr').val();
		parkFields['date'] = (new Date()).format('yyyy-MM-dd hh:mm:ss');
		return parkFields;

	};
	
	function getAreaID(){
		var area = 0;          
		if($("#seachdistrict").val() != "0"){
			area = $("#seachdistrict").val();                
		}else if ($("#seachcity").val() != "0"){
			area = $("#seachcity").val();
		}else{
			area = $("#seachprov").val();
		}
		return area;
	}


	function getAreaNamebyID(areaID){
		var areaName = "";
		if(areaID.length == 2){
			areaName = area_array[areaID];
		}else if(areaID.length == 4){
			var index1 = areaID.substring(0, 2);
			areaName = area_array[index1] + "-" + sub_array[index1][areaID];
		}else if(areaID.length == 6){
			var index1 = areaID.substring(0, 2);
			var index2 = areaID.substring(0, 4);
			areaName = area_array[index1] + "-" + sub_array[index1][index2] + "-" + sub_arr[index2][areaID];
		}
		return areaName;
	}
	
	/**bind delete park click **/
	var bindDeleteBtnClick = function(){
		$('#deletePark').on('click', $(this), function(){
			var checkedTr = $('#parkBody').find('input[type="checkbox"]:checked').parents('tr');
			if(checkedTr > 1)
				return;
			var modal = new $.Modal("parkDelete","删除停车场","是否删除停车场!" );
			var callback = deleteClickHandle;
			modal.setSubmitClickHandle(callback);
			$('#showMessage').html(modal.get());
			modal.show();
		});
	};
	
	var deleteClickHandle = function(){
		var checkedTr = $('#parkBody').find('input[type="checkbox"]:checked').parents('tr');
		var id = parseInt($(checkedTr.find('td')[1]).text());
		$.ajax({
			url:$.fn.config.webroot + "/delete/park/" + id,
			type: 'get',
			success: function(data){
				$('#refresh').click();
			},
			error: function(data){
				errorHandle(data);
			}
		});
	};
	
	/**bind search park click **/
	var bindSeacherBtnClick = function(){
		
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
			url: $.fn.config.webroot + "/getParkDetail" ,
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
		data = $.parseJSON(data["body"]);
		for(var i = 0; i < data.length; i++){
			var tr = $('<tr></tr>');
			tr.append('<td><input type="checkbox" /></td>');
			tr.append('<td>' + data[i]['id']+ '</td>');
			tr.append('<td>' + data[i]['name']+ '</td>');
			tr.append('<td>' + data[i]['channelCount']+ '</td>');
			tr.append('<td>' + data[i]['portCount']+ '</td>');
			tr.append('<td>' + data[i]['portLeftCount']+ '</td>');
			tr.append('<td data=' + data[i]['charge'] + ' >' + data[i]['charge']+ '</td>');
			tr.append('<td data=' + data[i]['charge1'] + ' >' + data[i]['charge1']+ '</td>');
			var status = data[i]['status'] == 0 ? '可用':'不可用';
			tr.append('<td data=' + data[i]['status'] + ' >' + status + '</td>');
			var free = parseInt(data[i]['isFree']) == 1 ? '是' : '否';
			tr.append('<td data=' + data[i]['isFree'] + ' >' + free + '</td>');
			tr.append('<td>' + data[i]['floor']+ '</td>');
			tr.attr('mapAddr', data[i]['mapAddr']);
			tr.attr('contact', data[i]['contact']);
			tr.attr('number', data[i]['number']);
			tr.attr('pictureUri', data[i]['pictureUri']);
			tr.attr('position', data[i]['position']);
			
			var type='';
			if(data[i]['type'] == 0)
				type='室内';
			else if(data[i]['type'] == 1)
				type='室外';
			else 
				type='其它';
			tr.append('<td data=' + data[i]['type'] + ' >' + type + '</td>');
			tr.append('<td longitude='+ data[i]['longitude'] +' latitude=' + data[i]['latitude'] + ' >' + data[i]['position']+ '</td>');
			tr.append('<td>' + data[i]['date']+ '</td>');
			if( i % 2 == 0){
				tr.addClass('success');
			}else{
				tr.addClass('active');
			}
		
			var picUri = data[i]['pictureUri']
			if(picUri != undefined && picUri != null && picUri != "" )
				tr.append('<td><a href="#" ref="' + picUri + '"> 显示图片</a></td>');
			else
				tr.append('<td>暂无图片</td>')
			
			tr.find('a').on('click', $(this), function(){
				var modal = new $.Modal('showPic', "停车场图片", '<center><img width=300 height=300 src="' + $(this).attr("ref") + '"></img></center>');
				$('#showPicture').html(modal.get());
				modal.show();
			});
			
			parkBody.append(tr);
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
			url:$.fn.config.webroot + "/getParkCount",
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
		renderPark($.fn.page.pageSize * ($.fn.page.currentPage - 1), $.fn.page.pageSize);
	};
	
})(jQuery);