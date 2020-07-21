history.navigationMode = 'compatible';
jQuery(function () {
    initHomePage()
});

window.addEventListener('resize', function () {
    displayItems = parseInt(style.getPropertyValue('--displayItems'));
    slideHeight = style.getPropertyValue('--slideHeight');
    refreshPage();
}, false);

const slides = null;
const style = window.getComputedStyle(document.documentElement);
let displayItems = parseInt(style.getPropertyValue('--displayItems'));
let slideHeight = parseInt(style.getPropertyValue('--slideHeight'));

let thumbsToLoad = 0;
let popularThumbIndex = 1;
let topRatedThumbIndex = 1;
let popularThumbs = [];
let topRatedThumbs = [];

function initHomePage() {
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
                if(data != null) {
                    if (data.listItems != null) {
                        privateListItems = data.listItems;
                    }
                }
            });
        } else {
            $('#login').fadeIn(0);
            $('#account').addClass('hide');
        }
        publicListItems = getPublicListItems();
    });
}

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
            retrieveSearch(title, year,0,"showList", listTitle);
        }
    })
        .fail(function () {
            alertFailure("Oeps! An unknown error occurred retrieving the page content :(", longTimeOut)
        })
}

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
    let text = $('.replyText');
    if (search.returnValue === null) {
        alertFailure("No results where found", longTimeOut);
    } else {
        const {poster_url, overview, title, release_year} = search.results;
        if (type === 'searchBox') {
            // update search box
            let poster = "";
            if (search.poster !== null) {
                poster = "data:image/png;base64," + search.poster;
            } else {
                poster = poster_url;
            }
            $('.searchBoxText').fadeOut(FADEOUT_TIME, function () {
                $('.searchBoxText').display = "none";
                $('#searchPoster').attr('src', poster);
                $('#replyTitle').text(title + " (" + release_year + ")");
                $('#replyOverview').text(overview);
                text.display = "block";
                text.fadeIn(FADEIN_TIME);
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
    let poster;
    if (search.poster !== null) {
        poster = "data:image/png;base64," + search.poster;
    } else {
        poster = poster_url;
    }
    let cloneThumb = $('#popular-templateThumb').clone(true, true);
    cloneThumb.removeAttr('id');
    cloneThumb.attr('movieId', id);
    cloneThumb.removeClass('hide');
    cloneThumb.addClass('popularThumb');
    cloneThumb.find('img').attr('src', poster);
    cloneThumb.insertAfter('#popular-templateThumb');
    thumbsToLoad--;
}

function addTopRatedThumb() {
    const {poster_url, id} = search.results;
    let poster;
    if (search.poster !== null) {
        poster = "data:image/png;base64," + search.poster;
    } else {
        poster = poster_url;
    }
    let cloneThumb = $('#topRated-templateThumb').clone(true, true);
    cloneThumb.removeAttr('id');
    cloneThumb.attr('movieId', id);
    cloneThumb.removeClass('hide');
    cloneThumb.addClass('topRatedThumb');
    cloneThumb.find('img').attr('src', poster);
    cloneThumb.insertAfter('#topRated-templateThumb');
    thumbsToLoad--;
}

function refreshPage() {
    if (thumbsToLoad === 0) {
        indexElements();
        showPopularThumbs(1);
        showTopRatedThumbs(1);
        $('.loaderPage').fadeOut(FADEOUT_TIME, function () {
            removeHidden();
        });
    }
}

function indexElements() {
    let i;
    let pThumb = 0;
    let tThumb = 0;
    popularThumbs = document.getElementsByClassName('popularThumb');
    topRatedThumbs = document.getElementsByClassName('topRatedThumb');
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

$(function () {
    let popularThumb = $('.popular');
    popularThumb.on("mouseover", function () {
        $('#popularLabel-container').css('opacity', 0);
    })
    popularThumb.on("mouseleave", function () {
        $('#popularLabel-container').css('opacity', 1);
    })
});

$(function () {
    let topRatedThumb = $('.topRated');
    topRatedThumb.on("mouseover", function () {
        $('#topRatedLabel-container').css('opacity', 0);
    })
    topRatedThumb.on("mouseleave", function () {
        $('#topRatedLabel-container').css('opacity', 1);
    })
});