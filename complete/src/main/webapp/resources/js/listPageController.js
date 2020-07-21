history.navigationMode = 'compatible';
jQuery(function () {
    initListPage()
});

window.addEventListener('resize', function () {
    window.location.href = "/listPage";
}, false)

const style = window.getComputedStyle(document.documentElement);
let displayItems = parseInt(style.getPropertyValue('--displayItems'));
let slideHeight = parseInt(style.getPropertyValue('--slideHeight'));

let thumbIndex = 1;
let slideIndex = 1;
let slides = [];
let thumbs = [];
let thumbsToLoad = 0;
let pageLoad = true;

function initListPage() {
    let height = (slideHeight - 10) + "px";
    root.style.setProperty("--slideHeight", height);
    $('.loaderPage').fadeIn(FADEIN_TIME);

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
        buildMovieList();
        publicListItems = getPublicListItems();
    });
}

function buildMovieList() {
    getLoginStatus();
    promiseLoginStatus.then(data => {
        if (data) {
            getCurrentList();
            promiseCurrentList.then(data => {
                if (data !== null) {

                    let listTitle = data.title;
                    let description = data.description;

                    $('#myWatchlistLabel-container').find('div').text(listTitle + " : " + description);

                    let listItems = data.listItems;
                    if (listItems.length !== 0) {
                        thumbsToLoad += listItems.length;
                        for (let i = 0; i < listItems.length; i++) {
                            privateListItems.push(listItems[i]);
                            let title = listItems[i].title;
                            let year = listItems[i].release_year;
                            retrieveSearch(title, year, 0, 'update', "");
                        }
                    } else {
                        $('.loaderPage').fadeOut(FADEOUT_TIME, function () {
                            alertSuccess("Welcome! Try making your new list", longTimeOut);
                            showThumbs(0);
                            removeHidden();
                        });
                    }
                    // }
                } else {
                    $('.loaderPage').fadeOut(FADEOUT_TIME);
                    removeHidden();
                    $('#myWatchlistLabel-container').find('div').text("MyWatchlist: My first watchlist");
                    $('#list').trigger("click");
                    showListMessage("You dont seem to have any list. Try creating on :)", alertSuccessColor);
                }
            });
        } else {
            if (publicListItems == null) {
                $('.loaderPage').fadeOut(FADEOUT_TIME, function () {
                    alertSuccess("Welcome! Try making your new list", longTimeOut);
                    removeHidden();
                });
                setCookie(publicListItems, '', NO_END_DATE);
            } else if (publicListItems.length === 0) {
                $('.loaderPage').fadeOut(FADEOUT_TIME, function () {
                    alertSuccess("Welcome! Try making your new list", longTimeOut);
                    removeHidden();
                });
            } else {
                thumbsToLoad += publicListItems.length;
                for (let i = 0; i < publicListItems.length; i++) {
                    let title = publicListItems[i].title;
                    let year = publicListItems[i].release_year;
                    retrieveSearch(title, year, 0, 'update', "");
                }
            }
            $('#myWatchlistLabel-container').find('div').text("MyWatchlist: My first watchlist");
        }
    });
}

function processSearch(search, type) {
    let text = $('.replyText');
    if (search.returnValue === null) {
        alertFailure("No results where found", longTimeOut);
    } else {
        const {poster_url, overview, title, release_year} = search.results;
        if (type === 'searchBox') {
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
        } else if (type === 'update') {
            // executed on list page reload
            addThumb();
            addSlide();
            refreshPage();
        }
    }
}

function addThumb() {
    const {poster_url} = search.results;
    let poster;
    if (search.poster !== null) {
        poster = "data:image/png;base64," + search.poster;
    } else {
        poster = poster_url;
    }
    let cloneThumb = $('#templateThumb').clone(true, true);
    cloneThumb.removeAttr('id');
    cloneThumb.removeClass('hide');
    cloneThumb.addClass('thumb');
    cloneThumb.find('img').attr('src', poster);
    cloneThumb.insertAfter('#templateThumb');
}

// below "argument type string not assignable to parameter type Jquery" appeared in 2020.1 version of IntelliJ?
function addSlide() {
    const {vote_average, overview, release_year, poster_url, id, title} = search.results;
    let poster;
    if (search.poster !== null) {
        poster = "data:image/png;base64," + search.poster;
    } else {
        poster = poster_url;
    }
    let cloneSlide = $('#templateSlide').clone(true, true);
    cloneSlide.removeAttr('id');
    cloneSlide.attr('movieId', id);
    cloneSlide.removeClass('hide');
    cloneSlide.addClass('slide');
    cloneSlide.find('.poster').find('img').attr('src', poster);
    let deleteButton = cloneSlide.find('#templateDeleteButton');
    deleteButton.removeAttr('id');
    deleteButton.addClass('deleteButton');
    cloneSlide.find('.title').text(title + " (" + release_year + ")");
    cloneSlide.find('.rating').text(vote_average);
    cloneSlide.find('.plot').text(overview);
    cloneSlide.insertAfter('#templateSlide');
    $(cloneSlide).fadeOut(0);
}

function refreshPage() {
    if (thumbsToLoad <= 1) {
        indexElements();
        showThumbs(1);
        showSlides(1);
        $('.loaderPage').fadeOut(FADEOUT_TIME, function () {
            removeHidden();
        });
    } else {
        thumbsToLoad--;
    }
}

function indexElements() {
    slides = document.getElementsByClassName('slide');
    thumbs = document.getElementsByClassName('thumb');
    let i;
    let slide = 0;
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
    for (i = 0; i < thumbs.length; i++) {
        thumb++;
        thumbs[i].setAttribute('thumbId', thumb + '');
        let id = thumbs[i].getAttribute('id');
        if (id !== "newThumb" && id !== "infoThumb") {
            createEventListenerThumb(thumb);
        }
    }
}

function createEventListenerPoster(i, element) {
    let poster = slides[i].getElementsByClassName('poster');
    $(poster[0]).off('click');
    return $(poster[0]).on('click', function () {
        pushListMoviePage(element)
    })
}

function createEventListenerTitle(i, element) {
    let title = slides[i].getElementsByClassName('title');
    $(title[0]).off('click');
    return $(title[0]).on('click', function () {
        pushListMoviePage(element);
    })
}

function createEventListenerDeleteButton(i, element) {
    let button = slides[i].getElementsByClassName('deleteButton');
    $(button[0]).off('click');
    return $(button[0]).on('click', function () {
        deleteSlide(element);
    })
}

function createEventListenerThumb(thumb) {
    $(thumbs[thumb - 1]).off('click');
    return $(thumbs[thumb - 1]).on('click', function () {
        currentThumb(thumb);
    })
}

function showThumbs(thumb) {
    let visibleThumbs = 0;
    for (let i = 0; i < thumbs.length; i++) {
        let thumbId = thumbs[i].getAttribute("thumbId");
        if (thumbId >= thumb && thumbId < (thumb + displayItems)) {
            thumbs[i].style.display = "grid";
            visibleThumbs++;
        } else {
            thumbs[i].style.display = "none";
        }
    }
    // Set width of info thumb depending of amount of movie thumbs are visible
    let width = getComputedStyle(root).getPropertyValue("--newThumbWidth");
    let maxWidth = getComputedStyle(root).getPropertyValue("--newThumbMaxWidth");
    if (visibleThumbs === 0) {
        root.style.setProperty("--infoThumbWidth", "calc(" + (displayItems - 1) + " *" + width + ")");
        root.style.setProperty("--infoThumbMaxWidth", "calc(" + (displayItems - 1) + " *" + maxWidth + ")");
    } else {
        root.style.setProperty("--infoThumbWidth", "calc(" + ((displayItems + 1) - visibleThumbs) + " *" + width + ")");
        root.style.setProperty("--infoThumbMaxWidth", "calc(" + ((displayItems + 1) - visibleThumbs) + " *" + maxWidth + ")");
    }
    showThumbAnimation(thumbs);
}

function showSlides(slide) {
        let row;
        let move;
        let nextRow = 1;
        let rows = getRows();
        let animationDuration = 1000;
        for (let i = 0; i < slides.length; i++) {
            let slideId = slides[i].getAttribute("slideId");
            if (pageLoad) {
                $(slides[i]).fadeIn(animationDuration);
            } else {
                if (slide <= slides.length) {
                    if (parseInt(slideId) === parseInt(slide)) {
                        move = rows[i] - 1;
                        row = 1;
                        slides[i].style.gridRow = row;
                    } else {
                        nextRow++;
                        move = rows[i] - nextRow;
                        row = nextRow;
                        slides[i].style.gridRow = row;
                    }
                    $(slides[i]).fadeIn(animationDuration);
                    showSlideAnimation(i, move, animationDuration);
                }
            }
            pageLoad = false;
        }
}

function deleteSlide(slide) {

    let deleteThumb = thumbs[slide - 1];
    let deleteSlide = slides[slide - 1];

    getCurrentList();
    promiseCurrentList.then(data => {
        if (data) {
            let id = deleteSlide.getAttribute('movieId');
            for (let i = 0; i < privateListItems.length; i++) {
                if (privateListItems[i].id === id) {
                    privateListItems = data.listItems;
                    privateListItems.splice(i, 1);
                    let listTitle = data.title;
                    let listItems = JSON.stringify(privateListItems);
                    updatePrivateList(listTitle, listItems);
                    alertSuccess("Deleted movie from \"" + listTitle + "\"", shortTimeOut);
                }
            }
        } else {
            let id = deleteSlide.getAttribute('movieId');
            for (let i = 0; i < publicListItems.length; i++) {
                if (publicListItems[i].id === id) {
                    publicListItems.splice(i, 1);
                    let listTitle = "movieList";
                    updatePublicList(listTitle, publicListItems);
                    alertSuccess("Deleted movie from MyWatchlist", shortTimeOut);
                }
            }
        }

        let row;
        let move;
        let nextRow = 0;
        let rows = getRows();
        let animationDuration = 1000;
        for (let i = 0; i < slides.length; i++) {
            if (slide <= slides.length) {
                if (slides[i] === deleteSlide) {
                    move = rows[i] - (slides.length);
                    row = slides.length;
                    slides[i].style.gridRow = row;
                    $(slides[i]).css('z-index', -1);
                    $(slides[i]).fadeOut(animationDuration, function () {
                        deleteThumb.remove();
                        deleteSlide.remove();
                        refreshPage();
                    });
                    $(deleteThumb).fadeOut(animationDuration);
                } else {
                    nextRow++;
                    move = rows[i] - nextRow;
                    row = nextRow;
                    slides[i].style.gridRow = row;
                }
                removeSlideAnimation(i, move, animationDuration);
            }
        }
    });
}

function currentThumb(thumb) {
    slideIndex = thumb;
    showSlides(slideIndex);
}

function nextThumb(thumb) {
    let currentThumb = thumbIndex;
    currentThumb += (thumb * displayItems);
    currentThumb = clamp(currentThumb, 1, thumbs.length - 1)
    if (currentThumb !== thumbIndex) {
        thumbIndex = currentThumb;
        showThumbs(thumbIndex);
    } else {
        alertFailure("Reached end of list", longTimeOut);
    }
}

function removeHidden() {
    let hidden = document.getElementsByClassName("un-hide");
    for (let i = 0; i < hidden.length; i++) {
        hidden[i].classList.remove("hide");
    }
}

// find current rows for slides
function getRows() {
    let top;
    let rows = [];
    let rowsTop = [];
    for (let i = 0; i < slides.length; i++) {
        top = $(slides[i]).offset().top;
        rowsTop.push(top);
    }
    const rowHeight = Math.min.apply(null, rowsTop);
    for (let i = 0; i < slides.length; i++) {
        top = $(slides[i]).offset().top;
        let row = ((top - rowHeight) / slideHeight) + 1;
        rows.push(Math.round(row));
    }
    return rows;
}

function clamp(val, min, max) {
    return val < min ? min : (val > max ? max : val);
}

$(function () {
    let header = $('.header');
    header.on("mouseover", function () {
        $('#myWatchlistLabel-container').css('opacity', 0);
    })
    header.on("mouseleave", function () {
        $('#myWatchlistLabel-container').css('opacity', 1);
    })
});