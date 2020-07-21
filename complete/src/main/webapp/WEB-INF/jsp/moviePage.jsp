<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset=utf-8>
    <meta name=description content="Create a watchlist of your favourite movies">
    <meta name=keywords content="movies, favourite, watch, watchlist, create, browse, list, free, account">
    <title>Movie - My watchlist</title>
    <link href="<c:url value="/resources/css/commonStyles.css" />" rel="stylesheet"> <%--keep at the top so other style sheets can override it--%>
    <link href="<c:url value="/resources/css/moviePage.css" />" rel="stylesheet">
    <link href="<c:url value="/resources/css/moviePageMenu.css" />" rel="stylesheet">
    <link href="<c:url value="/resources/css/searchBox.css" />" rel="stylesheet">
    <link href="<c:url value="/resources/css/loginForm.css" />" rel="stylesheet">
    <link href="<c:url value="/resources/css/accountForm.css" />" rel="stylesheet">
    <link href="<c:url value="/resources/css/listForm.css" />" rel="stylesheet">
    <link href="<c:url value="/resources/css/alertPopup.css" />" rel="stylesheet">
    <link href="<c:url value="/resources/css/loadingAnimation.css" />" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
    <script src="<c:url value="/resources/js/moviePageController.js" />"></script>
    <script src="<c:url value="/resources/js/searchController.js" />"></script>
    <script src="<c:url value="/resources/js/loginController.js" />"></script>
    <script src="<c:url value="/resources/js/accountController.js" />"></script>
    <script src="<c:url value="/resources/js/listController.js" />"></script>
    <script src="<c:url value="/resources/js/alertPopupController.js" />"></script>
    <script src="<c:url value="/resources/js/cookieController.js" />"></script>
    <script src="<c:url value="/resources/js/animationController.js" />"></script>
</head>
<body class="center">
<div id="content" class="hide un-hide">
    <nav class="nav">
        <div class="logo">
            <a href="listPage"><img
                    src="../../resources/images/logo.png" alt="Home"/></a>
        </div>
        <div class="navButtons"><a href="homePage">Home</a></div>
        <div id="list" class="navButtons"><a href="javascript:null;">lists</a></div>
        <div id="Search" class="navButtons"><a href="javascript:null;">Search</a></div>
        <div id="login" class="navButtons"><a href="javascript:null;">Login</a></div>
        <div id="account" class="hide navButtons"><a href="javascript:null;">account</a></div>
    </nav>
    <header class="header">
        <!--header content-->
        <img id="banner" class="banner" src="" alt="Movie Poster"/>
        <div class="video-container">
            <div class="video">
                <iframe id="youtube_player" class="yt_player_iframe" src="" allowfullscreen="true"
                        allowscriptaccess="always" frameborder="0"></iframe>
            </div>
        </div>
        <!--header controls-->
        <div id="button-container">
            <div id="buttons">
                <img id="dropDownButton" class="item"
                     src="../../resources/images/arrow-150x150.png" title="Toggle banner" alt="Show banner">
                <img id="playButton" toggle="#banner" class="play-pause item" title="Play trailer" alt="Play trailer"
                     src="../../resources/images/playButton-150x150.png">
                <img id="pauseButton" class="item" title="Pause trailer" alt="Pause trailer"
                     src="../../resources/images/pauseButton-150x150.png">
                <img id="refreshButton" class="item" src="../../resources/images/refresh-150x150.png"
                     title="Reload trailer" alt="Reload trailer">
            </div>
        </div>
    </header>
    <main class="main">
        <div>
            <h2>
                <span class="title">Title could not be found!</span>
                <img src="../../resources/images/yellow_star.png" height="15" width="15" alt=""/>
                <span class="rating">0</span>
                <span id="playText" toggle="#banner" class="play-pause">Play trailer</span>
            </h2>
            <h2>Plot</h2>
            <p class="plot">
                Oeps! It seems the plot could not be loaded :(
            </p>
            <h3>Directed by</h3>
            <p>
                <a class="crewDirector" href="javascript:null;"></a>
                <span> & </span>
                <a class="crewDirector" href="javascript:null;"></a>
            </p>
            <h3>Written by</h3>
            <p>
                <a class="crewWriter" href="javascript:null;"></a>
                <span> & </span>
                <a class="crewWriter" href="javascript:null;"></a>
            </p>
        </div>
    </main>
    <aside class="aside">
        <a class="poster"><img src="" alt="Movie poster"/></a>
        <img class="poster-overlay" src="../../resources/images/plus-150x150.png" alt="Add movie to list">
    </aside>
    <div class="castInfo">
        <h3>Movie Cast</h3>
        <p>
            <a class="castName" href="javascript:null;"></a>
            <br><a class="castName" href="javascript:null;"></a>
            <br><a class="castName" href="javascript:null;"></a>
            <br><a class="castName" href="javascript:null;"></a>
            <br><a class="castName" href="javascript:null;"></a>
            <br><a class="castName" href="javascript:null;"></a>
            <br><a class="castName" href="javascript:null;"></a>
            <br><a class="castName" href="javascript:null;"></a>
            <br><a class="castName" href="javascript:null;"></a>
            <br><a class="castName" href="javascript:null;"></a>
            <br><a class="castName" href="javascript:null;"></a>
            <br><a class="castName" href="javascript:null;"></a>
    </div>
    <div class="cast-wrapper">
        <a class="cast hide">
            <img class="castPoster" src="" alt=""/>
            <div class="castCharacterName"></div>
        </a>
        <a class="cast hide">
            <img class="castPoster" src="" alt=""/>
            <div class="castCharacterName"></div>
        </a>
        <a class="cast hide">
            <img class="castPoster" src="" alt=""/>
            <div class="castCharacterName"></div>
        </a>
        <a class="cast hide">
            <img class="castPoster" src="" alt=""/>
            <div class="castCharacterName"></div>
        </a>
        <a class="cast hide">
            <img class="castPoster" src="" alt=""/>
            <div class="castCharacterName"></div>
        </a>
        <a class="cast hide">
            <img class="castPoster" src="" alt=""/>
            <div class="castCharacterName"></div>
        </a>
        <a class="cast hide">
            <img class="castPoster" src="" alt=""/>
            <div class="castCharacterName"></div>
        </a>
        <a class="cast hide">
            <img class="castPoster" src="" alt=""/>
            <div class="castCharacterName"></div>
        </a>
        <a class="cast hide">
            <img class="castPoster" src="" alt=""/>
            <div class="castCharacterName"></div>
        </a>
        <a class="cast hide">
            <img class="castPoster" src="" alt=""/>
            <div class="castCharacterName"></div>
        </a>
        <a class="cast hide">
            <img class="castPoster" src="" alt=""/>
            <div class="castCharacterName"></div>
        </a>
        <a class="cast hide">
            <img class="castPoster" src="" alt=""/>
            <div class="castCharacterName"></div>
        </a>
    </div>
    <div class="similar-banner">
        <h3 class="similar-banner-text">Browse similar movies</h3>
    </div>
    <similar class="similar">
        <div onclick="similarNextThumb(1)" class="prevSlide">
            <span>&#10095;</span>
        </div>
        <div class="thumbSlides">
            <div id="similar-templateThumb" class="hide">
                <a href="javascript:null;"><img
                        src="../../resources/images/default-poster-332x500-borders.png" alt=""/></a>
            </div>
            <div>
                <div id="similar-infoThumb" class="similarThumb infoThumb"><span>This is an info box</span></div>
            </div>
        </div>
        <div onclick="similarNextThumb(-1)" class="nextSlide">
            <span>&#10094;</span>
        </div>
<%--                <div id="similarLabel-container">--%>
        <%--                    <div class="slideShowText scroll-left">Browse similar movies</div>--%>
        <%--                </div>--%>
    </similar>
</div>
<!--Hidden search box-->
<div class="searchBox">
    <div class="searchBox-Inner">
        <div class="searchBoxPoster">
            <img id="searchPoster" src="../../resources/images/default-poster-332x500-borders.png" alt="">
        </div>
        <div class="searchBoxText">
            <h1>Search new movies</h1>
            <form class="formWrap" action="#" novalidate>
                <input id="searchBoxTitle" placeholder="Movie title" type="text" required/>
                <input id="searchBoxYear" placeholder="Movie year (optional)" type="text"/>
                <input id="searchBoxSearchButton" class="button" value="Search"/>
                <input id="searchBoxNextButton" class="button" value="Next result"/>
            </form>
            <img id="closeSearchBox" src="../../resources/images/close-150x150.png" alt="">
        </div>
        <div class="replyText">
            <h1 id="replyTitle">No movie was found :(</h1>
            <p id="replyOverview"></p>
            <form class="formWrap" action="#">
                <input id="replyTextAddButton" class="button" value="Add movie"/>
            </form>
            <img id="refreshSearchBox" src="../../resources/images/refresh-150x150.png" alt="">
        </div>
    </div>
</div>
<!--Hidden login form-->
<div class="loginForm">
    <div class="loginForm-Inner">
        <div class="loginFormText">
            <h1>Login</h1>
            <form class="formWrapLoginForm" novalidate>
                <input id="loginFormUser" placeholder="Username" type="text" required/>
                <input id="loginFormPass" placeholder="Password" type="password" required/>
                <input id="loginFormSubmit" class="button" value="Login"/>
                <input id="loginFormCreate" class="button" value="Create account"/>
            </form>
            <img id="closeLoginForm" src="../../resources/images/close-150x150.png" alt="">
            <span toggle="#loginFormPass" class="fa fa-fw fa-eye togglePassword"></span>
        </div>
        <div class="LoginFormReplyText">
            <h1>Account creation</h1>
            <form class="formWrapLoginForm" novalidate>
                <input id="accountCreationUser" placeholder="Username" type="text" required/>
                <input id="accountCreationPass" placeholder="Password" type="password" required/>
                <input id="accountCreationEmail" placeholder="example@provider.com" type="text" required/>
                <input id="accountCreationSubmit" class="button" value="Create account"/>
            </form>
            <img id="refreshLoginForm" src="../../resources/images/refresh-150x150.png" alt="">
            <span toggle="#accountCreationPass" class="fa fa-fw fa-eye togglePassword"></span>
            <div id="usernameTooltip" class="tooltip">
                <img src="../../resources/images/questionmark-150x150.png" alt="">
                <span class="tooltipText">
                    <span>Length: 6 to 12 characters</span><br>
                    <span>Must contain 1 letter</span><br>
                    <span>No consecutive "." or "_"</span><br>
                    <span>No "." followed by "_" or visa versa</span><br>
                    <span>No spaces</span></span>
            </div>
            <div id="passwordTooltip" class="tooltip">
                <img src="../../resources/images/questionmark-150x150.png" alt="">
                <span class="tooltipText">
                    <span>Length: 6 to 12 characters</span><br>
                    <span>Only alphanumerical characters</span><br>
                    <span>Must contain 1 capital letter</span><br>
                    <span>Must contain 1 digit</span><br>
                    <span>No spaces</span></span>
            </div>
        </div>
        <!--        hidden message box-->
        <div class="loginFormMessage"></div>
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
<!--Hidden account form-->
<div class="accountForm">
    <div class="accountForm-Inner">
        <div class="accountFormText">
            <h1>Account</h1>
            <form class="formWrapAccountForm" novalidate>
                <input id="accountFormSubmit" class="button" value="Log out"/>
                <input id="accountFormDelete" class="button" value="Delete account"/>
                <input id="adminPageButton" class="button" value="Access admin page"/>
            </form>
            <img id="closeAccountForm" src="../../resources/images/close-150x150.png" alt="">
        </div>
        <!--        hidden message box-->
        <div class="accountFormMessage">...</div>
        <!--        hidden loading animation-->
        <div class="loaderAccountBox">
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
<!--Hidden list form-->
<div class="listForm">
    <div class="listForm-Inner">
        <div class="listFormText">
            <h1>Manage watchlists</h1>
            <form class="formWrapListForm" novalidate>
                <div id="listFormList" class="custom-select-wrapper">
                    <div class="custom-select">
                        <div class="custom-select__trigger"><span id="listValue">Select a list</span>
                            <div class="arrow"></div>
                        </div>
                        <div class="custom-options">
                            <span class="custom-option selected" data-value="Select a list">Select a list</span>
                            <span id="templateOption" class="hide custom-option" data-value=""></span>
                        </div>
                    </div>
                </div>
                <input id="listFormSubmit" class="button" value="Load list"/>
                <input id="listFormDelete" class="button" value="Delete list"/>
                <input id="listFormCreate" class="button" value="Create list"/>
            </form>
        </div>
        <div class="listFormReplyText">
            <h1 class="listTitle">List creation</h1>
            <form class="formWrapListForm" novalidate>
                <input id="listCreationName" placeholder="Name" type="text" required/>
                <textarea id="listCreationDescription" placeholder="Description" type="text"></textarea>
                <input id="listCreationSubmit" class="button" value="Create watchlist"/>
                <input id="listImportSubmit" class="button" value="Import watchlist"/>
            </form>
        </div>
        <img id="closeListForm" src="../../resources/images/close-150x150.png" alt="">
        <img id="refreshListForm" src="../../resources/images/refresh-150x150.png" alt="">
        <!--        hidden message box-->
        <div class="listFormMessage"></div>
        <!--        hidden loading animation-->
        <div class="loaderListBox">
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
<!--Hidden alert-->
<div class="notify">
    <span id="notifyType" class=""></span>
</div>
<!--        hidden loading animation-->
<div class="loaderPage">
    <div class="loader__spinner">
        <div class="loader__bar"></div>
        <div class="loader__bar"></div>
        <div class="loader__bar"></div>
        <div class="loader__bar"></div>
        <div class="loader__bar"></div>
    </div>
</div>
</body>
</html>