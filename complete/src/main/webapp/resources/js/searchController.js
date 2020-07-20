history.navigationMode = 'compatible';
jQuery(function () {
    initSearchBox()
});

const DEFAULT_POSTER_URL = "../../resources/images/default-poster-332x500-noborders.png";
const NO_END_DATE = new Date(9999, 1);

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
            callSearch(title, year, searchObject, "searchBox");
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
            callSearch(title, year, searchObject,'searchBox');
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

function retrieveSearch(searchTitle, searchYear, searchObject, processType, listTitle) {
    let url = "/retrieveSearch";
    let parameters = {
        searchTitle: searchTitle,
        searchYear: searchYear
    };

    $.getJSON(url, parameters, function(data) {
        console.log("retrieved search");
        if (processType === "build") {
            loadMoviePage(data);
        } else if (processType === "update" || processType === "showList" || processType === "searchBox") {
            search = data;
            processSearch(data, processType, listTitle);
        }
    })
        .fail(function () {
            console.log("called new search");
            callSearch(searchTitle, searchYear, searchObject, processType, listTitle);
        })
}

function callSearch(searchTitle, searchYear, searchObject, processType, listTitle) {
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
        processSearch(data, processType, listTitle);
        // if (processType !== "searchBox") {
            saveLastPerformedSearch();
        // }
    }
}

function saveLastPerformedSearch() {
    let parameters;
    let url = "/saveSearch";
    parameters = {
        searchObject: JSON.stringify(search)
    };

    $.getJSON(url, parameters);
}