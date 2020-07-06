history.navigationMode = 'compatible';
jQuery(function () {
    initSearchBox()
});

const DEFAULT_POSTER_URL = "../../resources/images/default-poster-332x500-noborders.png";
const NO_END_DATE = new Date(9999, 1);
// const pushMovie = "moviePush";

let search;
let searchObject = 0;

function initSearchBox() {
    $('.replyText').fadeOut();
}

$(function () {
    $('#newThumb').on('click', function () {
        $('.searchBox').addClass('showSearchBox');
    })
});

$(function () {
    $('#Search').on('click', function () {
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
            callSearch(title, year,"searchBox");
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
            let title = document.querySelector('#searchBoxTitle').value;
            let year = document.querySelector('#searchBoxYear').value;
            callNextSearch(title, year, searchObject, 'searchBox');
        } else {
            alertFailure("Movie title is required for a search", longTimeOut);
        }
    })
});

$(function () {
    $('#replyTextAddButton').on('click', function () {
        addMovieToCurrentList(search.results);
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

function retrieveSearch(searchTitle, searchYear, type, listTitle) {
    let url = "/retrieveSearch";
    let parameters = {
        searchTitle: searchTitle,
        searchYear: searchYear
    };

    $.getJSON(url, parameters, function(data) {
        if (type === "build") {
            loadMoviePage(data);
        } else if (type === "update" || type === "showList") {
            search = data;
            processSearch(data, type, listTitle);
        }
    })
        .fail(function () {
            callSearch(searchTitle, searchYear, type, listTitle);
        })
}

function callSearch(searchTitle, searchYear, type, listTitle) {
    let url;
    let parameters;
    url = "/newSearch";
    parameters = {
        searchTitle: searchTitle,
        searchYear: searchYear
    };

    $.getJSON(url, parameters, searchReturn);

    function searchReturn(data) {
        search = data;
        console.log(data);
        processSearch(data, type, listTitle);
    }
}

function callNextSearch(searchTitle, searchYear, searchObject, type) {
    let url;
    let parameters;
    url = "/nextSearch";
    parameters = {
        searchTitle: searchTitle,
        searchYear: searchYear,
        returnValue: searchObject
    };

    $.getJSON(url, parameters, searchReturn);

    function searchReturn(data) {
        search = data;
        processSearch(data, type);
    }
}