<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/pages/common/head.jsp" %>

<div>
	<ul class="breadcrumb">
		<li><a href="#">后台管理</a> <span class="divider">/</span></li>
		<li><a href="/backend/userlist.html">用户管理</a></li>
	</ul>
</div>
<div class="row-fluid sortable">		
	<div class="box span12">
		<div class="box-header well" data-original-title>
			<h2><i class="icon-user"></i>用户</h2>
			<div class="box-icon">
				<!--
				<a href="#" class="btn btn-setting btn-round"><i class="icon-cog"></i></a>
				<a href="#" class="btn btn-minimize btn-round"><i class="icon-chevron-up"></i></a>
				<a href="#" class="btn btn-close btn-round"><i class="icon-remove"></i></a> 
				-->
				<span class="icon32 icon-color icon-add custom-setting adduser" ></span>
			</div>
		</div>
		<div class="box-content">
			<form action="/backend/userlist.html" method="post">
				<div class="searcharea">
					用户名称：
					<input type="text" name="s_loginCode" value="${loginCode }">
					推荐人：
					<input type="text" name="s_referCode" value="${referCode }">
					角色：
					<select name="s_roleId" style="width:100px">
						<option value="" selected="selected">--请选择--</option>
						<c:forEach items="${roleList }" var="role">
							<option <c:if test="${role.id == s_roleId }"> selected="selected"</c:if> 
								value="${role.id }">${role.roleName }</option>
						</c:forEach>
					</select>
					是否启用：
					<select name="s_isStart" style="width:100px">
					<!-- 
						启用：1 
						不启用：""/2
					-->
						<option value="" selected="selected">--请选择--</option>
						<c:if test="${s_isStart == 1}">
							<option value="1" selected="selected">启用</option>
							<option value="2" >未启用</option>
						</c:if>					
						<c:if test="${s_isStart == 2}">
							<option value="1" >启用</option>
							<option value="2" selected="selected">未启用</option>
						</c:if>					
						<c:if test="${s_isStart == null || s_isStart == ''}">
							<option value="1" >启用</option>
							<option value="2" >未启用</option>
						</c:if>					
					</select>
					
					<button type="submit" class="btn btn-primary"><i class="icon-search icon-white"></i>查询</button>
					
				</div>
			</form>
			<table class="table table-striped table-bordered bootstrap-datatable datatable">
			  <thead>
				  <tr>
					  <th>用户名</th>
					  <th>角色</th>
					  <th>会员类型</th>
					  <th>推荐人</th>
					  <th>启用状态（启用/禁用）</th>
					  <th>注册时间</th>
					  <th>操作</th>
				  </tr>
			  </thead>   
			  <tbody>
			  	<c:if test="${page.items != null }">
			  		<c:forEach items="${page.items }" var="user">
			  			<tr>
							<td class="center">${user.loginCode }</td>
							<td class="center">${user.roleName }</td>
							<td class="center">${user.userTypeName }</td>
							<td class="center">${user.referCode }</td>
							<td class="center">
								<c:if test="${user.isStart == 2 }"><input type="checkbox" disabled="disabled" /></c:if>
								<c:if test="${user.isStart == 1 }"><input type="checkbox" checked="true" disabled="disabled" /></c:if>
							</td>
							<td class="center">
								<fmt:formatDate value="${user.createTime }" pattern="yyyy-MM-dd" />
							</td>
							<td class="center">
								<a class="btn btn-success viewuser" href="#" id="${user.id }">
									<i class="icon-zoom-in icon-white "></i>  
									查看                                            
								</a>
								<a class="btn btn-info modifyuser" href="#"  id="${user.id }">
									<i class="icon-edit icon-white "></i>  
									修改                                            
								</a>
								<a class="btn btn-danger deleteuser" href="#" id="${user.id }">
									<i class="icon-trash icon-white"></i> 
									删除
								</a>
							</td>			  	
			  			</tr>
			  		</c:forEach>
			  	</c:if>
				
				</tbody>
			</table>
			<div class="pagination pagination-centered">
				<ul>
						<!-- 判断是否首页 -->
						<!-- 不是首页则设置为可点击，并将页面信息带上 -->
					<c:choose >
						<c:when test="${page.page ==1 }">
							<li class="active"><a href="javascript:void();" title="首页">首页</a></li>
						</c:when>
						<c:otherwise>
							<li><a href="/backend/userlist.html?currentPage=1&&s_loginCode=${s_loginCode }&&s_referCode=${s_referCode}&&s_roleId=${roleId}&&s_isStart=${s_isStart}" title="首页">首页</a></li>
						</c:otherwise>
					</c:choose>
					<!-- 前面num个页数 -->
					<c:if test="${page.prevPages != null }">
						<c:forEach items="${page.prevPages}" var="num">
							<li><a href="/backend/userlist.html?currentpage=${num }&&s_loginCode=${s_loginCode }&&s_referCode=${s_referCode}&&s_roleId=${roleId}&&s_isStart=${s_isStart}" title=${num }>${num }</a></li>
						</c:forEach>
						
					</c:if>
					
					<!-- 当前页 -->
					<li class="active"><a href="#" title=${page.page }>${page.page }</a></li>
					
					<!-- 后面num个页数 -->
					<c:if test="${page.nextPages != null }">
						<c:forEach items="${page.nextPages}" var="num">
							<li><a href="/backend/userlist.html?currentpage=${num }&&s_loginCode=${s_loginCode }&&s_referCode=${s_referCode}&&s_roleId=${roleId}&&s_isStart=${s_isStart}" title=${num }>${num }</a></li>
						</c:forEach>
						
					</c:if>
					<!-- 判断是否首页 -->
						<!-- 不是首页则设置为可点击，并将页面信息带上 -->
					
					<c:if test="${page.pageCount != null }">
						<c:choose >
						<c:when test="${page.page ==page.pageCount }">
							<li class="active"><a href="javascript:void();" title="尾页">尾页</a></li>
						</c:when>
						<c:otherwise>
							<li><a href="/backend/userlist.html?currentpage=${page.pageCount }&&s_loginCode=${s_loginCode }&&s_referCode=${s_referCode}&&s_roleId=${roleId}&&s_isStart=${s_isStart}" title="尾页">尾页</a></li>
						</c:otherwise>
						</c:choose>
					</c:if>
					<c:if test="${page.pageCount == null }">
						<li class="active"><a href="javascript:void();" title="尾页">尾页</a></li>
					</c:if>
					
				</ul>
			</div>     
		</div>
	</div>
</div>
<div class="modal hide fade" id="addUserDiv">
	<!-- enctype="multipart/form-data">>>> 文件上传 
		onsubmit="return addUserFuntion();">>>>> 表单验证
		addUserFunction >>>>>> 用于表单验证提示
	-->
	<form action="/backend/adduser.html" method="post" enctype="multipart/form-data" onsubmit="return addUserFunction();">
		<div class="modal-header">
			<button type="button" class="close" data-dismiss="modal">×</button>
			<h3>添加用户信息</h3>
		</div>	
		<div class="modal-body">
			<!-- <p>Here settings can be configured...</p> -->
			<!-- 错误信息的提示 -->
			<ul id="add_formtip"></ul>
			<!-- 
				a_loginCode(a:是指add的缩写)
				m_loginCode(m:是指 modify 的缩写)
				下面类似，不再做注释
			-->
			<ul class="topul">
				<li>
					<label>角色：</label>
					<!-- 记录角色名 -->
					<input id="selectrolename" type="hidden" name="roleName" value="" />
					<select id="selectrole" name="roleId" style="width:100px;">
						<option value="" selected="selected">--请选择--</option>
						<c:if test="${roleList != null }">
							<c:forEach items="${roleList }" var="role" >
								<option value="${role.id }" >${role.roleName }</option>
							</c:forEach>
						</c:if>						
					</select>
					<span style="color:red;font-size:bold;">*</span>				
				</li><!-- 角色  END -->
				
				<li>
					<label>会员类型：</label>
					<!-- 记录会员类型名 -->
					<input id="selectusertypename" type="hidden" name="userTypeName" value="" />
					<select id="selectusertype" name="userType" style="width:100px;">
						<option value="" selected="selected">--请选择--</option>
						<!-- 将根据"角色"进行填充，用ajax异步加载 -->			
					</select>
					<span style="color:red;font-size:bold;">*</span>				
				</li><!-- 会员类型   END -->
				<li>
					<label>用户名：</label>
					<!-- 记录用户名    
						a_loginCode(a:是指add的缩写)
					-->
					<!-- 后台ajax进行验证      前台用正则表达式-->
					<input id="a_logincode" type="text" style="width:150px;" name="loginCode" onkeyup="value=value.replace(/[^\w\.\/]/ig,'')"/>
					<span style="color:red;font-size:bold;">*</span>				
				</li><!-- 用户名    END -->
				<li>
					<label>姓名：</label>
					<input id="a_username" type="text" name="userName" style="width:150px;" />
					<span style="color:red;font-size:bold;">*</span>				
				</li><!-- 姓名      END -->
				<li>
					<label>性别：</label>
					<select id="sex" name="sex" style="width:100px;">
						<option value="" selected="selected">-- 请选择 --</option>
						<option value="男" >男</option>
						<option value="女" >女</option>
					</select>
				</li><!-- 性别      END -->
				<li>
					<label>证件类型：</label>
					<!-- 记录证件类型名 -->
					<input id="selectcardtypename" type="hidden" name="cardTypeName" value="" />
					<select id="selectcardtype" name="cardType" style="width:100px;">
						<option value="" selected="selected">--请选择--</option>
						<c:if test="${cardTypeList != null }">
							<c:forEach items="${cardTypeList }" var="cardType">
								<option value="${cardType.valueId }">${cardType.valueName }</option>
							</c:forEach>
						</c:if>				
					</select>
					<span style="color:red;font-size:bold;">*</span>				
				</li><!-- 证件类型      END -->
				<li>
					<label>证件号码：</label>
					<input id="a_idcard" type="text" name="idCard" style="width:150px;" onkeyup="value=value.replace(/[^\w\.\/]/ig,'')"/>
					<span style="color:red;font-size:bold;">*</span>				
				</li><!-- 证件号码      END -->
				<li>
					<label>生日：</label>
					<input id="a_birthday" type="text" class="Wdate" size="15" name="birthday" readonly="readonly" onclick="WdatePicker();" />
				</li><!-- 生日        END -->
				<li>
					<label>收货国家：</label>
					<input id="a_country" type="text" name="country" style="width:150px;" value="中国" />
				</li><!-- 收货国家      END -->
				<li>
					<label>联系电话：</label>
					<input id="a_mobile" type="text" name="mobile" style="width:150px;" onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')"/>
					<span style="color:red;font-size:bold;">*</span>				
				</li><!-- 联系电话      END -->
				<li>
					<label>Email：</label>
					<input id="a_email" type="email" name="email" style="width:150px;" />
					<span style="color:red;font-size:bold;">*</span>				
				</li><!-- email  END -->
				<li>
					<label>邮政编码：</label>
					<input id="a_postcode" type="text" name="postCode" style="width:150px;" onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')"/>
				</li><!-- postCode  END -->
				<li>
					<label>开户行：</label>
					<input id="a_bankname" type="text" name="bankName" style="width:150px;" />
					<span style="color:red;font-size:bold;">*</span>				
				</li><!-- bankname      END -->
				<li>
					<label>开户卡号：</label>
					<input id="a_bankaccount" type="text" name="bankAccount" style="width:150px;" onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')"/>
					<span style="color:red;font-size:bold;">*</span>				
				</li><!-- bankAccount      END -->
				<li>
					<label>开户人：</label>						 
					<input id="a_accountholer" type="text" name="accountHolder" style="width:150px;" />
					<span style="color:red;font-size:bold;">*</span>				
				</li><!-- accountHoler      END -->
				<li>
					<label>推荐人：</label>
					<input id="a_refercode" type="text" class="Wdate" size="15" name="referCode" value="${user.loginCode }" readonly="readonly"  />
				</li><!-- 推荐人        END -->
				<li>
					<label>注册时间：</label>
					<input type="text" id="a_createtime" name="createTime"  value="" readonly="readonly"/>
				</li><!-- 推荐人        END -->
				<li>
					<label>是否启用：</label>
					<select id="isstart" name="isStart" style="width:100px;">
						<option value="" selected="selected">-- 请选择 --</option>
						<option value="1" >启用</option>
						<option value="2" >未启用</option>
					</select>
				</li><!-- 启用      END -->
				<li class="lastli">
				  	<label>收货地址：</label><textarea id="a_useraddress" name="userAddress"></textarea>
				</li><!-- 收货地址      END -->
			
			</ul>
			<div class="clear"></div>
			<!-- 放置图片 -->
			<ul class="downul">
				<li>
					<label>上传身份证图片：</label>
					<!-- 记录上传身份证图片的路径
						a_fileInputIDPath(a:是指add的缩写)
					-->
					<input type="hidden" id="a_fileInputIDPath" name="idCardPicPath" value=""/>
					<input id="a_fileInputID" name="a_fileInputID" type="file"/>
					<input type="button" id="a_uploadbtnID" value="上传"/>
					<p><span style="color:red;font-weight: bold;">*注：1、正反面.2、大小不得超过50k.3、图片格式：jpg、png、jpeg、pneg</span></p>
					<div id="a_idPic"></div>
				</li>
			</ul>
			<ul class="downul">
				<li>
				<label>上传银行卡图片：</label>
					<input type="hidden" id="a_fileInputBankPath" name="bankPicPath" value=""/>
					<input id="a_fileInputBank" name="a_fileInputBank" type="file"/>
					<input type="button" id="a_uploadbtnBank" value="上传"/>
					<p><span style="color:red;font-weight: bold;">*注：1、大小不得超过50k.2、图片格式：jpg、png、jpeg、pneg</span></p>
					<div id="a_bankPic"></div>
				 </li>
			</ul>
			
		</div>
		<div class="modal-footer">
			<a href="#" class="btn addusercancel" data-dismiss="modal">取消</a>
			<input type="submit" class="btn btn-primary" var="保存" />
		</div>			
	</form>
</div><!-- addUserDiv END -->
<!-- =========================================================================================== -->
<div class="modal hide fade" id="modifyUserDiv">
	<!-- enctype="multipart/form-data">>>> 文件上传 
		onsubmit="return addUserFuntion();">>>>> 表单验证
	-->
	<!-- modify的表单提交 使用ajax，数据用 js 进行添加，页面代码简洁 -->
	<form action="/backend/modifyuser.html" method="post" enctype="multipart/form-data" onsubmit="return modifyUserFunction();">
		<div class="modal-header">
			<button type="button" class="close" data-dismiss="modal">×</button>
			<h3>修改用户信息</h3>
		</div>	
		<div class="modal-body">
			<!-- <p>Here settings can be configured...</p> -->
			<!-- 错误信息的提示 -->
			<ul id="modify_formtip"></ul>
			<input id="m_id" type="hidden" name="id"/>
			<!-- 
				a_loginCode(a:是指add的缩写)
				m_loginCode(m:是指 modify 的缩写)
				v_loginCode(v:是指view 查看的缩写)
				下面类似，不再做注释
			-->
			<ul class="topul">
				<li>
					<label>角色：</label>
					<!-- 记录角色名 -->
					<input id="m_rolename" type="hidden" name="roleName" value="" />
					<select id="m_roleid" name="roleId" style="width:100px;">
						<!-- <option value="" selected="selected">--请选择--</option> -->
						<!-- js 进行数据加载 -->
					</select>
					<span style="color:red;font-size:bold;">*</span>				
				</li><!-- 角色  END -->
				
				<li>
					<label>会员类型：</label>
					<!-- 记录会员类型名 -->
					<input id="m_selectusertypename" type="hidden" name="userTypeName" value="" />
					<select id="m_selectusertype" name="userType" style="width:100px;">
						<!-- <option value="" selected="selected">--请选择--</option> -->
						<!-- 将根据"角色"进行填充，用ajax异步加载 -->			
					</select>
					<span style="color:red;font-size:bold;">*</span>				
				</li><!-- 会员类型   END -->
				<li>
					<label>用户名：</label>
					<!-- 后台ajax进行验证      前台用正则表达式-->
					<input id="m_logincode" type="text" style="width:150px;" name="loginCode" onkeyup="value=value.replace(/[^\w\.\/]/ig,'')"/>
					<span style="color:red;font-size:bold;">*</span>				
				</li><!-- 用户名    END -->
				<li>
					<label>姓名：</label><input id="m_username" type="text" name="userName"  value="" style="width:150px;" />
					<span style="color:red;font-size:bold;">*</span>				
				</li><!-- 姓名      END -->
				<li>
					<label>性别：</label>
					<select id="m_sex" name="sex" style="width:100px;">
						<!-- js 进行动态填充 -->
					</select>
				</li><!-- 性别      END -->
				<li>
					<label>证件类型：</label>
					<!-- 记录证件类型名 -->
					<input id="m_cardtypename" type="hidden" name="cardTypeName" value="" />
					<select id="m_cardtype" name="cardType" style="width:100px;">
					</select>
					<span style="color:red;font-size:bold;">*</span>				
				</li><!-- 证件类型      END -->
				<li>
					<label>证件号码：</label><input id="m_idcard" type="text" name="idCard" value=""  style="width:150px;" onkeyup="value=value.replace(/[^\w\.\/]/ig,'')"/>
					<span style="color:red;font-size:bold;">*</span>				
				</li><!-- 证件号码      END -->
				<li>
					<label>生日：</label><input id="m_birthday" type="text" class="Wdate" size="15" name="birthday" readonly="readonly" onclick="WdatePicker();" />
				</li><!-- 生日        END -->
				<li>
					<label>收货国家：</label><input id="m_country" type="text" name="country" value=""  style="width:150px;" value="中国" />
				</li><!-- 收货国家      END -->
				<li>
					<label>联系电话：</label><input id="m_mobile" type="text" name="mobile" value="" style="width:150px;" onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')"/>
					<span style="color:red;font-size:bold;">*</span>				
				</li><!-- 联系电话      END -->
				<li>
					<label>Email：</label><input id="m_email" type="email" name="email" value="" style="width:150px;" />
					<span style="color:red;font-size:bold;">*</span>				
				</li><!-- email  END -->
				<li>
					<label>邮政编码：</label><input id="m_postcode" type="text" name="postCode" value="" style="width:150px;" onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')"/>
				</li><!-- postCode  END -->
				<li>
					<label>开户行：</label><input id="m_bankname" type="text" name="bankName" value="" style="width:150px;" />
					<span style="color:red;font-size:bold;">*</span>				
				</li><!-- bankname      END -->
				<li>
					<label>开户卡号：</label><input id="m_bankaccount" type="text" name="bankAccount" value="" style="width:150px;" onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')"/>
					<span style="color:red;font-size:bold;">*</span>				
				</li><!-- bankAccount      END -->
				<li>
					<label>开户人：</label><input id="m_accountholer" type="text" name="accountHolder" style="width:150px;" />
					<span style="color:red;font-size:bold;">*</span>				
				</li><!-- accountHoler      END -->
				<li>
					<label>推荐人：</label><input id="m_refercode" type="text" class="Wdate" size="15" name="referCode" value="${user.loginCode }" readonly="readonly"  />
				</li><!-- 推荐人        END -->
				<li>
					<label>注册时间：</label><input id="m_createtime" type="text"  name="createTime"  value="" readonly="readonly"/>
				</li><!-- 推荐人        END -->
				<li>
					<label>是否启用：</label>
					<select id="m_isstart" name="isStart" style="width:100px;">
					</select>
				</li><!-- 启用      END -->
				<li class="lastli">
				  	<label>收货地址：</label><textarea id="m_useraddress" name="userAddress"></textarea>
				</li><!-- 收货地址      END -->
			
			</ul>
			<div class="clear"></div>
			<!-- 放置图片 -->
			<ul class="downul">
				<li>
					<label>上传身份证图片：</label>
					<!-- 记录上传身份证图片的路径
						a_fileInputIDPath(a:是指add的缩写)
						m_fileInputIDPath(m:是指 modify 的缩写)
						v_fileInputIDPath(v:是指 view  查看 的缩写)
					-->
					<input type="hidden" id="m_fileInputIDPath" name="idCardPicPath" value=""/>
					<input id="m_fileInputID" name="m_fileInputID" type="file"/>
					<input type="button" id="m_uploadbtnID" value="上传" style="display: none;"/>
					<p><span style="color:red;font-weight: bold;">*注：1、正反面.<br />2、大小不得超过50k.<br />3、图片格式：jpg、png、jpeg、pneg</span></p>
					<div id="m_idPic"></div>
				</li>
			</ul>
			<ul class="downul">
				<li>
				<label>上传银行卡图片：</label>
					<input type="hidden" id="m_fileInputBankPath" name="bankPicPath" value=""/>
					<input id="m_fileInputBank" name="m_fileInputBank" type="file"/>
					<input type="button" id="m_uploadbtnBank" value="上传" style="display: none;"/>
					<p><span style="color:red;font-weight: bold;">*注：1、正反面.<br />2、大小不得超过50k.<br />3、图片格式：jpg、png、jpeg、pneg</span></p>
					<div id="m_bankPic"></div>
				 </li>
			</ul>
			
		</div>
		<div class="modal-footer">
			<a href="#" class="btn addusercancel modifyusercancel" data-dismiss="modal">取消</a>
			<input type="submit" class="btn btn-primary" var="修改" />
		</div>			
	</form>
</div><!-- modifyUserDiv END -->
<!-- =========================================================================================== -->
<div class="modal hide fade" id="viewUserDiv">
	<!-- enctype="multipart/form-data">>>> 文件上传 
		onsubmit="return addUserFuntion();">>>>> 表单验证
	-->
	<!-- modify的表单提交 使用ajax，数据用 js 进行添加，页面代码简洁 -->
	<form action="/backend/viewuser.html" method="post" enctype="multipart/form-data" onsubmit="return modifyFunction();">
		<div class="modal-header">
			<button type="button" class="close" data-dismiss="modal">×</button>
			<h3>查看用户信息</h3>
		</div>	
		<div class="modal-body">
			<!-- <p>Here settings can be configured...</p> -->
			<!-- 错误信息的提示 -->
			<!-- 
				a_loginCode(a:是指add的缩写)
				m_loginCode(m:是指 modify 的缩写)
				v_loginCode(v:是指view 查看的缩写)
				下面类似，不再做注释
			-->
			<input id="v_id" type="hidden" value="" />
			<ul class="topul">
				<li>
					<label>角色：</label><input id="v_rolename" type="text" name="roleName" value="" readonly="readonly"/>
				</li><!-- 角色  END -->
				<li>
					<label>会员类型：</label><input id="v_usertypename" type="text" name="userTypeName" value="" readonly="readonly" />
				</li><!-- 会员类型   END -->
				<li>
					<label>用户名：</label><input id="v_logincode" type="text" style="width:150px;" name="loginCode" value="" readonly="readonly" />
				</li><!-- 用户名    END -->
				<li>
					<label>姓名：</label><input id="v_username" type="text" name="userName" style="width:150px;" value="" readonly="readonly" />
				</li><!-- 姓名      END -->
				<li>
					<label>性别：</label><input id="v_sex" type="text" name="sex" style="width:150px;" value="" readonly="readonly" />
				</li><!-- 性别      END -->
				<li>
					<label>证件类型：</label><input id="v_cardtypename" type="text" name="cardTypeName" value=""  readonly="readonly" />
				</li><!-- 证件类型      END -->
				<li>
					<label>证件号码：</label><input id="v_idcard" type="text" name="idCard" style="width:150px;" readonly="readonly"/>
				</li><!-- 证件号码      END -->
				<li>
					<label>生日：</label><input id="v_birthday" type="text" class="Wdate" size="15" name="birthday" readonly="readonly" />
				</li><!-- 生日        END -->
				<li>
					<label>收货国家：</label><input id="v_country" type="text" name="country" style="width:150px;" value="中国"  readonly="readonly"/>
				</li><!-- 收货国家      END -->
				<li>
					<label>联系电话：</label><input id="v_mobile" type="text" name="mobile" style="width:150px;"  readonly="readonly"/>
				</li><!-- 联系电话      END -->
				<li>
					<label>Email：</label><input id="v_email" type="email" name="email" style="width:150px;"  readonly="readonly"/>
				</li><!-- email  END -->
				<li>
					<label>邮政编码：</label><input id="v_postcode" type="text" name="postCode" style="width:150px;"  readonly="readonly"/>
				</li><!-- postCode  END -->
				<li>
					<label>开户行：</label><input id="v_bankname" type="text" name="bankName" style="width:150px;"  readonly="readonly"/>
				</li><!-- bankname      END -->
				<li>
					<label>开户卡号：</label><input id="v_bankaccount" type="text" name="bankAccount" style="width:150px;" value="" readonly="readonly" />
				</li><!-- bankAccount      END -->
				<li>
					<label>开户人：</label><input id="v_accountholer" type="text" name="accountHolder" style="width:150px;" value=""   readonly="readonly" />
				</li><!-- accountHoler      END -->
				<li>
					<label>推荐人：</label><input id="v_refercode" type="text" class="Wdate" size="15" name="referCode" value="" readonly="readonly"  />
				</li><!-- 推荐人        END -->
				<li>
					<label>注册时间：</label><input id="v_createtime" type="text"  name="createTime"  value="" readonly="readonly"/>
				</li><!-- 推荐人        END -->
				<li>
					<label>是否启用：</label><input id="v_isstart"  type="text" name="isStart"  value="" readonly="readonly"/>
				</li><!-- 启用      END -->
				<li class="lastli">
				  	<label>收货地址：</label><textarea id="v_useraddress" name="userAddress" value="" readonly="readonly"></textarea>
				</li><!-- 收货地址      END -->
			
			</ul>
			<div class="clear"></div>
			<!-- 放置图片 -->
			<ul class="downul">
				<li>
					<label>上传身份证图片：</label>
					<input id="v_fileInputIDPath" type="hidden" name="idCardPicPath" value=""/>
					<div id="v_idPic">
						<!-- 到时候用js在 这里append一个<img /> -->
					</div>
				</li>
			</ul>
			<ul class="downul">
				<li>
				<label>上传银行卡图片：</label>
					<input id="v_fileInputBankPath" type="hidden" name="bankPicPath" value=""/>
					<div id="v_bankPic"><!-- 到时候用js在 这里append一个<img /> --></div>
				 </li>
			</ul>
			
		</div>
		<div class="modal-footer">
			<a href="#" class="btn viewusercancel" data-dismiss="modal">取消</a>
		</div>			
	</form>
</div><!-- viewUserDiv END -->


<%@include file="/WEB-INF/pages/common/foot.jsp" %>
<script type="text/javascript" src="/statics/localjs/userlist.js" ></script>
<script type="text/javascript">
/**
 * 把cardTypeList和roleList给转换成 Json
 方便js调用
 */
	//${cardTypeList}
	var cardTypeListJson = [<c:forEach items="${cardTypeList}" var="cardType">
							{"valueId":"${cardType.valueId}","valueName":"${cardType.valueName}"},
							</c:forEach>{"valueId":"over","valueName":"over"}];
	//${roleListJson}
	var roleListJson= [<c:forEach items="${roleList}" var="role">
						{"id":"${role.id}","roleName":"${role.roleName}"},
						</c:forEach>{"id":"over","roleName":"over"}];
	

</script>






























































































<!-- =================================================================================================== -->
<%-- <%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/pages/common/head.jsp" %>
<div>
	<ul class="breadcrumb">
		<li><a href="#">后台管理</a> <span class="divider">/</span></li>
		<li><a href="/backend/userlist.html">用户管理</a></li>
	</ul>
</div>


<%@include file="/WEB-INF/pages/common/foot.jsp"  --%>







<!-- ======================================================================================= -->
<!-- ======================================================================================= -->
<!-- ======================================================================================= -->
<!-- ======================================================================================= -->
<!-- ======================================================================================= -->
<!-- ======================================================================================= -->
<!-- ======================================================================================= -->
<!-- ======================================================================================= -->

<%--  <%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/pages/common/head.jsp" %> --%>
<%--
<div>
	<ul class="breadcrumb">
		<li><a href="#">后台管理</a> <span class="divider">/</span></li>
		<li><a href="/backend/userlist.html">用户管理</a></li>
	</ul>
</div>--%>

<%-- <%@include file="/WEB-INF/pages/common/foot.jsp" %>  --%>

