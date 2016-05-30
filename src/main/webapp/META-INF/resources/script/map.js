(function($){
    $.fn.map={};    
    $.fn.map.initial=function(){
        mapInitial();
        $.fn.map.getData();
        setInterval("$.fn.map.getData()",4000); 
    };
    var map;
    var point;
    var mapClusterer;
    var mapInitial=function(){
        map = new BMap.Map("parkmap");
        point = new BMap.Point(118.8, 32.0625);
        map.centerAndZoom(point, 12);
        map.setMinZoom(11);
        map.setMaxZoom(14);     
        map.enableScrollWheelZoom();                    
        mapClusterer = new BMapLib.MarkerClusterer(map,{maxZoom:11,isAverangeCenter:true});
    };
    var data_info;
    var opts = {
                width : 90,     // 信息窗口宽度
                height: 100,     // 信息窗口高度
                title : "停车场信息:" , // 信息窗口标题
                enableMessage:true,//设置允许信息窗发送短息
               };
    function addClickHandler(content,marker){
        marker.addEventListener("click",function(e){
    //      openInfo(content,e)
            var infoWindow = new BMapLib.SearchInfoWindow(map,content,
                    {
                //    title: title, //标题
                        width: 90, //宽度
                        height: 100, //高度
                        panel : "panel", //检索结果面板
                        enableAutoPan : true, //自动平移
                        enableSendToPhone: true, //是否显示发送到手机按钮
                        searchTypes :[
                            BMAPLIB_TAB_SEARCH,   //周边检索
                            BMAPLIB_TAB_TO_HERE,  //到这里去
                            BMAPLIB_TAB_FROM_HERE //从这里出发
                        ]
                        }
                    );
            infoWindow.open(e.target.getPosition()); 
            }
        );
    }
    function openInfo(content,e){
        var p = e.target;
        var point = new BMap.Point(p.getPosition().lng, p.getPosition().lat);
        var infoWindow = new BMap.InfoWindow(content,opts);  // 创建信息窗口对象 
        map.openInfoWindow(infoWindow,point); //开启信息窗口
    } 
    var showparks=function(title){
        map.clearOverlays();
        mapClusterer.clearMarkers();
        for(var i=0;i<data_info.length;i++){
            var point=new BMap.Point(data_info[i][0],data_info[i][1]);
            var marker = new BMap.Marker(point);  // 创建标注
            var content = data_info[i][2];
            var opts = {
                      position : point,    // 指定文本标注所在的地理位置
                //    offset   : new BMap.Size(30, -30)    //设置文本偏移量
                };
            var label = new BMap.Label(data_info[i][3], opts);
            label.setStyle({color:"blue", "font-weight":"bold"});
            map.addOverlay(label);               // 将标注添加到地图中
            mapClusterer.addMarker(marker);
            addClickHandler(content,marker);
        }
    };
    $.fn.map.getData=function(){
        $.ajax({
            url:'/park/getParks',
            type: 'get',
            contentType: 'application/json;charset=utf-8',          
            datatype: 'json',
            success: function(data){
                var parkdata=data.body;

                data_info=new Array(parkdata.length);
                for(var i=0;i<parkdata.length;i++){
                    console.log(parkdata[i].name+'\n');
                    var tmparray=new Array(4);
                    tmparray[0]=parkdata[i].longitude;
                    tmparray[1]=parkdata[i].latitude;
                      var v_html = '<div id="tipsjt"></div>';
                        v_html += '    <h4 class="font14 green relative">' + parkdata[i].name + '<i class="i pointer" onclick="closeTip()"></i></h4>';
                        v_html += '<p class="font14">空余车位：<b class="red">' + parkdata[i].portLeftCount + '</b> 个' + (parkdata[i].portLeftCount > 0 ? '<a href="#" class="but_b back_orange font18 radius_3 absolute reservation" style="right:10px;" pid="' + i+ '"><i class="i"></i>预定</a>' : '') + '</p>';
                        v_html += '<p class="green font14">收费标准：</p> ';
                        v_html += '  <div class="color_9">';
                    tmparray[2]= v_html;
                    tmparray[3]= parkdata[i].portLeftCount;
                    data_info[i]=tmparray;
                    
                }
                showparks();
            },
        });
    };

})(jQuery);