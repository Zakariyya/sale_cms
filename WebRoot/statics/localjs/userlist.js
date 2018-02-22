/**
 * 验证email，正则表达式
 * @param str
 * @returns {Boolean}
 */

function checkEmail(str){
	var reg = /^([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+\.[a-zA-Z]{2,3}$/;
	if(str == null || str == "" || reg.test(str))
		return true;
	else
		return false;
}

/**
 * 动态给隐藏域赋值
 */
$("#selectrole").change(function(){
	$("#selectrolename").val($("#selectrole").find("option:selected").text());
});
$("#selectusertype").change(function(){
	$("#selectusertypename").val($("#selectusertype").find("option:selected").text());
});
$("#selectcardtype").change(function(){
	$("#selectcardtypename").val($("#selectcardtype").find("option:selected").text());
});
/**
 * modify_动态给隐藏域赋值
 */
$("#m_roleid").change(function(){
	$("#m_rolename").val($("#m_roleid").find("option:selected").text());
	/**动态加载userType*/
	$("#m_selectusertype").empty();
	$("#m_selectusertype").append("<option value=\"\" selected=\"selected\">--请选择--</option>");
	var sel_role=$("m_roleid").val();
	if(sel_role==2){
		$.post("/backend/loadUserTypeList.html",{'s_roleid':sel_role},function(result){
			if(result != ''){
				for(var i=0; i<result.length; i++){
					/*valueName and valueId are in the table of "data_dictionary"*/
					$("#m_selectusertype").append("<option value=\""+result[i].valueId+"\">"+result[i].valueName+"</option>");
				}
			}else{
				alert("用户类型加载失败");
			}
		},'json');
	}
});
$("#m_selectusertype").change(function(){
	$("#m_selectusertypename").val($("#m_selectusertype").find("option:selected").text());
});
$("#m_cardtype").change(function(){
	$("#m_cardtypename").val($("#m_cardtype").find("option:selected").text());
});



$(".adduser").click(function(e){
	$("#add_formtip").html('');
	
	//把本身的所有事件给屏蔽掉
	e.preventDefault();

	$('#addUserDiv').modal('show');

});

/**
 * 点击"取消"之后，将表单中的数据清空
 */
$('.addusercancel').click(function(e){
	$("#add_formtip").html('');
	$("#a_idPic").html('');
	$("#a_bankPic").html('');
	$("#selectrole").val('');
	$("#selectusertype").val('');
	$("#selectusertype").html('<option value=\"\" selected=\"selected\">--请选择--</option>');
	$("#a_logincode").val('');
	$("#a_username").val('');
	$("#selectcardtype").val('');
	$("#a_idcard").val('');
	$("#a_mobile").val('');
	$("#a_email").val('');
	$("#a_postCode").val('');
	$("#a_bankname").val('');
	$("#a_bankaccount").val('');
	$("#a_accountholder").val('');
	$("#a_useraddress").val('');
});


/**
 * 通过选择"角色"来异步加载"会员类型"列表
 */
$("#selectrole").change(function(){
	$("#selectusertype").empty();
	$("#selectusertype").append("<option value=\"\" selected=\"selected\">--请选择--</option>");
	
	var sel_role=$("#selectrole").val();
	//roleId == 2 是会员，会员才有分等级
	if(sel_role == 2){
		$.post("/backend/loadUserTypeList.html",{'s_roleid':sel_role},function(result){
			if(result != ''){
				for(var i=0; i<result.length; i++){
					/*valueName and valueId are in the table of "data_dictionary"*/
					$("#selectusertype").append("<option value=\""+result[i].valueId+"\">"+result[i].valueName+"</option>");
				}
			}else{
				alert("用户类型加载失败");
			}
		},'json');
		
	}
});

//判断是否重名(增加用户名的时候)
$("#a_logincode").blur(function(){
	$("#a_logincode").empty();
	var alc = $("#a_logincode").val();
	if(alc != ""){
		/**
		 * 异步同名判断     
		 * id:-1  为的是后期跟"修改"共用一个url，只要不跟数据库中的id冲突就行
		 * id: > 0  修改
		 * repeat: 重复了
		 * failed：报错了
		 * ==========================================
		 * eg: $("#add_formtip").attr("key")
		 * $("#add_formtip").attr("logincode")：重名判断标记, 效果查看本js中的 function addUserFunction() 方法
		 * 		1: 重名了
		 * 		0：不重名
		 */
		$.post("/backend/logincodeisexist.html",{'loginCode':alc,'id':'-1'},function(result){
			if(result == "repeat"){
				$("#add_formtip").css("color","red");
				$("#add_formtip").html("<li>对不起，该用户名已存在</li>");
				$("#add_formtip").attr("logincode" ,1); //判断标记(用于提交表单是判断是否重名)
			}else if(result == "failed"){
				alert("操作超时！");
			}else if(result == "only"){
				$("#add_formtip").css("color","green");
				$("#add_formtip").html("<li>该用户名可以正常使用</li>");
				$("#add_formtip").attr("logincode" ,0);//判断标记(用于提交表单是判断是否重名)
			}
		},'html');
	}
});
$("#m_logincode").blur(function(){
	$("#m_logincode").empty();
	var mlc = $("#m_logincode").val();
	if(mlc != ""){
		/**
		 * 异步同名判断     
		 * id:-1  为的是后期跟"修改"共用一个url，只要不跟数据库中的id冲突就行
		 * id: > 0  修改
		 * repeat: 重复了
		 * failed：报错了
		 * ==========================================
		 * eg: $("#add_formtip").attr("key")
		 * $("#add_formtip").attr("logincode")：重名判断标记, 效果查看本js中的 function addUserFunction() 方法
		 * 		1: 重名了
		 * 		0：不重名
		 */
		$.post("/backend/logincodeisexist.html",{'loginCode':mlc,'id':$("#m_id").val()},function(result){
			if(result == "repeat"){
				$("#modify_formtip").css("color","red");
				$("#modify_formtip").html("<li>对不起，该用户名已存在</li>");
				$("#modify_formtip").attr("logincode" ,1); //判断标记(用于提交表单是判断是否重名)
			}else if(result == "failed"){
				alert("操作超时！");
			}else if(result == "only"){
				$("#modify_formtip").css("color","green");
				$("#modify_formtip").html("<li>该用户名可以正常使用</li>");
				$("#modify_formtip").attr("logincode" ,0);//判断标记(用于提交表单是判断是否重名)
			}
		},'html');
	}
});

/**
 * email校验触发
 */
$("#a_email").blur(function(){
	var flag = checkEmail($("#a_email").val());
	if(!flag){//flag is false , the email is exist
		$("#add_formtip").css("color","red");
		$("#add_formtip").html("<li>对不起，email格式不正确</li>");
		$("#add_formtip").attr("email" ,1); //判断标记(用于提交表单是判断是否重名)
	}else{
		$("#add_formtip").html("");
		$("#add_formtip").attr("email" ,0); //判断标记(用于提交表单是判断是否重名)
	}
});
$("#m_email").blur(function(){
	var flag = checkEmail($("#m_email").val());
	if(!flag){//flag is false , the email is exist
		$("#modify_formtip").css("color","red");
		$("#modify_formtip").html("<li>对不起，email格式不正确</li>");
		$("#modify_formtip").attr("email" ,1); //判断标记(用于提交表单是判断是否重名)
	}else{
		$("#modify_formtip").html("");
		$("#modify_formtip").attr("email" ,0); //判断标记(用于提交表单是判断是否重名)
	}
});

/**
 * 添加用户信息验证
 * return true/false
 */
function addUserFunction(){
	$("#add_formtip").html("");
	debugger;
	var result = true;
	if($("#selectrole").val()==""){
		$("#add_formtip").css("color","red");
		$("#add_formtip").html("<li>对不起，角色不能为空</li>");
		result = false;
	}/*角色 ======== END*/
	if($("#selectusertype").val()==""){
		$("#add_formtip").css("color","red");
		$("#add_formtip").html("<li>对不起，会员类型不能为空</li>");
		result = false;
	}/*会员类型 ======== END*/
	if($.trim($("#a_logincode").val())=="" || $("#a_logincode").val() == null){
		$("#add_formtip").css("color","red");
		$("#add_formtip").html("<li>对不起，用户名不能为空</li>");
		result = false;
	}else{//重名，不能提交
		if($("#add_formtip").attr("logincode") == "1"){
			alert("用户名验证============"+$("#add_formtip").attr("logincode"));
			$("#add_formtip").css("color","red");
			$("#add_formtip").html("<li>对不起，该用户名已存在</li>");
			result = false;
		}
	}/*用户名   ========  END*/
	if($.trim($("#a_username").val())=="" || $("#a_username").val() == null){
		$("#add_formtip").css("color","red");
		$("#add_formtip").html("<li>对不起，姓名不能为空</li>");
		result = false;
	}/*姓名 ======== END*/
	if($("#selectcardtype").val()==""){
		$("#add_formtip").css("color","red");
		$("#add_formtip").html("<li>对不起，证件类型不能为空</li>");
		result = false;
	}/*证件类型======== END*/
	if($.trim($("#a_idcard").val())=="" || $("#a_idcard").val() == null){
		$("#add_formtip").css("color","red");
		$("#add_formtip").html("<li>对不起，证件号码不能为空</li>");
		result = false;
	}else{
		if($("#a_idcard").val().length < 6){
			$("#add_formtip").css("color","red");
			$("#add_formtip").append("<li>对不起，证件号码长度必须超过6位。</li>");
			result = false;
		}
	}/*证件号码======== END*/
	if($.trim($("#a_mobile").val())=="" || $("#a_mobile").val() == null){
		$("#add_formtip").css("color","red");
		$("#add_formtip").html("<li>对不起，联系电话不能为空</li>");
		result = false;
	}/*联系电话======== END*/
	if($.trim($("#a_email").val()) !="" && $("#a_email") != null && $("#add_formtip").attr("email") == "1"){
		$("#add_formtip").css("color","red");
		$("#add_formtip").html("<li>对不起，email格式不正确</li>");
		result = false;
	}/*email   ========  END*/
	if($.trim($("#a_bankname").val )=="" || $("#a_bankname") == null){
		$("#add_formtip").css("color","red");
		$("#add_formtip").html("<li>对不起，开户行不能空</li>");
		result = false;
	}/*bankname   ========  END*/
	if($.trim($("#a_bankaccount").val()) =='' || $("#a_bankaccount") == null){
		$("#add_formtip").css("color","red");
		$("#add_formtip").html("<li>对不起，开户卡号不能空</li>");
		result = false;
	}/*bankaccount   ========  END*/
	if($.trim($("#a_accountholer").val()) =="" || $("#a_accountholer") == null){
		$("#add_formtip").css("color","red");
		$("#add_formtip").html("<li>对不起，开户人不能空</li>");
		result = false;
	}/*开户人   ========  END*/
	
	if(result == true)
		alert("添加成功");
	return result;
}/*addUserFunction  END*/


/**
 * 添加用户信息验证
 * return true/false
 */
function modifyUserFunction(){
	$("#modify_formtip").html("");
	debugger;
	var result = true;
	if($("#m_roleid").val()==""){
		$("#modify_formtip").css("color","red");
		$("#modify_formtip").html("<li>对不起，角色不能为空</li>");
		result = false;
	}/*角色 ======== END*/
	if($("#m_selectusertype").val()==""){
		$("#modify_formtip").css("color","red");
		$("#modify_formtip").html("<li>对不起，会员类型不能为空</li>");
		result = false;
	}/*会员类型 ======== END*/
	if($.trim($("#m_logincode").val())=="" || $("#m_logincode").val() == null){
		$("#modify_formtip").css("color","red");
		$("#modify_formtip").html("<li>对不起，用户名不能为空</li>");
		result = false;
	}else{//重名，不能提交
		if($("#modify_formtip").attr("logincode") == "1"){
			alert("用户名验证============"+$("#modify_formtip").attr("logincode"));
			$("#modify_formtip").css("color","red");
			$("#modify_formtip").html("<li>对不起，该用户名已存在</li>");
			result = false;
		}
	}/*用户名   ========  END*/
	if($.trim($("#m_username").val())=="" || $("#m_username").val() == null){
		$("#modify_formtip").css("color","red");
		$("#modify_formtip").html("<li>对不起，姓名不能为空</li>");
		result = false;
	}/*姓名 ======== END*/
	if($("#m_cardtype").val()==""){
		$("#modify_formtip").css("color","red");
		$("#modify_formtip").html("<li>对不起，证件类型不能为空</li>");
		result = false;
	}/*证件类型======== END*/
	if($.trim($("#m_idcard").val())=="" || $("#m_idcard").val() == null){
		$("#modify_formtip").css("color","red");
		$("#modify_formtip").html("<li>对不起，证件号码不能为空</li>");
		result = false;
	}else{
		if($("#m_idcard").val().length < 6){
			$("#modify_formtip").css("color","red");
			$("#modify_formtip").append("<li>对不起，证件号码长度必须超过6位。</li>");
			result = false;
		}
	}/*证件号码======== END*/
	if($.trim($("#m_mobile").val())=="" || $("#m_mobile").val() == null){
		$("#modify_formtip").css("color","red");
		$("#modify_formtip").html("<li>对不起，联系电话不能为空</li>");
		result = false;
	}/*联系电话======== END*/
	if($.trim($("#m_email").val()) !="" && $("#m_email") != null && $("#add_formtip").attr("email") == "1"){
		$("#modify_formtip").css("color","red");
		$("#modify_formtip").html("<li>对不起，email格式不正确</li>");
		result = false;
	}/*email   ========  END*/
	if($.trim($("#m_bankname").val )=="" || $("#m_bankname") == null){
		$("#modify_formtip").css("color","red");
		$("#modify_formtip").html("<li>对不起，开户行不能空</li>");
		result = false;
	}/*bankname   ========  END*/
	if($.trim($("#m_bankaccount").val()) =='' || $("#m_bankaccount") == null){
		$("#modify_formtip").css("color","red");
		$("#modify_formtip").html("<li>对不起，开户卡号不能空</li>");
		result = false;
	}/*bankaccount   ========  END*/
	if($.trim($("#m_accountholer").val()) =="" || $("#m_accountholer") == null){
		$("#modify_formtip").css("color","red");
		$("#modify_formtip").html("<li>对不起，开户人不能空</li>");
		result = false;
	}/*开户人   ========  END*/
	alert("result=="+result);
	if(result == true)
		alert("添加成功");
	return result;
}/*modifyUserFunction  END*/

/**
 * 上传身份证照片 触发(增加)
 */
$("#a_uploadbtnID").click(function(){
	debugger;
	TajaxFileUpload('0','a_fileInputID','a_uploadbtnID','a_idPic','a_fileInputIDPath');
});/*添加图片*/
$("#m_uploadbtnID").click(function(){
	debugger;
	TajaxFileUpload($("#m_id").val(),'m_fileInputID','m_uploadbtnID','m_idPic','m_fileInputIDPath');
});/*修改图片*/

/**
 * 上传银行卡照片 触发(增加)
 */
$("#a_uploadbtnBank").click(function(){
	debugger;
	TajaxFileUpload('0','a_fileInputBank','a_uploadbtnBank','a_bankPic','a_fileInputBankPath');
});/*添加图片*/
$("#m_uploadbtnBank").click(function(){
	debugger;
	TajaxFileUpload($("#m_id").val(),'m_fileInputBank','m_uploadbtnBank','m_bankPic','m_fileInputBankPath');
});/*修改图片*/
/**
 * 0/1:
 * t1: 文件上传输入框的id
 * t2: "上传"按钮的id
 * t3: 显示的图片img的id
 * t4: 图片路径input隐藏域的id(路径的id)
 * 	('0','a_fileInputID','a_uploadbtnID','a_idPic','a_fileInputIDPath');
 */
function TajaxFileUpload(flag, t1, t2, t3, t4){
	debugger;
	if($("#"+t1+"").val() == $("#"+t1+"").val() == null){
		alert("请选择上传文件");
	}else{
		$.ajaxFileUpload({
			url:'/backend/upload.html',
			secureuri:false,
			fileElementId:t1,
			dataType:'json',
			success:function(data){
				data = data.replace(/(^\s*)|(\s*$)/g, "");
				if(data == '1'){
					alert("上传的图片大小不得超过50k");
					/*将上传框修改内容文字：无文件====== 在插件源码中修改。*/
					$("#uniform-"+t1+" span:first").html('无文件');
					/*浏览文件后，点击打开，上传框显示出文件名的时候，获取文件名*/
					$("input[name-'"+t1+"']").change(function(){
						var fn = $("input[name-'"+t1+"']").val();//chrome/firefox
						
						if($.browser.msie){//兼容ie
							fn = fn.substring(fn.lastIndexOf('\\')+1);
						}
						$("#uniform-"+t1+" span:first").html(fn);
					});
				}else if(data == '2'){//上传的格式不正确
					alert("上传的格式不正确");
					$("#uniform-"+t1+" span:first").html('无文件');
					/*浏览文件后，点击打开，上传框显示出文件名的时候，获取文件名*/
					$("input[name-'"+t1+"']").change(function(){
						var fn = $("input[name-'"+t1+"']").val();//chrome/firefox
						
						if($.browser.msie){//兼容ie
							fn = fn.substring(fn.lastIndexOf('\\')+1);
						}
						$("#uniform-"+t1+" span:first").html(fn);
					});
				}else{
					$("#"+t3+"").append("<p><span onclick=\"deletepic('"+flag+"','"+t3+"','"+t2+"',this,'"+data+"','"+t4+"','"+t1+"');\">x</span><img src=\""+data+"?m="+Math.random()+"\" /></p>");
					$("#"+t2+"").hide();
					$("#"+t4+"").val(data);
					
				}
			},error:function(){
				alert("上传失败");
			}
		});
	}
}

/**
 * viewuser
 * 查看按钮
 */
$(".viewuser").click(function(e){
	var m_id = $(this).attr('id');
	//ajax
	debugger;
	$.ajax({
		url:"/backend/getuser.html",
		type:'POST',
		data:{id:m_id},
		dataType:'json',
		timeout:1000,
		error:function(){
			alert("error");
		},
		success:function(result){
			if("failed"==result)
				alert("操作超时");
			else if("nodata"==result)
				alert("没有数据");
			else{
				$('#v_id').val(result.id);
				$('#v_rolename').val(result.roleName);
				$('#v_usertypename').val(result.userTypeName);
				$('#v_logincode').val(result.loginCode);
				$('#v_username').val(result.userName);
				$('#v_sex').val(result.sex);
				$('#v_cardtypename').val(result.cardTypeName);
				$('#v_idcard').val(result.idCard);
				$('#v_birthday').val(result.birthday);
				$('#v_country').val(result.country);
				$('#v_mobile').val(result.mobile);
				$('#v_email').val(result.email);
				$('#v_postcode').val(result.postCode);
				$('#v_bankname').val(result.bankName);
				$('#v_bankaccount').val(result.bankAccount);
				$('#v_accountholer').val(result.accountHolder);
				
				$('#v_refercode').val(result.referCode);
				$('#v_createtime').val(result.createTime);
				$('#v_isstart').val(result.isStart);
				
				if(result.isStart == '1'){
					$('#v_isstart').val("启用");
//					$('v_isstart').append("<option value=\"1\" selected=\"selected\">启用</option><option value=\"2\">未启用</option>")
				}
				if(result.isStart == '2'){
					$('#v_isstart').val("未启用");
//					$('v_isstart').append("<option value=\"1\">启用</option><option value=\"2\" selected=\"selected\">未启用</option>")
				}
				$('#v_useraddress').val(result.userAddress);
				$('#v_fileInputIDPath').val(result.idCardPicPath);
				var v_idcardpicpath=result.idCardPicPath;
				if(v_idcardpicpath == '' || v_idcardpicpath==null)
					$("#v_idPic").append("暂无");
				else{
					$("#v_idPic").append("<p><img src=\""+v_idcardpicpath+"?m="+Math.random()+"\" /></p>");
				}
			
			
				$('#v_fileInputBankPath').val(result.bankPicPath);
				var v_bankpicpath=result.bankPicPath;
				if(v_bankpicpath == '' || v_bankpicpath==null)
					$("#v_bankPic").append("暂无");
				else{
					$("#v_bankPic").append("<p><img src=\""+v_bankpicpath+"?m="+Math.random()+"\" /></p>");
				};
				
				
				e.preventDefault();
				$('#viewUserDiv').modal('show');
			}			
		}
	});
});/*viewuser  END*/

/**
 * 查看信息后，点击关闭(取消)键
 */
$(".viewusercancel").click(function(e){
	$("#v_idPic").html('');
	$("#v_bankPic").html('');
	$("#v_isstart").html('');
});/*viewusercancel  END*/

$(".modifyusercancel").click(function(e){
	$("#m_idPic").html('');
	$("#m_bankPic").html('');
	$("#m_isstart").html('');
	$("#modify_formtip").html('');
});/*viewusercancel  END*/

/**
 * 点击"修改",弹出小窗口, 显示信息页的数据
 */
$(".modifyuser").click(function(e){
	
	var m_id = $(this).attr('id');
//	alert("m_id==="+m_id);
	debugger;
	$.ajax({
		url:"/backend/getuser.html",
		type:'POST',
		data:{id:m_id},
		dataType:'json',
		timeout:1000,
		error:function(){
			alert("error");
		},
		success:function(result){
			if("failed"==result)
				alert("操作超时");
			else if("nodata"==result)
				alert("没有数据");
			else{
				$('#m_id').val(result.id);
				$('#m_logincode').val(result.loginCode);
				$('#m_username').val(result.userName);
				$('#m_birthday').val(result.birthday);
				debugger;
				/**=============================================
				 * cardType(身份证类型)动态加载选项
				 * =============================================
				 * */
				$('#m_cardtypename').val(result.cardTypeName);
				var cardType = $('#m_cardtype').val(result.cardType);
				$('#m_cardtype').html('');
				if(cardType==null || cardType == "")
					$('#m_cardtype').append("<option value=\"\" selected=\"selected\">-- 请选择 --</option>");
				
				for(var i=0;i < cardTypeListJson.length-1;i++){
					if(cardTypeListJson[i].valueId == cardType){
						$('#m_cardtype').append("<option value=\""+result.cardType+"\" selected=\"selected\"> "+result.cardTypeName+" </option>");
					}else{
						$('#m_cardtype').append("<option value=\""+cardTypeListJson[i].valueId+"\" > "+cardTypeListJson[i].valueName+" </option>");
					}
				}
				/**=============================================
				 * role(角色)动态加载选项
				 * =============================================
				 * */
				var roleId = result.roleId;
				var roleName = 	result.roleName;
				$('#m_roleid').val(result.roleId);
				$('#m_roleid').html('');
				$('#m_rolename').val(result.roleName);
				alert("roleId:"+roleId+",roleName:"+roleName);
				if(roleId == '' || roleId == null)
					$('#m_roleid').append("<option value=\"\" selected=\"selected\">-- 请选择 --</option>");
				for(var i = 0;i<roleListJson.length-1;i++){
					if(roleListJson[i].valueId == roleId)
						$('#m_roleid').append("<option value=\""+result.roleId+"\" selected=\"selected\"> "+ result.roleName+" </option>");
					else
						$('#m_roleid').append("<option value=\""+roleListJson[i].id+"\" > "+roleListJson[i].roleName+" </option>");
				}/*role END*/
				
				
				
				/**============================
				 * 根据角色动态加载"会员"
				 * ============================
				 */
				debugger;
				$('#m_selectusertypename').val(result.userTypeName);
				$('#m_selectusertype').html('');
				if(roleId == '2'){
					var userType =result.userType;
					var userTypeName =result.userTypeName;
					if(userType == null || userType == ""){
						$('#m_selectusertype').append("<option value=\"\" selected=\"selected\">-- 请选择 --</option>");
					}
					$.post("/backend/loadUserTypeList.html",{'s_roleid':roleId},function(r){
						if(r != ''){
							for(var i=0; i<r.length; i++){
								if(r[i].valueId == userType)
									/*valueName and valueId are in the table of "data_dictionary"*/
									$('#m_selectusertype').append("<option value=\""+userType+"\" selected=\"selected\"> "+userTypeName+" </option>");
								else{
									/*valueName and valueId are in the table of "data_dictionary"*/
									$('#m_selectusertype').append("<option value=\""+r[i].valueId+"\" > "+r[i].valueName+" </option>");
								}
							}
						}else{
							alert("用户类型加载失败");
						}
					},'json');
					
				}else if (roleId == '1'){
					$('#m_selectusertype').append("<option value=\"\" selected=\"selected\">-- 请选择 --</option>");
				}/*userType END*/
				
				/**
				 * sex 
				 */
				var sex = result.sex;
				$('#m_sex').html('');
				if(sex == null || sex == ""){
					$('#m_sex').append("<option value=\"\" selected=\"selected\">-- 请选择 --</option>");
					$('#m_sex').append("<option value=\"男\" > 男 </option>");
					$('#m_sex').append("<option value=\"女\" > 女 </option>");
				}else if(sex=='男'){
					$('#m_sex').append("<option value=\"男\" selected=\"selected\"> 男 </option>");
					$('#m_sex').append("<option value=\"女\" > 女 </option>");
				}else if(sex=='女'){
					$('#m_sex').append("<option value=\"男\" > 男 </option>");
					$('#m_sex').append("<option value=\"女\" selected=\"selected\"> 女 </option>");
				}/*sex END*/
				
				$('#m_idcard').val(result.idCard);
				$('#m_country').val(result.country);
				$('#m_mobile').val(result.mobile);
				$('#m_email').val(result.email);
				$('#m_postcode').val(result.postCode);
				$('#m_bankname').val(result.bankName);
				$('#m_bankaccount').val(result.bankAccount);
				$('#m_accountholer').val(result.accountHolder);
				$('#m_createtime').val(result.createTime);
				$('#m_refercode').val(result.referCode);
				$('#m_useraddress').val(result.userAddress);
				/**
				 * m_isstart
				 */
				$('#m_isstart').val(result.isStart);
				var isStart = result.isStart;
				$('#m_isstart').html('');
				if(isStart == '1'){
					$('#m_isstart').append("<option value=\"1\" selected=\"selected\">启用</option><option value=\"2\">未启用</option>")
				}else if(isStart == '2'){
					$('#m_isstart').append("<option value=\"1\">启用</option><option value=\"2\" selected=\"selected\">未启用</option>")
				}/*m_isstart END*/
				
				/**
				 * 身份证照片
				 * IdCard图片
				 */
				var m_fileInputIDPath = $('#m_fileInputIDPath').val(result.idCardPicPath);
				var m_idcardpicpath=result.idCardPicPath;
				if(m_idcardpicpath == '' || m_idcardpicpath==null){
					$("#m_idPic").append("暂无");
					$("#m_uploadbtnID").show();
				}
				else{
					$("#m_idPic").append("<p>" +
							"<span onclick=\"deletepic('"+result.id+"'," +
									"'m_idPic','m_uploadbtnID'," +
									"this," +
									"'"+m_idcardpicpath+"','m_fileInputIDPath');\">x</span>" +
											"<img src=\""+m_idcardpicpath+"?m="+Math.random()+"\" /></p>");
//					$("#m_idPic").append("<p><img src=\""+m_idcardpicpath+"?m="+Math.random()+"\" /></p>");
					$("#m_uploadbtnID").hide();
				}/*IdCard图片*/
			
			
				var m_fileInputBankPath = $('#m_fileInputBankPath').val(result.bankPicPath);
				var m_bankpicpath=result.bankPicPath;
				if(m_bankpicpath == '' || m_bankpicpath==null){
					$("#m_bankPic").append("暂无");
					$("#m_uploadbtnBank").show();
				}
				else{
					//deletepic(id,closeSpan,uploadBtn,obj,picPath,picText,fileInputId)
					$("#m_bankPic").append("<p>" +
							"<span onclick=\"deletepic('"+result.id+"'," +
									"'m_bankPic','m_uploadbtnBank'," +
									"this," +
									"'"+m_bankpicpath+"','"+m_fileInputBankPath+"','m_fileInputBank');\">x</span>" +
											"<img src=\""+m_bankpicpath+"?m="+Math.random()+"\" /></p>");
//					$("#m_bankPic").append("<p><img src=\""+m_bankpicpath+"?m="+Math.random()+"\" /></p>");
//					<p><span onclick=\"deletepic('"+flag+"','"+t3+"','"+t2+"',this,'"+data+"','"+t4+"','"+t1+"');\">x</span>
					$("#m_uploadbtnBank").hide();
				};
				e.preventDefault();
				$("#modifyUserDiv").modal('show');
			}/*ajax->success->else  END*/
		}/*ajax->success END*/
	});/*ajax END*/
})/*.modifyuser END*/




















/**
 * 删除图片
 * /**
 * 0/1:
 * t1: 文件上传输入框的id
 * t2: "上传"按钮的id
 * t3: 显示的图片img的id
 * t4: 图片路径input隐藏域的id(路径的id)
 * 	('0','a_fileInputID','a_uploadbtnID','a_idPic','a_fileInputIDPath');
 *
 */
function deletepic(id,closeSpan,uploadBtn,obj,picPath,picText,fileInputId){
	
	//delete
	$.post("/backend/delpic.html",{'id':id,'picPath':picPath}, function(result){
		if("success" == result){
			alert("删除成功");
			debugger;
			alert("picText ===>"+picText+",fileInputId:"+fileInputId);
			$("#"+picText).val('');
			$("#uniform-"+fileInputId+" span:first").html('无文件');
			//document.getElementById(closeSpan).removeChild(obj.parentElement);//靠，删掉一个节点要这么复杂吗???
			/**
			 * 为了不坑自己，于是将上面很坑的写法K掉了
			 */
			$("#"+closeSpan).html('');
			$("#"+uploadBtn).show();
			alert("DELETE ajax success END");
		}else if("ServerFileLost" == result){
			$("#"+picText).val('');
			$("#uniform-"+fileInputId+" span:first").html('无文件');
			$("#"+closeSpan).html('');
			$("#"+uploadBtn).show();
		}else{
			alert("删除失败(failed == result) ");
		}
	},'html');
}



































