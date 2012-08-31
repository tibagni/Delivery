<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Web Site Name</title>
    <link rel="stylesheet" href="css/style.css" />
    
    <!-- Arquivo que define os paths para o front-end -->
    <script type="text/javascript" src="javascript/front-end/front-end-paths.js"></script>
    
    <script type="text/javascript" src="javascript/jquery.js"></script>
    <script type="text/javascript" src="javascript/jquery.simplemodal.js"></script>
	<script type="text/javascript" src="javascript/common/Util.js"></script>
	<script type="text/javascript" src="javascript/common/Navigation.js"></script>
	<script type="text/javascript" src="javascript/common/Menu.js"></script>
</head>
<body>
    <div class="BackgroundGradient"></div>
    <div class="BodyContent">
    
    <div class="BorderBorder"><div class="BorderBL"><div></div></div><div class="BorderBR"><div></div></div>
    <div class="BorderTL"></div><div class="BorderTR"><div></div></div><div class="BorderT"></div>
    <div class="BorderR"><div></div></div><div class="BorderB"><div></div></div><div class="BorderL"></div>
    <div class="BorderC"></div>
    
    <div class="Border">
    	<!-- Begin Header -->
        <div class="Header">
          <div class="HeaderTitle">
          <!-- topo -->
          </div>
        </div>
    	<!-- End Header -->
        
    	<!-- Begin Menu de navegação -->
		<div class="Menu">
            <ul>
				<li><a href="#Home" class="ActiveMenuButton"><span>Home</span></a></li> 
				<li><a href="#Menu" class="MenuButton"><span>Cardápio</span></a></li>
				<c:if test="${not empty UserSession }">
					<li><a href="#" class="MenuButton"><span>Meus pedidos</span></a></li> 
					<li><a href="#" class="MenuButton"><span>Recomendações</span></a></li>
				</c:if> 
			</ul>
        </div>
    	<!-- End Menu de navegação -->
		
		<div class="Columns"><div class="Column1"><div class="BlockBorder"><div class="BlockBL"><div></div></div>
		<div class="BlockBR"><div></div></div><div class="BlockTL"></div><div class="BlockTR"><div></div></div>
		<div class="BlockT"></div><div class="BlockR"><div></div></div><div class="BlockB"><div></div></div>
		<div class="BlockL"></div><div class="BlockC"></div>
	
		<!-- Begin Caixa de usuário/login -->	
		<div class="Block">
            <span class="BlockHeader"><span id="userAreaTitle">:: Login</span></span>
            <div class="BlockContentBorder" id="userArea">
            <c:choose>
                <c:when test="${not empty UserSession }">
                    <jsp:include page="userInfo.jsp"/>
                </c:when>
            	<c:otherwise>
					<form method="post" action="Login">
						<table>
							<tr>
								<td><label for="login_user">Usuário</label></td>
								<td><input type="text" style="width:120px" id="login_user" name="user" /></td>
							</tr>
							<tr>
								<td><label for="login_pwd">Senha</label></td>
								<td><input type="password" style="width:120px" id="login_pwd" name="password" /></td>
							</tr>
						</table>
						<span class="ButtonInput"><span><input type="submit" value="Login" /></span></span>
					</form><div style="text-align: right;"><a href="#SignUp" class="AjaxLink" >Criar conta</a></div>
				</c:otherwise>
			</c:choose>
            </div>
        </div>
		<!-- End Caixa de usuário/login -->
		
		</div>
        <div class="BlockBorder"><div class="BlockBL"><div></div></div><div class="BlockBR"><div></div></div>
        <div class="BlockTL"></div><div class="BlockTR"><div></div></div><div class="BlockT"></div>
        <div class="BlockR"><div></div></div><div class="BlockB"><div></div></div>
        <div class="BlockL"></div><div class="BlockC"></div>
        
		<!-- Begin ShopCar -->
		<!-- Só mostra o carrinho se o usuario estiver logado! -->
		<c:if test="${not empty UserSession }">
	        <div class="Block">
	            <span class="BlockHeader"><span>:: Pedido</span></span>
	            <div class="BlockContentBorder" id="shopCarArea">
                    <jsp:include page="order/ShopCar.jsp"/>
	            </div>
	        </div>
        </c:if>
		<!-- End ShopCar -->
		
		</div>
        </div>
        
        <!-- Begin Conteúdo -->
        <div class="MainColumn">
	        <div class="Article" id="MainArea" title="">
	            <c:choose>
	                <c:when test="${defaultPage == 'login'}">
                        <c:remove var="defaultPage"/>
	                    <jsp:include page="Login.jsp"/>
	                </c:when>
	                <c:otherwise>
                        <jsp:include page="home.jsp"/>
	                </c:otherwise>
	            </c:choose>
	        </div>
        </div>
        <!-- End Conteúdo -->
        </div>
        
        <!-- Begin Rodape -->
        <div class="Footer">
           <a href="#Home" class="AjaxLink">Home</a> | <a href="#Menu" class="AjaxLink">Cardápio</a> 
           <c:if test="${not empty UserSession }">
            | <a href="#" class="AjaxLink">Meus pedidos</a> | <a href="#" class="AjaxLink">Recomendações</a>
            </c:if>
        </div>
        <!-- End Rodape -->                

    </div></div>
    </div>
</body>
</html>