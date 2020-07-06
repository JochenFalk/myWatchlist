<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE HTML>
<html lang="en">
<head>
    <meta charset=utf-8>
    <meta name=test content="react test">
    <meta name=test content="">
    <title>react test - My watchlist</title>
    <link href="<c:url value="/resources/css/accountVerificationSuccess.css" />" rel="stylesheet">
    <link href="<c:url value="/resources/css/alertPopup.css" />" rel="stylesheet">
    <link href="<c:url value="/resources/css/loadingAnimation.css" />" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
    <script type="text/javascript" src="https://unpkg.com/react@16/umd/react.development.js" crossorigin></script>
    <script type="text/javascript" src="https://unpkg.com/react-dom@16/umd/react-dom.development.js" crossorigin></script>
    <script src="https://unpkg.com/@babel/standalone/babel.min.js"></script>
    <script type="text/babel" src="<c:url value="/resources/js/reactTest.js" />"></script>
</head>
<body class="center">
<div class="loginForm">
    <div class="loginForm-Inner">
        <div id="ReactForm" class="loginFormText">
<%--            <h1>Login</h1>--%>
<%--            <form class="formWrapLoginForm" novalidate>--%>
<%--                <input id="loginFormUser" placeholder="Username" type="text" required/>--%>
<%--                <input id="loginFormPass" placeholder="Password" type="password" required/>--%>
<%--                <input id="loginVerificationSubmit" class="button" value="Login"/>--%>
<%--            </form>--%>
<%--            <span toggle="#loginFormPass" class="fa fa-fw fa-eye togglePassword"></span>--%>
        </div>
        <!--        hidden message box-->
<%--        <div class="loginFormMessage">--%>
<%--        </div>--%>
</html>