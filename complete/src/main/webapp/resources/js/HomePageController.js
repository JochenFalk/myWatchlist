history.navigationMode = 'compatible';
jQuery(function () {
    initHomePage()
});

const slides = null;
const displayItems = 5;

let thumbsToLoad = 0;
let popularThumbIndex = 1;
let topRatedThumbIndex = 1;
let popularThumbs = [];
let topRatedThumbs = [];

function initHomePage() {
    deleteCookie("moviePush");
    getSystemList("Popular", "System");
    getSystemList("TopRated", "System");
    $('.loaderPage').fadeIn(FADEIN_TIME);

    getLoginStatus();
    promiseLoginStatus.then(data => {
        if (data) {
            $('#login').fadeOut(0);
            $('#account').removeClass('hide');
            getCurrentList();
            promiseCurrentList.then(data => {
                privateListItems = data.listItems;
            });
        } else {
            $('#login').fadeIn(0);
            $('#account').addClass('hide');
            publicListItems = getPublicListItems();
        }
    });
}

$(function () {
    $('.popular').on("mouseover", function () {
        $('#popularLabel-container').css('opacity', 0);
    })
    $('.popular').on("mouseleave", function () {
        $('#popularLabel-container').css('opacity', 1);
    })
});

$(function () {
    $('.topRated').on("mouseover", function () {
        $('#topRatedLabel-container').css('opacity', 0);
    })
    $('.topRated').on("mouseleave", function () {
        $('#topRatedLabel-container').css('opacity', 1);
    })
});

function getSystemList(listTitle, userName) {
    let url = "/getSystemList";
    let parameters = {
        listTitle: listTitle,
        userName: userName
    };

    $.getJSON(url, parameters, function (data) {
        let {listItems} = data;
        thumbsToLoad += listItems.length;
        for (let i = 0; i < listItems.length; i++) {
            let title = listItems[i].title;
            let year = listItems[i].release_year;
            retrieveSearch(title, year, "showList", listTitle);
        }
        console.log("list retrieved");
    })
        .fail(function () {
            alertFailure("Oeps! An unknown error occurred retrieving the page content :(", longTimeOut)
        })
}

// slideshow controls
function popularNextThumb(thumb) {
    let currentThumb = popularThumbIndex;
    currentThumb += (thumb * displayItems);
    currentThumb = clamp(currentThumb, 1, popularThumbs.length - 1)
    if (currentThumb !== popularThumbIndex) {
        popularThumbIndex = currentThumb;
        showPopularThumbs(popularThumbIndex);
    } else {
        alertFailure("Reached end of list", longTimeOut);
    }
}

// slideshow controls
function topRatedNextThumb(thumb) {
    let currentThumb = topRatedThumbIndex;
    currentThumb += (thumb * displayItems);
    currentThumb = clamp(currentThumb, 1, topRatedThumbs.length - 1)
    if (currentThumb !== topRatedThumbIndex) {
        topRatedThumbIndex = currentThumb;
        showTopRatedThumbs(topRatedThumbIndex);
    } else {
        alertFailure("Reached end of list", longTimeOut);
    }
}

function processSearch(search, type, listTitle) {
    // check search identifier for null
    if (search.returnValue === '-1') {
        alertFailure("No results where found", longTimeOut);
    } else {
        const {poster_url, overview, title, release_year} = search.results;
        if (type === 'searchBox') {
            // update search box
            $('.searchBoxText').fadeOut(FADEOUT_TIME, function () {
                $('.searchBoxText').display = "none";
                $('#searchPoster').attr('src', poster_url);
                $('#replyTitle').text(title + " (" + release_year + ")");
                $('#replyOverview').text(overview);
                $('.replyText').display = "block";
                $('.replyText').fadeIn(FADEIN_TIME);
                searchResultAnimation();
            });
        } else if (type === "showList") {
            const {title, release_year, id} = search.results;
            let newMovie = {
                'title': title,
                'release_year': release_year,
                'id': id
            };
            systemListItems.push(newMovie);
            if (listTitle === "Popular") {
                addPopularThumb();
                refreshPage();
            } else if (listTitle === "TopRated") {
                addTopRatedThumb();
                refreshPage();
            }
        }
    }
}

function addPopularThumb() {
    const {poster_url, id} = search.results;
    let cloneThumb = $('#popular-templateThumb').clone(true, true);
    cloneThumb.removeAttr('id');
    cloneThumb.attr('name', id);
    cloneThumb.removeClass('hide');
    cloneThumb.addClass('popularThumb');
    cloneThumb.find('img').attr('src', poster_url);
    cloneThumb.insertAfter('#popular-templateThumb');
    thumbsToLoad--;
}

function addTopRatedThumb() {
    const {poster_url, id} = search.results;
    let cloneThumb = $('#topRated-templateThumb').clone(true, true);
    cloneThumb.removeAttr('id');
    cloneThumb.attr('name', id);
    cloneThumb.removeClass('hide');
    cloneThumb.addClass('topRatedThumb');
    cloneThumb.find('img').attr('src', poster_url);
    cloneThumb.insertAfter('#topRated-templateThumb');
    thumbsToLoad--;
}

function refreshPage() {
    if (thumbsToLoad == 0) {
        indexElements();
        showPopularThumbs(1);
        showTopRatedThumbs(1);
        $('.loaderPage').fadeOut(FADEOUT_TIME, function () {
            removeHidden();
        });
    }
}

function indexElements() {
    // find elements
    let i;
    let pThumb = 0;
    let tThumb = 0;
    popularThumbs = document.getElementsByClassName('popularThumb');
    topRatedThumbs = document.getElementsByClassName('topRatedThumb');
    // set element id used by thumb slideshow and create event listeners
    for (i = 0; i < popularThumbs.length; i++) {
        pThumb++;
        popularThumbs[i].setAttribute('thumbId', pThumb + '');
        let id = popularThumbs[i].getAttribute('id');
        if (id !== "infoThumb") {
            createEventListenerPopularThumb(i, pThumb);
        }
    }
    for (i = 0; i < topRatedThumbs.length; i++) {
        tThumb++;
        topRatedThumbs[i].setAttribute('thumbId', tThumb + '');
        let id = topRatedThumbs[i].getAttribute('id');
        if (id !== "infoThumb") {
            createEventListenerTopRatedThumb(i, tThumb);
        }
    }
}

function createEventListenerPopularThumb(i, thumb) {
    $(popularThumbs[i]).off('click');
    return $(popularThumbs[i]).on('click', function () {
        pushPopularMoviePage(thumb);
    })
}

function createEventListenerTopRatedThumb(i, thumb) {
    $(topRatedThumbs[i]).off('click');
    return $(topRatedThumbs[i]).on('click', function () {
        pushTopRatedMoviePage(thumb);
    })
}

// Show and hide thumbs
function showPopularThumbs(thumb) {
    let i;
    let visibleThumbs = 0;
    for (i = 0; i < popularThumbs.length; i++) {
        let thumbId = popularThumbs[i].getAttribute("thumbId");
        if (thumbId >= thumb && thumbId < (thumb + displayItems)) {
            popularThumbs[i].style.display = "grid";
            visibleThumbs++;
        } else {
            popularThumbs[i].style.display = "none";
        }
    }
    // Set width of info thumb depending of amount of movie thumbs are visible
    let width = getComputedStyle(root).getPropertyValue("--newThumbWidth");
    let maxWidth = getComputedStyle(root).getPropertyValue("--newThumbMaxWidth");
    root.style.setProperty("--popularInfoThumbWidth", "calc(" + ((displayItems + 1) - visibleThumbs) + " *" + width + ")");
    root.style.setProperty("--popularInfoThumbMaxWidth", "calc(" + ((displayItems + 1) - visibleThumbs) + " *" + maxWidth + ")");
    showThumbAnimation(popularThumbs);
}

function showTopRatedThumbs(thumb) {
    let i;
    let visibleThumbs = 0;
    for (i = 0; i < topRatedThumbs.length; i++) {
        let thumbId = topRatedThumbs[i].getAttribute("thumbId");
        if (thumbId >= thumb && thumbId < (thumb + displayItems)) {
            topRatedThumbs[i].style.display = "grid";
            visibleThumbs++;
        } else {
            topRatedThumbs[i].style.display = "none";
        }
    }
    // Set width of info thumb depending of amount of movie thumbs are visible
    let width = getComputedStyle(root).getPropertyValue("--newThumbWidth");
    let maxWidth = getComputedStyle(root).getPropertyValue("--newThumbMaxWidth");
    root.style.setProperty("--topRatedInfoThumbWidth", "calc(" + ((displayItems + 1) - visibleThumbs) + " *" + width + ")");
    root.style.setProperty("--topRatedInfoThumbMaxWidth", "calc(" + ((displayItems + 1) - visibleThumbs) + " *" + maxWidth + ")");
    showThumbAnimation(topRatedThumbs);
}

function removeHidden() {
    let i;
    let hidden = document.getElementsByClassName("un-hide");
    for (i = 0; i < hidden.length; i++) {
        hidden[i].classList.remove("hide");
    }
}

function clamp(val, min, max) {
    return val < min ? min : (val > max ? max : val);
}