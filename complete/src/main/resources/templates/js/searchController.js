const NO_END_DATE = new Date(9999, 1);
const API_KEY = "1c0a5b80ba526a387cb22c7d79fbeb03";
const BASE_IMAGE_URL = "https://image.tmdb.org/t/p/original";
const DEFAULT_POSTER_URL = "images/default-poster-332x500-noborders.png";

const BASE_BACKDROP_URL = "https://image.tmdb.org/t/p/original";
const DEFAULT_BACKDROP_URL = "";
const BASE_CASTPOSTER_URL = "https://image.tmdb.org/t/p/original";
const DEFAULT_CASTPOSTER_URL = "";

const listName = "movieList";
const pushMovie = "moviePush";

let searchObject = 0;
let searchResult;
let searchTitle;
let searchYear;
let movieIndex;
let movieTitle;
let movieYear;
let movieRating;
let movieReleaseDate;
let moviePoster;
let moviePlot;
let movieId;
let movieBackDrop

let slides = [];
let posters = [];
let titles = [];
let thumbs = [];
let deleteButtons = [];
let movieDirectors = [];
let movieWriters = [];
let movieCastNames = [];
let movieCastPosters = [];

$(function () {
    $('#newThumb').on('click', function () {
        $('.searchBox').addClass('showSearchBox');
    })
});

$(function () {
    $('#closeSearchBox').on('click', function () {
        document.querySelector('#searchBoxTitle').value = "";
        document.querySelector('#searchBoxYear').value = "";
        $('.searchBox').removeClass('showSearchBox');
        $('#searchBoxNextButton').fadeOut();
        $('#searchPoster').attr('src', DEFAULT_POSTER_URL);
    })
});

$(function () {
    $('#refreshSearchBox').on('click', function () {
        $('.replyText').fadeOut(FADEOUT_TIME, function () {
            $('.replyText').display = "none";
            $('#searchBoxNextButton').fadeIn();
            $('.searchBoxText').display = "block";
            $('.searchBoxText').fadeIn(FADEIN_TIME);
        })
    })
});

$(function () {
    $('#searchBoxSearchButton').on('click', function () {
        searchObject = 0;
        if (document.querySelector('#searchBoxTitle').value !== "") {
            alertSuccess("Fetching movie info", shortTimeOut);
            let title = document.querySelector('#searchBoxTitle').value;
            let year = document.querySelector('#searchBoxYear').value;
            callSearch(title, year, "", "searchBox", "search");
        } else {
            alertFailure("Movie title is required for a search", longTimeOut);
        }
    })
});


$(function () {
    $('#searchBoxNextButton').on('click', function () {
        searchObject++;
        if (document.querySelector('#searchBoxTitle').value !== "") {
            alertSuccess("Fetching movie info", shortTimeOut);
            callSearch('', '', '', 'searchBox', 'search');
        } else {
            alertFailure("Movie title is required for a search", longTimeOut);
        }
    })
});

$(function () {
    $('#replyTextAddButton').on('click', function () {
        movieIndex = slides.length + 1;
        let newMovie = {
            'title': movieTitle,
            'year': movieYear,
            'rating': movieRating,
            'release': movieReleaseDate,
            'poster': moviePoster,
            'id': movieId,
            'backdrop': movieBackDrop
        };
        publicList.push(newMovie);
        addThumb();
        addSlide();
        resetSearchBox();
        updateList();
        alertSuccess("Successfully added movie", shortTimeOut);
    })
});

function resetSearchBox() {
    $('.replyText').fadeOut(FADEOUT_TIME, function () {
        $('.replyText').display = "none";
        $('#searchBoxNextButton').fadeIn(0);
        $('.searchBoxText').display = "block";
        $('.searchBoxText').fadeIn(FADEIN_TIME);
    })
}

function callSearch(title, year, id, type, query) {
    let url;
    let parameters;
    searchTitle = title;
    searchYear = year;

    if (query == 'cast' || query == 'crew') {
        url = "/searchById";
        parameters = {
            searchId: id,
        };

    } else if (query == 'search') {
        url = "/searchByTitle";
        parameters = {
            searchTitle: title,
            searchYear: year
        };
    }

    $.getJSON(url, parameters, searchReturn);

    function searchReturn(data) {
        let results;
        if (query == 'cast') {
            results = data["cast"];
        } else if (query == 'crew') {
            results = data["crew"];
        } else if (query == 'search') {
            results = data.results;
        }
        processSearch(results, type, query)
    }
}

function processSearch(results, type, query) {
    searchResult = results;

    // set search result variables
    if (query == 'search') {
        if (searchResult.length == 0) {
            alertFailure("No results where found", longTimeOut);
        } else {
            console.log(searchResult);
            searchObject = clamp(searchObject, 0, searchResult.length - 1);
            const {vote_average, release_date, overview, poster_path, backdrop_path, id, original_title} = searchResult[searchObject];
            // set and check poster path
            moviePoster = BASE_IMAGE_URL + poster_path;
            if (moviePoster.substr(-4, 4) == "null") {
                moviePoster = DEFAULT_POSTER_URL;
                alertFailure("Unable to fetch movie poster", longTimeOut);
            }
            // set and check backdrop path
            movieBackDrop = BASE_BACKDROP_URL + backdrop_path;
            if (movieBackDrop.substr(-4, 4) == "null") {
                alertFailure("Unable to fetch movie backdrop", longTimeOut);
                movieBackDrop = DEFAULT_BACKDROP_URL;
            }
            movieTitle = original_title;
            movieYear = parseInt(release_date);
            movieRating = vote_average;
            movieReleaseDate = release_date;
            moviePlot = overview;
            movieId = id;
        }
    } else if (query == 'cast') {
        // get cast poster path
        for (let i = 0; i <= 2; i++) {
            let castPoster;
            const {name: name, profile_path: profile_path} = results[i];
            movieCastNames.push(name);
            castPoster = BASE_CASTPOSTER_URL + profile_path;
            // set and check cast poster path
            if (castPoster.substr(-4, 4) == "null") {
                alertFailure("Unable to fetch cast poster", longTimeOut);
                castPoster = DEFAULT_CASTPOSTER_URL;
                movieCastPosters.push(castPoster);
            } else {
                movieCastPosters.push(castPoster);
            }
        }
    } else if (query == 'crew') {
        // get cast names
        for (let i = 0; i < results.length; i++) {
            const {name: name, department: department} = results[i];
            if (department == "Directing") {
                movieDirectors.push(name);
            }
            if (department == "Writing") {
                movieWriters.push(name);
            }
        }
    }
    if (type == 'searchBox') {
        // update search box
        $('.searchBoxText').fadeOut(FADEOUT_TIME, function () {
            $('.searchBoxText').display = "none";
            $('#searchPoster').attr('src', moviePoster);
            $('#replyTitle').text(movieTitle + " (" + movieYear + ")");
            $('#replyOverview').text(moviePlot);
            $('.replyText').display = "block";
            $('.replyText').fadeIn(FADEIN_TIME);
            searchResultAnimation();
        });
    } else if (type == 'update') {
        // executed on page reload
        addThumb();
        addSlide();
        updateList();
        // alertSuccess("Successfully retrieved movie list", longTimeOut);
    } else if (type == 'build') {
        // executed on movie page load
        loadMoviePage(query);
        // alertSuccess("Successfully build movie page", longTimeOut);
    }
}

function updateList() {
    indexElements();
    showThumbs(1);
    showSlides(1);
    setCookie(listName, publicList, NO_END_DATE);
}

function indexElements() {
    slides = [];
    posters = [];
    titles = [];
    thumbs = [];
    deleteButtons = [];
    // find all elements
    slides = document.getElementsByClassName('slide');
    // posters = document.getElementsByClassName('slide poster');
    // titles = document.getElementsByClassName('slide title');
    thumbs = document.getElementsByClassName('thumb');
    // deleteButtons = document.getElementsByClassName('deleteSlide');
    let i;
    let slide = 0;
    // set element id used to order slides and create event listeners
    for (i = 0; i < slides.length; i++) {
        slide++;
        slides[i].setAttribute('slideId', slide + '');
        let poster = slides[i].getElementsByClassName('poster');
        poster[0].setAttribute('posterId', slide + '');
        let title = slides[i].getElementsByClassName('title');
        title[0].setAttribute('titleId', slide + '');
        let deleteButton = slides[i].getElementsByClassName('deleteButton');
        deleteButton[0].setAttribute('buttonId', slide + '');
        createEventListenerPoster(i, slide);
        createEventListenerTitle(i, slide);
        createEventListenerDeleteButton(i, slide);
    }
    let thumb = 0;
    // set element id used by thumb slide show  and create event listeners
    for (i = 0; i < thumbs.length; i++) {
        thumb++;
        thumbs[i].setAttribute('thumbId', thumb + '');
        let id = thumbs[i].getAttribute('id');
        if (id != "newThumb" && id != "infoThumb") {
            createEventListenerThumb(thumb);
        }
    }
}

function createEventListenerPoster(i, slide) {
    let poster = slides[i].getElementsByClassName('poster');
    $(poster[0]).off('click');
    return $(poster[0]).on('click', function () {
        pushMoviePage(slide);
    })
}

function createEventListenerTitle(i, slide) {
    let title = slides[i].getElementsByClassName('title');
    $(title[0]).off('click');
    return $(title[0]).on('click', function () {
        pushMoviePage(slide);
    })
}

function createEventListenerDeleteButton(i, slide) {
    let button = slides[i].getElementsByClassName('deleteButton');
    $(button[0]).off('click');
    return $(button[0]).on('click', function () {
        deleteSlide(slide);
    })
}

function createEventListenerThumb(thumb) {
    $(thumbs[thumb - 1]).off('click');
    return $(thumbs[thumb - 1]).on('click', function () {
        currentThumb(thumb);
    })
}

function clamp(val, min, max) {
    return val < min ? min : (val > max ? max : val);
}