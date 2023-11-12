<%@page contentType = "text/html;charset=utf-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<title>Document</title>
	<link rel="stylesheet" type="text/css" href="css\common.css">
	<script type="text/javascript" src="js\js1.js"></script>
</head>
<body>
<%--JSTL混合书写判断 判断c是否存在 对应PaintingController 存在追加链接 c:set保存数据--%>
<c:if test="${param.c != null}">
	<c:set var = "categoryParam" value="&c=${param.c}"></c:set>
</c:if>
<c:if test="${param.c == null}">
	<c:set var = "categoryParam" value=""></c:set>
</c:if>
<div class="header">
	<div class="logo">
		<img src="image\logo.png">
	</div>
	<div class="menu"   onclick="show_menu()" onmouseleave="show_menu1()">
		<div class="menu_title" ><a href="###">内容分类</a></div>
		<ul id="title">
			<li><a href="/mgallery/page?c=1">现实主义</a></li>
			<li><a href="/mgallery/page?c=2">抽象主义</a></li>
		</ul>
	</div>
	<div class="auth">
		<ul>
			<li><a href="#">登录</a></li>
			<li><a href="#">注册</a></li>
		</ul>
	</div>
</div>
<div class="content">
	<div class="banner">
		<img src="image/welcome.png" class="banner-img">
	</div>
	<div class="img-content">
		<ul>
			<c:forEach items="${pageModel.pageData}" var="painting">
				<li>
					<img src="${painting.preview}" class="img-li">
					<div class="info">
						<h3>${painting.pname}</h3>
						<p>
								${painting.description}
						</p>
						<div class="img-btn">
							<div class="price"><fmt:formatNumber pattern="￥0.00" value="${painting.price}"></fmt:formatNumber></div>
							<a href="#" class="cart">
								<div class="btn">
									<img src="image/cart.svg">
								</div>
							</a>
						</div>
					</div>
				</li>
			</c:forEach>
		</ul>
	</div>
	<div class="page-nav">
		<ul>
			<li><a href="/mgallery/page?p=1${categoryParam}">首页</a></li> <!--当前页减一就是上一页 否则就是1-->
			<li><a href="/mgallery/page?p=${pageModel.hasPreviousPage?pageModel.page-1:1}${categoryParam}">上一页</a></li>
			<c:forEach begin="1" end="${pageModel.totalPages}" var="pno" step="1">
				<li><span ${pno==pageModel.page?"class='first-page'":""}> <!--选中的页才有⚪圈圈 三目运算符不满足产生空字符串-->
                     <%--  c不存在，则href="/mgallery/page?p=1"  c存在，测href="/mgallery/page?p=1&c=1"  --%>
					<a href="/mgallery/page?p=${pno}${categoryParam}">
							${pno}
					</a></span></li>
			</c:forEach>
			<li><a href="/mgallery/page?p=${pageModel.hasNextPage?pageModel.page+1:pageModel.totalPages}${categoryParam}">下一页</a></li>
			<li><a href="/mgallery/page?p=${pageModel.totalPages}${categoryParam}">尾页</a></li>
		</ul>
	</div>
</div>
<div class="footer">
	<p><span>P_luminary</span>©2023.10.3 POWERED BY GITHUB.COM</p>
</div>
</body>
</html>