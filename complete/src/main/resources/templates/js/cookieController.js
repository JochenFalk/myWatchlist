let publicList = [];
let pushList = [];

// retrieve and re-build movie list from cookie
function buildMovieList() {
    let movieList = getPublicList();
    if (movieList == null) {
        setCookie(listName, '', NO_END_DATE);
        alertSuccess("Welcome! Try making your new list", longTimeOut);
    }
    else if (movieList.length == 0) {
        alertSuccess("Welcome! Try making your new list", longTimeOut);
    } else {
        for (let i = 0; i < movieList.length; i++) {
            publicList.push(movieList[i]);
            movieTitle = movieList[i].title;
            movieYear = movieList[i].year;
            callSearch(movieTitle, movieYear, movieId, 'update', 'search');
        }
    }
}

function buildMoviePage() {
    let movieList = getPushList();
    if (movieList == null) {
        alertFailure("Oeps! There wa a problem loading the page. Please refresh and try again", longTimeOut);
    }
    else if (movieList.length == 0) {
        alertFailure("Oeps! There wa a problem loading the page. Please refresh and try again", longTimeOut);
    } else {
        movieTitle = movieList[0].title;
        movieYear = movieList[0].year;
        movieId = movieList[0].id;
        callSearch(movieTitle, movieYear, movieId, 'build', 'cast');
        callSearch(movieTitle, movieYear, movieId, 'build', 'crew');
        callSearch(movieTitle, movieYear, movieId, 'build', 'search');
    }
}

function setCookie(key, value, date) {
    let publicCookie = JSON.stringify(value);
    document.cookie = key + '=' + publicCookie + ';expires=' + date + ';';
}

function setSessionCookie(key, value) {
    let publicCookie = JSON.stringify(value);
    document.cookie = key + '=' + publicCookie + ';';
}

function getCookie(name) {
    let searchKey = name + '=';
    let decodedCookie = decodeURIComponent(document.cookie);
    let cookieJar = decodedCookie.split(';');
    for (let i = 0; i < cookieJar.length; i++) {
        let cookie = cookieJar[i];
        while (cookie.charAt(0) == ' ') {
            cookie = cookie.substring(1);
        }
        if (cookie.indexOf(searchKey) == 0) {
            return cookie.substring(searchKey.length, cookie.length);
        }
    }
    return null;
}

function deleteCookie(name) {
    // publicList = [];
    setCookie(name, '', -1);
}

function getPublicList() {
    let thisCookie = getCookie(listName);
    return JSON.parse(thisCookie);
}

function getPushList() {
    let thisCookie = getCookie(pushMovie);
    return JSON.parse(thisCookie);
}