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
								<fmt:formatDate value="${user.createTime }" pattern="yyyy-MM-dd" ></fmt:formatDate>
							</td>
							<td class="center">
								<a class="btn btn-success" href="#">
									<i class="icon-zoom-in icon-white"></i>  
									查看                                            
								</a>
								<a class="btn btn-info" href="#">
									<i class="icon-edit icon-white"></i>  
									修改                                            
								</a>
								<a class="btn btn-danger" href="#">
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
							<li><a href="/backend/userlist.html?currentpage=1&&s_loginCode=${s_loginCode }&&s_referCode=${s_referCode}&&s_roleId=${roleId}&&s_isStart=${s_isStart}" title="首页">首页</a></li>
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

<!-- 模式窗口 -->
<div class="modal hide fade" id="addUserDiv">
	<!-- enctype="multipart/form-data">>>> 文件上传 
		onsubmit="return addUserFuntion();">>>>> 表单验证
	-->
	<form action="/backend/adduser.html" method="post" enctype="multipart/form-data" onsubmit="return addUserFuntion();">
		<div class="modal-header">
			<button type="button" class="close" data-dismiss="modal">×</button>
			<h3>添加用户信息</h3>
		</div>	
		<div class="modal-body">
			<!-- <p>Here settings can be configured...</p> -->
			<!-- 错误信息的提示 -->
			<ul id="add_formTip"></ul>
			
			<ul class="topul">
				<li>
					<label>角色：</label>
					
					
					
				</li>
			
			</ul>
			<div class="clear"></div>
			<!-- 放置图片 -->
			<ul class="downul">
				<li>
					<label>上传身份证图片：</label>
				</li>
			</ul>
			
		</div>
		<div class="modal-footer">
			<a href="#" class="btn" data-dismiss="modal">取消</a>
			<input type="submit" class="btn btn-primary" var="保存" />
		</div>			
	</form>
</div>
<script type="text/javascript" src="/statics/localjs/userlist.js" />

<%@include file="/WEB-INF/pages/common/foot.jsp" %>








							
							