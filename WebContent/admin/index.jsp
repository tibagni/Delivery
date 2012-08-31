<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Web Site Name</title>
    <link rel="stylesheet" href="../css/style.css" />
    
    <!-- Arquivo que define os paths para o back-end -->
    <script type="text/javascript" src="../javascript/back-end/back-end-paths.js"></script>
    
    <script type="text/javascript" src="../javascript/jquery.js"></script>
    <script type="text/javascript" src="../javascript/jquery.simplemodal.js"></script>
	<script type="text/javascript" src="../javascript/common/Util.js"></script>
	<script type="text/javascript" src="../javascript/common/Navigation.js"></script>
	<script type="text/javascript" src="../javascript/common/Menu.js"></script>
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
		<c:if test="${not empty AdminSession }">
            <a style="float: right" href="Login?action=logout">Sair</a>
            <ul>
				<li><a href="#Home" class="ActiveMenuButton"><span>Home</span></a></li> 
				<li><a href="#MenuEditor" class="MenuButton"><span>Editor de cardápio</span></a></li> 
				<li><a href="#OrderManager" class="MenuButton"><span>Gerenciador de pedidos 
				    <b id="orderManagerLink" style="color: #FF0000;"></b></span></a></li>
			</ul>
	    </c:if>
        </div>
    	<!-- End Menu de navegação -->
		
		<div class="Columns">
        
        <!-- Begin Conteúdo -->
        <div class="MainColumn">
	        <div class="Article" id="MainArea" title="">
                <c:choose>
                    <c:when test="${empty AdminSession}">
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
        <c:if test="${not empty AdminSession }">
           <a href="#Home" class="AjaxLink">Home</a> | <a href="#MenuEditor" class="AjaxLink">Editor de cardápio</a> 
           | <a href="#OrderManager" class="AjaxLink">Gerenciador de pedidos</a>
        </c:if>
        </div>
        <!-- End Rodape -->                

    </div></div>
    </div>
</body>
</html>