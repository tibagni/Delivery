<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<META HTTP-EQUIV="REFRESH" CONTENT="1; URL=/Delivery">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

<title>Aguarde enquanto o ${action } � finalizado...</title>

    <link rel="stylesheet" href="css/style.css" />
</head>
<body>
<h1>Aguarde enquanto o ${action } � finalizado!</h1>
<p style="text-align: center;">
    <img src="images/loading.gif" />
    <br /><br />
    <a href="/Delivery">Continuar!</a>
</p>
</body>
</html>