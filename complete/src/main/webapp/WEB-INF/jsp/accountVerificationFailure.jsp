<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE HTML>
<html lang="en">
<head>
    <meta charset=utf-8>
    <meta name=description content="Registration failure">
    <meta name=keywords content="">
    <title>Registration failure - My watchlist</title>
    <link href="<c:url value="/resources/css/accountVerificationFailure.css" />" rel="stylesheet">
    <link href="<c:url value="/resources/css/alertPopup.css" />" rel="stylesheet">
    <link href="<c:url value="/resources/css/loadingAnimation.css" />" rel="stylesheet">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
    <script src="<c:url value="/resources/js/loginController.js" />"></script>
    <script src="<c:url value="/resources/js/alertPopupController.js" />"></script>
</head>
<body class="center">
<div class="loginForm">
    <div class="loginForm-Inner">
        <div class="loginFormText">
            <h1>Registration failed</h1>
            <form class="formWrapLoginForm" novalidate>
                <input id="requestLinkEmail" placeholder="example@provider.com" type="text" required/>
                <input id="requestLink" class="button" value="Request link"/>
            </form>
        </div>
        <!--        hidden message box-->
        <div class="loginFormMessage">Your email address could not be verified. Perhaps your link has expired.
            Submit your email address to request a new link.</div>
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