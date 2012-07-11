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
        
    	<!-- Begin Menu de navega��o -->
		<div class="Menu">
            <ul>
				<li><a href="#Home" class="ActiveMenuButton"><span>Home</span></a></li> 
				<li><a href="#Menu" class="MenuButton"><span>Card�pio</span></a></li> 
				<li><a href="#" class="MenuButton"><span>Archive</span></a></li> 
				<li><a href="#" class="MenuButton"><span>About</span></a></li>
			</ul>
        </div>
    	<!-- End Menu de navega��o -->
		
		<div class="Columns"><div class="Column1"><div class="BlockBorder"><div class="BlockBL"><div></div></div>
		<div class="BlockBR"><div></div></div><div class="BlockTL"></div><div class="BlockTR"><div></div></div>
		<div class="BlockT"></div><div class="BlockR"><div></div></div><div class="BlockB"><div></div></div>
		<div class="BlockL"></div><div class="BlockC"></div>
	
		<!-- Begin Caixa de usu�rio/login -->	
		<div class="Block">
            <span class="BlockHeader"><span id="userAreaTitle">:: Login</span></span>
            <div class="BlockContentBorder" id="userArea">
            <c:choose>
            	<c:when test="${not empty SessionOrder }">
            		<jsp:include page="order/ShopCar.jsp"/>
            	</c:when>
            	<c:otherwise>
					<form>
						<table>
							<tr>
								<td><label for="login_user">Usu�rio</label></td>
								<td><input type="text" style="width:120px" id="login_user" name="user" /></td>
							</tr>
							<tr>
								<td><label for="login_pwd">Senha</label></td>
								<td><input type="password" style="width:120px" id="login_pwd" name="password" /></td>
							</tr>
						</table>
						<span class="ButtonInput"><span><input type="button" value="Login" /></span></span>
					</form><div style="text-align: right;"><a href="#" class="debugPageLoader"  title="signup.jsp">Criar conta</a></div>
				</c:otherwise>
			</c:choose>
            </div>
        </div>
		<!-- End Caixa de usu�rio/login -->
		
		</div>
        <div class="BlockBorder"><div class="BlockBL"><div></div></div><div class="BlockBR"><div></div></div>
        <div class="BlockTL"></div><div class="BlockTR"><div></div></div><div class="BlockT"></div>
        <div class="BlockR"><div></div></div><div class="BlockB"><div></div></div>
        <div class="BlockL"></div><div class="BlockC"></div>
        
		<!-- Begin links -->
        <div class="Block">
            <span class="BlockHeader"><span>:: Links</span></span>
            <div class="BlockContentBorder">
                <ul>
                    <li><a href="#">Test Link 1</a></li>
                    <li><a href="#">Test Link 2</a></li>
                    <li><a href="#">Test Link 3</a></li>
                    <li><a href="#">Test Link 4</a></li>
                    <li><a href="#">Test Link 5</a></li>
                </ul>
            </div>
        </div>
		<!-- End links -->
		
		</div>
        </div>
        
        <!-- Begin Conte�do -->
        <div class="MainColumn">
	        <div class="Article" id="MainArea"></div>
        </div>
        <!-- End Conte�do -->
        </div>
        
        <!-- Begin Rodape -->
        <div class="Footer">
           Sei la. Rodape - <a href="#">Contato?</a>
        </div>
        <!-- End Rodape -->                

    </div></div>
    </div>
</body>
</html>