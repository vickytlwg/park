(function($) {
    $.fn.layout = {};
    //	$.fn.layout.cheweiguanli=function(){
    //		if($('p#shenfen').text().trim()!="管理员")
    //		{
    //		var modal=new jQuery.Modal('2','温馨提示','你没有权限进行操作');
    //		modal.show();
    //		}
    //		else{
    //		location.href="/park/businessCarport"
    //		}
    //		}

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
        // $('#luneichange').addClass("luneiluwai");
        // $('#luwaichange').addClass("luneiluwai");
        if ($.cookie("lunei") == "true") {
            // $('#luneichange').addClass("luneiluwai");
            // $('#luwaichange').removeClass("luneiluwai");
            $.fn.layout.lunei();
        } else if($.cookie("lunei") == "false"){
            // $('#luneichange').removeClass("luneiluwai");
            // $('#luwaichange').addClass("luneiluwai");
            $.fn.layout.luwai();
        }
        $('#onlyforcss').click();
    };

    $.fn.layout.lunei = function() {
        $.cookie("lunei", "true");
         $('#luneichange').removeClass("hhhh");
        $('#luneichange').addClass("luneiluwai");
        $('#luwaichange').removeClass("luneiluwai");
         $('#luwaichange').addClass("hhhh");
        $('#tcdsj').show();
        /*$('#tcdsj').show();*/
        $('#tcdsj').show();
        $('#jrsj').show();
        $('#jrsj2').show();
        // $('#s').show();
        // $('#q').show();
        // $('#ld').show();
        $('#crkgl').show();
        $('#tcc').show();
        $('#jfbzgl').show();
        $('#byyhgl').show();
        $('#yygl').show();
        $('#jrgk').show();
 //       $('#jrqf').show();
        $('#cwdz').show();
        $('#cwlszd').show();
        $('#tcjlcx').show();
        $('#zfsj').show();
        $('#dzkzqgl').show();
        $('#yjsbgl').show();
        $('#wzyhgl').show();
        $('#sybwpgl').show();
        $('#tokengl').show();
        $('#jsgl').show();
        $('#jsfpgl').show();
        $('#ymgl').show();
        $('#gdgl').show();
    };
    $.fn.layout.luwai = function() {
        $.cookie("lunei", "false");
        $('#luneichange').removeClass("luneiluwai");
        $('#luwaichange').addClass("luneiluwai");
         $('#luwaichange').removeClass("hhhh");
        $('#luneichange').addClass("hhhh");
        $('#tcdsj').show();
        $('#tcdsj').show();
        $('#jrsj').show();
        $('#jrsj2').show();

        // $('#s').show();
        // $('#q').show();
        // $('#ld').show();
        $('#tcc').show();
        $('#jfbzgl').show();
        $('#sczdgl').show();
        $('#byyhgl').show();
        //  $('#yhqgl').show();
        //   $('#hbmdgl').show();
        $('#sfrygl').show();
        $('#xjygl').show();
        $('#jrgk').show();
        // $('#jrtcfx').show();
        $('#cwsyfx').show();
        //   $('#zdsfgk').show();
  //      $('#jrqf').show();
        $('#cwgl').show();
        $('#cwzt').show();
        $('#bwgk').show();
        $('#cwdz').show();
        $('#cwlszd').show();
        $('#tcjlcx').show();
     //   $('#tpccx').show();
        $('#sfyyj').show();
        //  $('#zjm').show();
  //      $('#tccfb').show();
   //     $('#crkgl').show();
        
        $('#yjsbgl').show();
        $('#wzyhgl').show();
        $('#tokengl').show();
        $('#jsgl').show();
        $('#jsfpgl').show();
        $('#ymgl').show();
        $('#gdgl').show();
        //    $('#llgl').show();
        $('#llkgl').show();
        $('#llt').show();
        $('#llzd').show();
    };
})(jQuery);
