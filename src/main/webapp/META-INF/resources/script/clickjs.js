$(function(){ 	
	//alert("js load ok");
	var aa=$('#daohang li a');
	var bb=$('#daohang li');
//	alert(window.location.href);
	//bb.addClass("active");
//	alert("add class ok");
	var url=window.location.href;
	var subUrl=url.substring(url.length-4);
	//alert(subUrl);
	$('#daohang li').removeClass("active");
	switch (subUrl) {
	case "cess":
		$('.daohang1').addClass("active");
		break;
	case "ark/":
		$('.daohang1').addClass("active");
		break;
	case "arks":
		$('.daohang2').addClass("active");
		break;
	case "nnel":
		$('.daohang3').addClass("active");
		break;
	case "port":
		$('.daohang4').addClass("active");
		break;
	case "ware":
		$('.daohang5').addClass("active");
		break;
	case "sers":
		$('.daohang6').addClass("active");
		break;
	default:
		//alert("unknow url");
		break;
	}
//	aa.on('click',$(this),function(){
//	alert("click ok");
//	alert(window.location.href);
	
//	}
	
	//alert("remove class ok");
	

});