<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE HTML>
<html lang="en">
<head>
    <meta charset=utf-8>
    <meta name=description content="Successful registration">
    <meta name=keywords content="">
    <title>Successful registration - My watchlist</title>
    <link href="<c:url value="/resources/css/accountVerificationSuccess.css" />" rel="stylesheet">
    <link href="<c:url value="/resources/css/alertPopup.css" />" rel="stylesheet">
    <link href="<c:url value="/resources/css/loadingAnimation.css" />" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
    <script src="<c:url value="/resources/js/loginController.js" />"></script>
    <script src="<c:url value="/resources/js/alertPopupController.js" />"></script>
</head>
<body class="center">
<div class="loginForm">
    <div class="loginForm-Inner">
        <div class="loginFormText">
            <h1>Login</h1>
            <form class="formWrapLoginForm" novalidate>
                <input id="loginFormUser" placeholder="Username" type="text" required/>
                <input id="loginFormPass" placeholder="Password" type="password" required/>
                <input id="loginVerificationSubmit" class="button" value="Login"/>
            </form>
            <span toggle="#loginFormPass" class="fa fa-fw fa-eye togglePassword"></span>
        </div>
        <!--        hidden message box-->
        <div class="loginFormMessage">Your email address has successfully been verified.
            Please log in to complete the registration process
        </div>
        <!--        hidden loading animation-->
        <div class="loaderLoginBox">
            <div class="loader__spinner">
                <div class="loader__bar"></div>
                <div class="loader__bar"></div>
                <div class="loader__bar"></div>
                <div class="loader__bar"></div>
                <div class="loader__bar"></div>
            </div>
        </div>
    </div>
</div>
</body>
<!--Hidden alert-->
<div class="notify">
    <span id="notifyType" class=""></span>
</div>
</html>