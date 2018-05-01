<%--
    Document   : Header
--%>

<%@page contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <link href="${pageContext.request.contextPath}/resources/bootstrap/css/bootstrap.css" rel="stylesheet">
        <link href="${pageContext.request.contextPath}/resources/bootstrap/css/bootstrap-theme.css" rel="stylesheet">
        <link href="${pageContext.request.contextPath}/resources/jquery/jquery-ui.css" rel="stylesheet">
        <link href="${pageContext.request.contextPath}/resources/font-awesome/css/font-awesome.css" rel="stylesheet">
        <link href="${pageContext.request.contextPath}/resources/css/main.css" rel="stylesheet">

        <script src="${pageContext.request.contextPath}/resources/jquery/jquery.js"></script>
        <script src="${pageContext.request.contextPath}/resources/bootstrap/js/bootstrap.js"></script>
        <script src="${pageContext.request.contextPath}/resources/jquery/jquery-ui.js"></script>


        <script>var ctx = "${pageContext.request.contextPath}";</script>

        <title>Exam Preparation</title>
    </head>
    <body>
        <div class="container" style="background-color: #FFF; border-radius: 8px;">

            <div class="row" style="background: #4DB969; padding: 1em .5em;">
                <a href="welcome" style="text-decoration: none;">
                    <h3 style="font-weight: bold; color: #FFF; margin: 0;">Bangladesh Exam Preparation</h3>
                </a>
            </div>
            <c:if test="${sessionScope.loggedUser != null}">
                <%@ include file="menu.jsp" %>
            </c:if>
            <div class="row" >
