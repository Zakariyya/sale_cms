$("#loginBtn").click(function(){
	var user = new Object();
	user.loginCode = $.trim($("#loginCode").val());
	user.password = $.trim($("#password").val());
	
	user.isStart = 1;
	
	if(user.loginCode == "" || user.loginCode == null){
		$("#loginCode").focus;
		$("#formtip").css("color","red");
		$("#formtip").html("对不起，登录账号不能为空");
	}else if(user.password == "" || user.password == null){
		$("#loginCode").focus;
		$("#formtip").css("color","red");
		$("#formtip").html("对不起，登录密码不能为空");
	}else{
		$("#formtip").html("");
		
		$.ajax({
			type:"POST",
			url:"/login.html",
			data:{user:JSON.stringify(user)},
			dataType: 'html',//ajax返回的数据类型，result
			timeout:1000,//超时
			//当发生非200，比如：404、500 状态码的时候进入error错误提示
			error:function(){
				$("#formtip").css("color","red");
				$("#formtip").html("登录失败，请重试");
			},success:function(result){ //这里的result类型是dataType
				//如果登录成功，跳转到，/mian.html
				if(result != "" && result == "success"){
					window.location.href="/main.html";
				}else if(result == "failed"){
					$("#formtip").css("color","red");
					$("#formtip").html("登录失败，请重试");
					$("#loginCode").val('');
					$("#password").val('');
				}else if(result == "nologincode"){
					$("#formtip").css("color","red");
					$("#formtip").html("对不起，账号不存在，请重试");
					$("#loginCode").val('');
				}else if(result == "pwderror"){
					$("#formtip").css("color","red");
					$("#formtip").html("对不起，密码错误，请重试");
					$("#password").val('');
				}else if("nodata" == result){
					$("#formtip").css("color","red");
					$("#formtip").html("对不起，没有任何数据需要被处理，请重试");
				}
				
			}
			
		})
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
});