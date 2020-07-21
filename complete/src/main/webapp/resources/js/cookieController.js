let publicListItems = [];
let privateListItems = [];
let systemListItems = [];

let promisePushedMovie;

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
        while (cookie.charAt(0) === ' ') {
            cookie = cookie.substring(1);
        }
        if (cookie.indexOf(searchKey) === 0) {
            return cookie.substring(searchKey.length, cookie.length);
        }
    }
    return null;
}

function deleteCookie(name) {
    setCookie(name, '', -1);
}

function getPublicListItems() {
    let thisCookie = getCookie("movieList");
    if (thisCookie != null) {
        return JSON.parse(thisCookie);
    } else {
        return null;
    }
}

function getPushedMovieFromCookie() {
    let thisCookie = getCookie("moviePush");
    return JSON.parse(thisCookie);
}

function getPushedMovieFromDatabase() {
    promisePushedMovie = new Promise((resolve) => {
        const url = '/getPushedMovie';
        $.getJSON(url, data => {
            resolve(data);
        });
    });
}

function setPushedMovieInDatabase(pushedMovie) {

    let url = "/setPushedMovie";
    let parameters = {
        pushedMovie: JSON.stringify(pushedMovie)
    };

    $.getJSON(url, parameters, callback);

    function callback(data) {
        if (data) {
            window.location.href = "/moviePage";
        } else {
            alertFailure("Oeps! There wa a problem loading the page. Please refresh and try again", longTimeOut);
        }
    }
}

function pushListMoviePage(slide) {
    let pushList = [];
    let movieId = slides[slide - 1].getAttribute('movieId');
    getLoginStatus();
    promiseLoginStatus.then(data => {
        if (data) {
            for (let i = 0; i < privateListItems.length; i++) {
                if (privateListItems[i].id === movieId) {
                    let newPush = {
                        'title': privateListItems[i].title,
                        'release_year': privateListItems[i].release_year,
                        'id': privateListItems[i].id
                    };
                    let pushedMovie = [];
                    pushedMovie.push(newPush);
                    setPushedMovieInDatabase(pushedMovie);
                }
            }
        } else {
            for (let i = 0; i < publicListItems.length; i++) {
                if (publicListItems[i].id === movieId) {
                    let newPush = {
                        'title': publicListItems[i].title,
                        'release_year': publicListItems[i].release_year,
                        'id': publicListItems[i].id
                    };
                    pushList.push(newPush);
                    setSessionCookie("moviePush", pushList);
                    window.location.href = "/moviePage";
                }
            }
        }
    });
}

function pushPopularMoviePage(thumb) {
    let pushList = [];
    let movieId = popularThumbs[thumb - 1].getAttribute('movieId');
    getLoginStatus();
    promiseLoginStatus.then(data => {
        if (data) {
            for (let i = 0; i < systemListItems.length; i++) {
                if (systemListItems[i].id === movieId) {
                    let newPush = {
                        'title': systemListItems[i].title,
                        'release_year': systemListItems[i].release_year,
                        'id': systemListItems[i].id
                    };
                    let pushedMovie = [];
                    pushedMovie.push(newPush);
                    setPushedMovieInDatabase(pushedMovie);
                }
            }
        } else {
            for (let i = 0; i < systemListItems.length; i++) {
                if (systemListItems[i].id === movieId) {
                    let newPush = {
                        'title': systemListItems[i].title,
                        'release_year': systemListItems[i].release_year,
                        'id': systemListItems[i].id
                    };
                    pushList.push(newPush);
                    setSessionCookie("moviePush", pushList);
                    window.location.href = "/moviePage";
                }
            }
        }
    });
}

function pushTopRatedMoviePage(thumb) {
    let pushList = [];
    let movieId = topRatedThumbs[thumb - 1].getAttribute('movieId');
    getLoginStatus();
    promiseLoginStatus.then(data => {
        if (data) {
            for (let i = 0; i < systemListItems.length; i++) {
                if (systemListItems[i].id === movieId) {
                    let newPush = {
                        'title': systemListItems[i].title,
                        'release_year': systemListItems[i].release_year,
                        'id': systemListItems[i].id
                    };
                    let pushedMovie = [];
                    pushedMovie.push(newPush);
                    setPushedMovieInDatabase(pushedMovie);
                }
            }
        } else {
            for (let i = 0; i < systemListItems.length; i++) {
                if (systemListItems[i].id === movieId) {
                    let newPush = {
                        'title': systemListItems[i].title,
                        'release_year': systemListItems[i].release_year,
                        'id': systemListItems[i].id
                    };
                    pushList.push(newPush);
                    setSessionCookie("moviePush", pushList);
                    window.location.href = "/moviePage";
                }
            }
        }
    });
}

function pushSimilarMoviePage(thumb) {
    let pushList = [];
    let movieId = similarThumbs[thumb - 1].getAttribute('movieId');
    getLoginStatus();
    promiseLoginStatus.then(data => {
        if (data) {
            for (let i = 0; i < systemListItems.length; i++) {
                if (systemListItems[i].id === movieId) {
                    let newPush = {
                        'title': systemListItems[i].title,
                        'release_year': systemListItems[i].release_year,
                        'id': systemListItems[i].id
                    };
                    let pushedMovie = [];
                    pushedMovie.push(newPush);
                    setPushedMovieInDatabase(pushedMovie);
                }
            }
        } else {
            for (let i = 0; i < systemListItems.length; i++) {
                if (systemListItems[i].id === movieId) {
                    let newPush = {
                        'title': systemListItems[i].title,
                        'release_year': systemListItems[i].release_year,
                        'id': systemListItems[i].id
                    };
                    pushList.push(newPush);
                    console.log(newPush);
                    setSessionCookie("moviePush", pushList);
                    window.location.href = "/moviePage";
                }
            }
        }
    });
}