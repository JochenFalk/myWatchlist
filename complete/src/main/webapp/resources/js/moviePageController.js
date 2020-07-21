history.navigationMode = 'compatible';
jQuery(function () {
    initMoviePage()
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

let similarThumbIndex = 1;
let similarThumbs = [];
let thumbsToLoad = 0;

function initMoviePage() {
    $('.loaderPage').fadeIn(FADEIN_TIME);
    $('#pauseButton').fadeOut(0);
    $('#refreshButton').fadeOut(0);

    let castCharacterNames = document.getElementsByClassName('castCharacterName');
    let directorNames = document.getElementsByClassName('directorName');
    let writerNames = document.getElementsByClassName('writerName');

    let length = castCharacterNames.length;
    for (let i = 0; i < length; i++) {
        $(castCharacterNames[i]).siblings().on('mouseover', function () {
            scrollTextForward(castCharacterNames[i], 3500);
        })
    }
    length = directorNames.length;
    for (let i = 0; i < length; i++) {
        $(directorNames[i]).siblings().on('mouseover', function () {
            scrollTextForward(directorNames[i], 3500);
        })
    }
    length = writerNames.length;
    for (let i = 0; i < length; i++) {
        $(writerNames[i]).siblings().on('mouseover', function () {
            scrollTextForward(writerNames[i], 3500);
        })
    }

    getLoginStatus();
    promiseLoginStatus.then(data => {
        if (data) {
            $('#login').fadeOut(0);
            $('#account').removeClass('hide');
            getCurrentList();
            promiseCurrentList.then(data => {
                if (data != null) {
                    if (data.listItems != null) {
                        privateListItems = data.listItems;
                    }
                }
            });
        } else {
            $('#login').fadeIn(0);
            $('#account').addClass('hide');
        }
        buildMoviePage(getPushedMovieFromCookie());
        publicListItems = getPublicListItems();
    });
}

function buildMoviePage(pushedMovie) {
    getLoginStatus();
    promiseLoginStatus.then(data => {
        if (data) {
            getPushedMovieFromDatabase();
            promisePushedMovie.then(data => {
                if (data) {
                    if (data.length !== 0) {
                        let title = data[0].title;
                        let year = data[0].release_year;
                        retrieveSearch(title, year, 0, "build", "");
                    } else {
                        $('.loaderPage').fadeOut(FADEOUT_TIME, function () {
                            alertFailure("Oeps! There wa a problem loading the page. Please refresh and try again", longTimeOut);
                            setTimeout(function () {
                                // window.location = '/homePage';
                            }, longTimeOut);
                        });
                    }
                }
            });
        } else {
            if (pushedMovie == null || pushedMovie === "") {
                $('.loaderPage').fadeOut(FADEOUT_TIME, function () {
                    alertFailure("Oeps! There wa a problem loading the page. Please refresh and try again", longTimeOut);
                    setTimeout(function () {
                        window.location = '/homePage';
                    }, longTimeOut);
                });
            } else {
                let title = pushedMovie[0].title;
                let year = pushedMovie[0].release_year;
                retrieveSearch(title, year, 0, "build", "");
            }
        }
    });
}

function loadMoviePage(search) {

    const {crew: crew, cast: cast, results} = search;
    const {vote_average, overview, title, id, release_year, poster_url, backdrop_url, video_url} = results;

    let poster;
    if (search.poster !== null) {
        poster = "data:image/png;base64," + search.poster;
    } else {
        poster = poster_url;
    }

    $('#banner').attr('src', backdrop_url);
    $('#youtube_player').attr('src', video_url);
    $('.aside .poster').find('img').attr('src', poster);
    $('.title').text(title + " (" + release_year + ")");
    $('.rating').text(vote_average);
    $('.plot').text(overview);

    let crewDirectors = document.getElementsByClassName('crewDirector');
    let crewWriters = document.getElementsByClassName('crewWriter');
    let castNames = document.getElementsByClassName('castName');
    let castPosters = document.getElementsByClassName('castPoster');
    let castCharacterNames = document.getElementsByClassName('castCharacterName');

    let dCount = 0;
    let wCount = 0;

    for (let i = 0; i < cast.length; i++) {
        const {profile_url, name, character} = cast[i];
        castPosters[i].setAttribute('src', profile_url);
        castPosters[i].parentElement.classList.remove("hide");
        $(castNames[i]).text(name);
        $(castCharacterNames[i]).text(name + " as " + character);
    }
    for (let i = 0; i < crew.length; i++) {
        const {job, name} = crew[i];
        if (job === "Director") {
            $(crewDirectors[dCount]).text(name);
            dCount++;
        }
    }
    for (let i = 0; i < crew.length; i++) {
        const {job, name} = crew[i];
        if (job === "Writer") {
            $(crewWriters[wCount]).text(name);
            wCount++;
        }
    }

    if (dCount !== 2) {
        $('.crewDirector').siblings('span').text("");
    }
    if (dCount === 0) {
        $('.crewDirector').first().text("Unknown");
    }
    if (wCount !== 2) {
        $('.crewWriter').siblings('span').text("");
    }
    if (wCount === 0) {
        $('.crewWriter').first().text("Unknown");
    }

    getSimilar(parseInt(id));
}

function removeHidden() {
    let i;
    let hidden = document.getElementsByClassName("un-hide");
    for (i = 0; i < hidden.length; i++) {
        hidden[i].classList.remove("hide");
    }
}

function getSimilar(movieId) {
    let url;
    url = "/getSimilar";
    let parameters = {
        movieId: movieId
    };

    $.getJSON(url, parameters, callback);

    function callback(data) {
        let {listItems} = data;
        thumbsToLoad += listItems.length;
        if (listItems.length !== 0) {
            for (let i = 0; i < listItems.length; i++) {
                let title = listItems[i].title;
                let year = listItems[i].release_year;
                retrieveSearch(title, year, 0, "showList", "Similar")
            }
            console.log("list created");
        } else {
            $('.loaderPage').fadeOut(FADEOUT_TIME, function () {
                $('.similar').fadeOut(0);
                removeHidden();
            });
        }
    }
}

function similarNextThumb(thumb) {
    let currentThumb = similarThumbIndex;
    currentThumb += (thumb * displayItems);
    currentThumb = clamp(currentThumb, 1, similarThumbs.length - 1)
    if (currentThumb !== similarThumbIndex) {
        similarThumbIndex = currentThumb;
        showSimilarThumbs(similarThumbIndex);
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
        let poster = "";
        if (search.poster !== null) {
            poster = "data:image/png;base64," + search.poster;
        } else {
            poster = poster_url;
        }
        if (type === 'searchBox') {
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
            if (listTitle === "Similar") {
                addSimilarThumb();
                refreshPage();
            }
        } else if (type === 'build') {
            loadMoviePage(search);
        }
    }
}

function addSimilarThumb() {
    const {poster_url, id} = search.results;
    let poster;
    if (search.poster !== null) {
        poster = "data:image/png;base64," + search.poster;
    } else {
        poster = poster_url;
    }
    let cloneThumb = $('#similar-templateThumb').clone(true, true);
    cloneThumb.removeAttr('id');
    cloneThumb.attr('movieId', id);
    cloneThumb.removeClass('hide');
    cloneThumb.addClass('similarThumb');
    cloneThumb.find('img').attr('src', poster);
    cloneThumb.insertAfter('#similar-templateThumb');
}

function refreshPage() {
    if (thumbsToLoad === 1) {
        indexElements();
        showSimilarThumbs(1);
        $('.loaderPage').fadeOut(FADEOUT_TIME, function () {
            removeHidden();
        });
    } else {
        thumbsToLoad--;
    }
}

function indexElements() {
    let sThumb = 0;
    similarThumbs = document.getElementsByClassName('similarThumb');
    for (let i = 0; i < similarThumbs.length; i++) {
        sThumb++;
        similarThumbs[i].setAttribute('thumbId', sThumb + '');
        let id = similarThumbs[i].getAttribute('id');
        if (id !== "infoThumb") {
            createEventListenerSimilarThumb(i, sThumb);
        }
    }
}

function createEventListenerSimilarThumb(i, thumb) {
    $(similarThumbs[i]).off('click');
    return $(similarThumbs[i]).on('click', function () {
        pushSimilarMoviePage(thumb);
    })
}

function showSimilarThumbs(thumb) {
    let i;
    let visibleThumbs = 0;
    for (i = 0; i < similarThumbs.length; i++) {
        let thumbId = similarThumbs[i].getAttribute("thumbId");
        if (thumbId >= thumb && thumbId < (thumb + displayItems)) {
            similarThumbs[i].style.display = "grid";
            visibleThumbs++;
        } else {
            similarThumbs[i].style.display = "none";
        }
    }
    // Set width of info thumb depending of amount of movie thumbs are visible
    let width = getComputedStyle(root).getPropertyValue("--newThumbWidth");
    let maxWidth = getComputedStyle(root).getPropertyValue("--newThumbMaxWidth");
    root.style.setProperty("--similarInfoThumbWidth", "calc(" + ((displayItems + 1) - visibleThumbs) + " *" + width + ")");
    root.style.setProperty("--similarInfoThumbMaxWidth", "calc(" + ((displayItems + 1) - visibleThumbs) + " *" + maxWidth + ")");
    showThumbAnimation(similarThumbs);
}

function play() {
    $('.yt_player_iframe').each(function () {
        this.contentWindow.postMessage('{"event":"command","func":"playVideo","args":""}', '*')
    });
}

function pause() {
    $('.yt_player_iframe').each(function () {
        this.contentWindow.postMessage('{"event":"command","func":"pauseVideo","args":""}', '*')
    });
}

function replay() {
    $('.yt_player_iframe').each(function () {
        this.contentWindow.postMessage('{"event":"command","func":"stopVideo","args":""}', '*')
        this.contentWindow.postMessage('{"event":"command","func":"playVideo","args":""}', '*')
    });
}

function scrollTextForward(targetElement, speed) {
    const scrollWidth = targetElement.scrollWidth;
    const clientWidth = targetElement.clientWidth;

    $(targetElement).css('color', 'rgb(95,158,160)');
    $(targetElement).animate({scrollLeft: scrollWidth - clientWidth},
        {
            duration: speed,
            complete: function () {
                $(targetElement).animate({scrollLeft: 0},
                    {
                        duration: 1200,
                        complete: function () {
                            $(targetElement).css('color', 'black');
                        }
                    });
            }
        });
}

function clamp(val, min, max) {
    return val < min ? min : (val > max ? max : val);
}

$(function () {
    $('.poster-overlay').on('click', function () {
        promiseLoginStatus.then(data => {
            if (data) {
                getPushedMovieFromDatabase();
                promisePushedMovie.then(data => {
                    addMovieToCurrentList(data[0]);
                });
            } else {
                addMovieToCurrentList(getPushedMovieFromCookie()[0]);
            }
        });
    })
});

$(function () {
    $('.play-pause').on('click', function () {
        let banner = $('.banner');
        // block transition time is set in moviePage.css at 1500ms
        if (!banner.hasClass('toggleBanner')) {
            $('.header').css('max-height', 560 + "px")
            banner.fadeOut(1500);
            $('#refreshButton').fadeIn(FADEIN_TIME);
            $('#playButton').fadeOut(0);
            $('#pauseButton').fadeIn(0);
            $('.video-container').fadeIn(250, function () {
                $('#button-container').css('background', 'transparent');
                $('.item').addClass('filter');
                play();
                setTimeout(function () {
                    banner.toggleClass('toggleBanner');
                    $('#button-container').css('opacity', 0);
                    root.style.setProperty('--bannerMaxHeight', 560 + "px");
                }, 1250);
                if (!banner.hasClass('toggleDropdown') && !banner.hasClass('toggleBanner')) {
                    arrowUpAnimation();
                }
            });
        }
    })
});

$(function () {
    $('#dropDownButton').on('click', function () {
        let banner = $('.banner');
        let buttonContainer = $('#button-container');
        if (banner.hasClass('toggleBanner')) {
            $('.header').css('max-height', 225 + "px");
            $('.video-container').fadeOut(1500);
            $('#pauseButton').fadeOut(0);
            $('#playButton').fadeIn(0);
            $('#refreshButton').fadeOut(FADEOUT_TIME);
            setTimeout(function () {
                pause();
                banner.fadeIn(1000);
                banner.toggleClass('toggleBanner');
                buttonContainer.css('background', 'rgba(105, 105, 105, 0.25)');
                buttonContainer.css('opacity', 1);
                $('.item').removeClass('filter');
                root.style.setProperty('--bannerMaxHeight', 225 + "px");
                arrowDownAnimation();
            }, 500);
        } else if (!banner.hasClass('toggleDropdown')) {
            banner.addClass('toggleDropdown');
            $('.header').css('max-height', 508 + "px")
            root.style.setProperty('--bannerMaxHeight', 508 + "px");
            arrowUpAnimation();
        } else if (banner.hasClass('toggleDropdown')) {
            banner.removeClass('toggleDropdown');
            $('.header').css('max-height', 225 + "px");
            root.style.setProperty('--bannerMaxHeight', 225 + "px");
            setTimeout(function () {
                buttonContainer.css('background', 'rgba(105, 105, 105, 0.25)');
                buttonContainer.css('opacity', 1);
            }, 500);
            arrowDownAnimation();
        }
    });
});

$(function () {
    $('#playText').on('click', function () {
        let banner = $('.banner');
        if (banner.hasClass('toggleBanner')) {
            play();
            $('#playButton').fadeOut(0);
            $('#pauseButton').fadeIn(0);
            if (!banner.hasClass('toggleDropdown') && !banner.hasClass('toggleBanner')) {
                arrowUpAnimation();
            }
        }
    })
});

$(function () {
    $('#playButton').on('click', function () {
        let banner = $('.banner');
        if (banner.hasClass('toggleBanner')) {
            play();
            $('#playButton').fadeOut(0);
            $('#pauseButton').fadeIn(0);
            if (!banner.hasClass('toggleDropdown') && !banner.hasClass('toggleBanner')) {
                arrowUpAnimation();
            }
        }
    })
});

$(function () {
    $('#pauseButton').on('click', function () {
        pause();
        $('#pauseButton').fadeOut(0);
        $('#playButton').fadeIn(0);
    })
})

$(function () {
    $('#refreshButton').on('click', function () {
        replay();
        $('#playButton').fadeOut(0);
        $('#pauseButton').fadeIn(0);
    })
});

$(function () {
    $('#button-container').on('mouseover', function () {
        $('#button-container').css('opacity', 1);
    })
    $('.video-container').on('mouseover', function () {
        $('#button-container').css('opacity', 1);
    })
});

$(function () {
    $('#button-container').on('mouseleave', function () {
        if ($('.banner').hasClass('toggleBanner')) {
            $('#button-container').css('opacity', 0);
        }
    })
    $('.video-container').on('mouseleave', function () {
        if ($('.banner').hasClass('toggleBanner')) {
            $('#button-container').css('opacity', 0);
        }
    })
});

$(function () {
    let poster = $('.poster');
    let overlay = $('.poster-overlay');
    poster.on("mouseover", function () {
        overlay.css('opacity', 1);
        poster.find('img').css('border', '1px solid cadetblue');
    })
    poster.on("mouseleave", function () {
        overlay.css('opacity', 0);
        poster.find('img').css('border', '1px solid black');
    })
    overlay.on("mouseover", function () {
        overlay.css('opacity', 1);
        poster.find('img').css('border', '1px solid cadetblue');
    })
    overlay.on("mouseleave", function () {
        overlay.css('opacity', 0);
        poster.find('img').css('border', '1px solid black');
    })
});

$(function () {
    let similarThumb = $('.similar');
    similarThumb.on("mouseover", function () {
        $('#similarLabel-container').css('opacity', 0);
    })
    similarThumb.on("mouseleave", function () {
        $('#similarLabel-container').css('opacity', 1);
    })
});