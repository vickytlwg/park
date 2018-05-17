(function($){	
	$.fn.park = {};	
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
		bindSearchPark();
		initFeeCriterionSelect();	
		
	    bindauthoritySet();
		bindtextSet();
		bindsubmitAuthorityBtn();
		bindsubmitTextShowBtn();			
	};
	var bindsubmitTextShowBtn=function(){
	    $('#submitTextShowBtn').on('click', $(this), function(){
            updateTextShowData();
        });
	}
	var bindauthoritySet=function(){
	    $('#authoritySet').on('click', $(this), function(){
            authoritySetBtnClickHandle();
        });
	};
	var bindsubmitAuthorityBtn=function(){
	    $('#submitAuthorityBtn').on('click', $(this), function(){
            updateAuthorityData();
        });
	};
	var bindtextSet=function(){
	    $('#textSet').on('click', $(this), function(){
            textSetBtnClickHandle();
        });
	};
	
	var bindSearchPark = function(){
		$('#searchContent').keyup(function (event) {
    if (event.keyCode == "13") {
    var val = $('#searchContent').val();
    if(val!="")
    searchPark(val);
 }
});
		$('#searchButton').on('click', $(this), function(){
			var val = $('#searchContent').val();
			if(val==""||val==null)
			{
			    return;
			}
			searchPark(val);
			
		});
	};
	
	var searchPark = function(parkName){
		$.ajax({
			url: $.fn.config.webroot + "/getParkByName"  ,
			type: 'post',	
			contentType: 'application/json;charset=utf-8',	
			datatype: 'json',
			data:$.toJSON({"name":parkName}),
			success: function(data){
				fillParkTbody(data);
				$('#pagination').html('');
			},
			error: function(data){
				errorHandle(data);
			}
		});
	};
	
	var bindUploadPicBtn = function(){
		$("#uploadPic").on('click', $(this), function(){
			var checkedTr = $('#parkBody').find('input[type="checkbox"]:checked').parents('tr');
			if(checkedTr.length != 1)
				return;
			$("#uploadParkPic").modal('show');
			$("#pictureForm")[0].reset();
		});
		
	};
	
	var bindSubmitPicBtn = function(){
		var btn = $("#submitPicBtn");
		btn.on('click', $(this), function(){
			var form = $("#pictureForm");
			var checkedTr = $('#parkBody').find('input[type="checkbox"]:checked').parents('tr');
			var tds = checkedTr.find('td');
			var parkId = parseInt($(tds[1]).text());
			$("#parkId").val(parkId);
			form[0].submit();
		});		
		
	};
	
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
		    renderPagination();
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
	var authoritySetBtnClickHandle = function(){
	
        var checkedTr = $('#parkBody').find('input[type="checkbox"]:checked').parents('tr');
        if(checkedTr.length == 0){
            alert("选择停车场!");
            return;
        }
            
        else{
           assignAuthority($(checkedTr[0])); 
        }
            
            
	    $('#parkAuthoritySet').modal('show');
	};
	var textSetBtnClickHandle = function(){
	       
        var checkedTr = $('#parkBody').find('input[type="checkbox"]:checked').parents('tr');
        if(checkedTr.length == 0){
            alert("选择停车场!");
            return;
        }
            
        else{
           assignParkShow($(checkedTr[0])); 
        }
        
        $('#showTextSet').modal('show');
    }
	var addBtnClickHandle = function(){
		$('#addParkForm')[0].reset();
		$('input#parkName').removeAttr('parkId');
		$('#addParkResult').html('');		
		initFeeCriterionSelect();
		
		$.ajax({
		    url:"/park/getLastPark",
		    type:'get',
		    datatype: 'json',
		    success:function(data){
		    data=data['park'];
      
		    },
		});
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
	
	
	var initFeeCriterionSelect = function(selectedId){
		url = $.fn.config.webroot + '/fee/criterion/get';	
		$.ajax({
			url:url,
			type: 'get',
			contentType: 'application/json;charset=utf-8',			
			success: function(data){
				if(data.status == 1001){
					$('select#feeCriterion').html('');
					var criterions = data.body;
					
					$('select#feeCriterion').append('<option value=20>未绑定</option>');
					for(var i = 0; i < criterions.length; i++)
					{
						$('select#feeCriterion').append('<option value=' + criterions[i].id+ '>' + criterions[i].name + '</option>');
					}
					if(selectedId != undefined)
						$('select#feeCriterion').val(selectedId);
				}
			},
			error: function(data){
				
			}
		});
	};
	
	var assignAuthority=function(checkedTr){
	    var tds = checkedTr.find('td');
	    var parkId=parseInt($(tds[1]).text());
	    var data = {'parkId': parkId};
        $.ajax({
            url: $.fn.config.webroot + "/carAuthority/getByPark" ,
            type: 'post',
            contentType: 'application/json;charset=utf-8',          
            datatype: 'json',
            data: $.toJSON(data),
            success: function(data){            
                fillAuthorityBody(data.body);
            },
            error: function(data){
                loader.remove();
                errorHandle(data);
            }
        });
	};
	var assignParkShow=function(checkedTr){
        var tds = checkedTr.find('td');
        var parkId=parseInt($(tds[1]).text());
        var data = {'parkId': parkId};
        $.ajax({
            url: $.fn.config.webroot + "/parkShowText/getByPark" ,
            type: 'post',
            contentType: 'application/json;charset=utf-8',          
            datatype: 'json',
            data: $.toJSON(data),
            success: function(data){            
                fillTextShowBody(data.body);
            },
            error: function(data){
               
                
            }
        });
    };
	var fillTextShowBody=function(data){
	      $.each(data,function(index,textShowTmp){
	          if(textShowTmp['channel']==1){
	              $('#textshow1label').val(textShowTmp['id']);
	              $('#text11').val(textShowTmp['line1']);
	              $('#text12').val(textShowTmp['line2']);
	              $('#text13').val(textShowTmp['line3']);
	              $('#text14').val(textShowTmp['line4']);
	          }
	          else{
	              $('#textshow0label').val(textShowTmp['id']);
                  $('#text01').val(textShowTmp['line1']);
                  $('#text02').val(textShowTmp['line2']);
                  $('#text03').val(textShowTmp['line3']);
                  $('#text04').val(textShowTmp['line4']);
	          }
	      })
	}
	var updateTextShowData=function(){
	     var data1={};
        data1['channel']=1;
        data1['id']=parseInt($('#textshow1label').val());
        data1['line1']=$('#text11').val();
        data1['line2']=$('#text12').val();
        data1['line3']=$('#text13').val();
        data1['line4']=$('#text14').val();
        
        
        var data0={};
        data0['channel']=0;
        data0['id']=parseInt($('#textshow0label').val());
        data0['line1']=$('#text01').val();
        data0['line2']=$('#text02').val();
        data0['line3']=$('#text03').val();
        data0['line4']=$('#text04').val();
        
        var data=[];
        data.push(data0);
        data.push(data1);       
          $.ajax({
            url: $.fn.config.webroot + "/parkShowText/updateRows" ,
            type: 'post',
            contentType: 'application/json;charset=utf-8',          
            datatype: 'json',
            data: $.toJSON(data),
            success: function(data){            
                alert("成功!");
            },
            error: function(data){
                
                alert("failed");
            }
        });
	}
	var fillAuthorityBody=function(data){	    
	    $.each(data,function(index,authorityTmp){
	        if(authorityTmp['channel']==1){
	            $('#month1label').val(authorityTmp['id']);
	              $('#month1').removeProp("checked");
	              $('#typeA1').removeProp("checked");
	              $('#typeB1').removeProp("checked");
	               $('#typeC1').removeProp("checked");
                  $('#typeD1').removeProp("checked");
                  $('#temporary1').removeProp("checked");
	            $('#month1').prop("checked",authorityTmp['month']);
	            $('#typeA1').prop("checked",authorityTmp['typea']);
	            $('#typeB1').prop("checked",authorityTmp['typeb']);
	            $('#typeC1').prop("checked",authorityTmp['typec']);
	            $('#typeD1').prop("checked",authorityTmp['typed']);
	            $('#temporary1').prop("checked",authorityTmp['temporary']);
	        }
	        else{
	            $('#month0label').val(authorityTmp['id']);
	            $('#month0').removeProp("checked");
                  $('#typeA0').removeProp("checked");
                  $('#typeB0').removeProp("checked");
                   $('#typeC0').removeProp("checked");
                  $('#typeD0').removeProp("checked");
                  $('#temporary0').removeProp("checked");
	            $('#month0').prop("checked",authorityTmp['month']);
                $('#typeA0').prop("checked",authorityTmp['typea']);
                $('#typeB0').prop("checked",authorityTmp['typeb']);
                $('#typeC0').prop("checked",authorityTmp['typec']);
                $('#typeD0').prop("checked",authorityTmp['typed']);
                $('#temporary0').prop("checked",authorityTmp['temporary']);
	        }
	    });	    
	};
	var updateAuthorityData=function(){
	    var data1={};
	    data1['channel']=1;
	    data1['id']=parseInt($('#month1label').val());
	    data1['month']=$('#month1').prop("checked") ;
	    data1['typea']=$('#typeA1').prop("checked") ;
	    data1['typeb']=$('#typeB1').prop("checked") ;
	    data1['typec']=$('#typeC1').prop("checked") ;
	    data1['typed']=$('#typeD1').prop("checked") ;
	    data1['temporary']=$('#temporary1').prop("checked") ;
	    
	    var data0={};
        data0['channel']=0;
        data0['id']=parseInt($('#month0label').val());
        data0['month']=$('#month0').prop("checked") ;
        data0['typea']=$('#typeA0').prop("checked") ;
        data0['typeb']=$('#typeB0').prop("checked") ;
        data0['typec']=$('#typeC0').prop("checked") ;
        data0['typed']=$('#typeD0').prop("checked") ;
        data0['temporary']=$('#temporary0').prop("checked") ;
	    
	    var data=[];
	    data.push(data0);
	    data.push(data1);	    
	      $.ajax({
            url: $.fn.config.webroot + "/carAuthority/updateRows" ,
            type: 'post',
            contentType: 'application/json;charset=utf-8',          
            datatype: 'json',
            data: $.toJSON(data),
            success: function(data){            
                alert("成功!");
            },
            error: function(data){
                
                alert("failed");
            }
        });
	};
	var assignAddParkForm = function(checkedTr){		
		var tds = checkedTr.find('td');
		$('input#parkName').attr('parkId', parseInt($(tds[1]).text()));
		$('input#parkName').val($(tds[2]).text());	
		$('input#street').val($(tds[3]).text());	
		$('input#channelCount').val($(tds[4]).text());
		$('input#portCount').val($(tds[5]).text());
		$('input#leftPortCount').val($(tds[6]).text());		
		if($('select#feeCriterion option').length == 0)
			initFeeCriterionSelect($(checkedTr).attr('feeCriterionId'));
		else
		$('select#feeCriterion').val($(checkedTr).attr('feeCriterionId'));		
		//$('input#chargeNight').val($(tds[7]).text());	
		$('select#parkStatus').val($(tds[8]).attr('data'));
		$('input#isFree')[0].checked = parseInt($(tds[9]).attr('data')) == 1 ? true : false;
		$('input#floorCount').val($(tds[10]).text());
		$('select#parkType').val($(tds[11]).attr('data'));
//		var positionInput = $('input#position');
//		positionInput.val($(tds[12]).text());
		$('input#longitude').val($(tds[12]).attr('longitude'));
		$('input#latitude').val($(tds[12]).attr('latitude'));
		$('input#mapAddr').val(checkedTr.attr('mapAddr'));		
		$('#tmpStreetId').text($(tds[15]).text());
//		$('select#streetid').val('number:'+$(tds[15]).text());
		$('input#number').val(checkedTr.attr('number'));
		$('input#contact').val(checkedTr.attr('contact'));
		$('input#description').val(checkedTr.attr('description'));
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
						setTimeout('$("#addParkResult").html(""); $("#modalCloseBtn").click();$("#refresh").click()', 500);
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
		console.log($('select#streetid').val());
	    if($('select#streetid').val()){
	        parkFields['streetId'] = $('select#streetid').val().split(':')[1];
	    }
	    else{
	        parkFields['streetId']=$("#tmpStreetId").text();
	    }
		
		parkFields['channelCount'] = parseInt($('input#channelCount').val());
		parkFields['portCount'] = parseInt($('input#portCount').val());
		parkFields['portLeftCount'] = parseInt($('input#leftPortCount').val());
		
		var feeCriterionId = parseInt($('select#feeCriterion').val());
		if(feeCriterionId >= 0 )
		parkFields['feeCriterionId'] = feeCriterionId;
		parkFields['feeCriterionId'] = parseInt($('select#feeCriterion').val());		
		parkFields['contact'] = $('input#contact').val();
		parkFields['number'] = $('input#number').val();
		parkFields['status'] = parseInt($('select#parkStatus').val());
		parkFields['isFree'] = $('input#isFree')[0].checked ? 1 : 0;
		parkFields['floor'] = parseInt($('input#floorCount').val());
		parkFields['type'] = parseInt($('select#parkType').val());
		if($('#isPositionChange').get(0).checked){
		    parkFields['position'] = $('#zoneCenterId').find("option:selected").text()+'-'+$("#areaId").find("option:selected").text()+'-'+$("#streetid").find("option:selected").text();
		}		
		parkFields['longitude'] = parseFloat($('input#longitude').val());
		parkFields['latitude'] = parseFloat($('input#latitude').val());
		parkFields['mapAddr'] = $('input#mapAddr').val();
		parkFields['description'] = $('input#description').val();
		parkFields['date'] = (new Date()).format('yyyy-MM-dd hh:mm:ss');
		return parkFields;

	};
	

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
			url: $.fn.config.webroot + "/getOutsideParkDetail" ,
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
			tr.append('<td>' + data[i]['name']+ '</td>');
			if(data[i]['streetName']!=undefined){
			  tr.append('<td streetId='+data[i]['streetId']+'>' + data[i]['streetName']+ '</td>');  
			}
			else{
			    tr.append('<td >' +''+'</td>'); 
			}
			tr.append('<td>' + data[i]['channelCount']+ '</td>');
			tr.append('<td>' + data[i]['portCount']+ '</td>');
			tr.append('<td>' + data[i]['portLeftCount']+ '</td>');
			var feeCriterionId = data[i]['feeCriterionId'];
			var feeCriterionName = data[i]['feeCriterionName'];
			if(feeCriterionId == undefined || feeCriterionId < 0){
				feeCriterionId = 20;
				feeCriterionName = "未绑定";
			}

			tr.append('<td data=' + feeCriterionId + ' >' + feeCriterionName + '</td>');
			tr.attr('feeCriterionId', feeCriterionId);
			
			
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
			tr.attr('description', data[i]['description']);
			var type='';
			if(data[i]['type'] == 0)
				type='室内';
			else if(data[i]['type'] == 1)
				type='室外';
			else if(data[i]['type'] == 3)
				type='路边';
			else if(data[i]['type'] == 4)
                type='充电桩';
			else 
				type='其它';
			tr.append('<td data=' + data[i]['type'] + ' >' + type + '</td>');
			tr.append('<td longitude='+ data[i]['longitude'] +' latitude=' + data[i]['latitude'] + ' >' + data[i]['position']+ '</td>');
			tr.append('<td>' + data[i]['date']+ '</td>');
			if( i % 2 == 0){
		//		tr.addClass('success');
			}else{
		//		tr.addClass('active');
			}
		
			var picUri = data[i]['pictureUri'];
			if(picUri != undefined && picUri != null && picUri != "" )
				tr.append('<td><a href="#" ref="' + picUri + '"> 显示图片</a></td>');
			else
				tr.append('<td>暂无图片</td>');
			tr.append("<td style='display:none'>"+data[i]['streetId']+"</td>");
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
			url:$.fn.config.webroot + "/getOutsideParkCount",
			type: 'get',
			success: function(data){
				data = $.parseJSON(data["body"]);
				var paginationUl = $.fn.page.initial(data['count'], pageClickFunc);
				$('#pagination').html('');
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