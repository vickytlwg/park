(function($) {

	$.fn.layout = {};
	// $.fn.layout.cheweiguanli=function(){
	// if($('p#shenfen').text().trim()!="管理员")
	// {
	// var modal=new jQuery.Modal('2','温馨提示','你没有权限进行操作');
	// modal.show();
	// }
	// else{
	// location.href="/park/businessCarport"
	// }
	// }

	$.fn.layout.hiddenall = function() {
		if ($.cookie("lunei") == undefined || $.cookie("lunei") == null) {
			$.cookie("lunei", true);
		}
		console.log($.cookie("lunei"));
		var a = $('.treeview-menu li');
		a.hide();
		$('#llgl').hide();
		$('#luneichange').on('click', function() {
			$('.treeview-menu li').hide();
			$('#llgl').hide();
			$.fn.layout.lunei();
		});
		$('#luwaichange').on('click', function() {
			$('.treeview-menu li').hide();
			$('#llgl').hide();
			$.fn.layout.luwai();
		});
		if ($.cookie("lunei") == "true") {
			$.fn.layout.lunei();
		} else if ($.cookie("lunei") == "false") {
			$.fn.layout.luwai();
		}
		$('#onlyforcss').click();
	};
	$.fn.layout.luwai = function() {
		$.cookie("lunei", "false");
		$('#luneichange').removeClass("luneiluwai");
		$('#luwaichange').addClass("luneiluwai");
		$('#luwaichange').removeClass("hhhh");
		$('#luneichange').addClass("hhhh");
		$('#tcdsj').show();// 停车大数据
		$('#presentData').show();// 今日数据
		$('#jrsj2').show();// 停车云数据
		// $('#s').show();
		// $('#q').show();
		// $('#ld').show();
		/* $('#tcc').show(); */
		$('#tcc2').show();// 停车场管理
		// $('#jfbzgl').show();
		/* $('#sczdgl').show(); */
		/* $('#byyhgl').show(); */
		// $('#yhqgl').show();
		// $('#hbmdgl').show();
		/* $('#sfrygl').show(); */
		/* $('#xjygl').show(); */
		/* $('#jrgk').show(); */
		$('#jrgk2').show();// 今日概况
		// $('#jrtcfx').show();
		// $('#cwsyfx').show();//
		// $('#zdsfgk').show();
		// $('#jrqf').show();
		$('#cwdz2').show();// 财务对账
		$('#cwgl').show();// 车位管理
		$('#cwzt').show();// 车位状态
		$('#bwgk').show();// 泊位概况
		/* $('#cwdz').show(); */
		/* $('#cwlszd').show(); */
		// $('#tcjlcx').show();
		// $('#tpccx').show();
		// $('#sfyyj').show();
		// $('#zjm').show();
		// $('#tccfb').show();
		// $('#crkgl').show();
		/* $('#yjsbgl').show(); */
		/* $('#wzyhgl').show(); */
		$('#wzyhgl2').show();// 用户管理
		/* $('#tokengl').show(); */
		/* $('#jsgl').show(); */
		/* $('#jsfpgl').show(); */
		/* $('#ymgl').show(); */
		/* $('#gdgl').show(); */
		$('#gdgl2').show();// 工单管理
		// $('#llgl').show();
		// $('#llkgl').show();
		// $('#llt').show();
		// $('#llzd').show();
	};
	$.fn.layout.lunei = function() {
		$.cookie("lunei", "true");
		$('#luneichange').removeClass("hhhh");
		$('#luneichange').addClass("luneiluwai");
		$('#luwaichange').removeClass("luneiluwai");
		$('#luwaichange').addClass("hhhh");
		$('#tcdsj').show();// 停车大数据
		$('#presentData').show();// 今日数据
		$('#jrsj2').show();// 停车云数据
		$('#tcc2').show();// 停车场管理
		// $('#jfbzgl').show();
		/* $('#byyhgl').show(); */
		/* $('#yygl').show(); */
		/* $('#jrgk').show(); */
		$('#jrgk2').show();// 今日概况
		// $('#jrqf').show();
		/* $('#cwdz').show(); */
		$('#cwdz2').show();// 财务管理
		$('#crkgl2').show();// 硬件管理
		/* $('#sfyyj').show(); */
		/* $('#cwlszd').show(); */
		// $('#tcjlcx').show();
		// $('#zfsj').show();
		/* $('#dzkzqgl').show(); */
		/* $('#yjsbgl').show(); */
		/* $('#wzyhgl').show(); */
		$('#wzyhgl2').show();// 用户管理
		/* $('#sybwpgl').show(); */
		/* $('#tokengl').show(); */
		/* $('#jsgl').show(); */
		/* $('#jsfpgl').show(); */
		/* $('#ymgl').show(); */
		/* $('#gdgl').show(); */
		$('#gdgl2').show();// 工单管理
	};

})(jQuery);
