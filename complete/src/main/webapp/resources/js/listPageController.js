history.navigationMode = 'compatible';
jQuery(function () {
    initListPage()
});

const displayItems = 5;
const slideHeight = 220; // includes 10px grid border

let thumbIndex = 1;
let slideIndex = 1;
let slides = [];
let thumbs = [];
let thumbsToLoad = 0;

function initListPage() {
    deleteCookie("moviePush");
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
                privateListItems = data.listItems;
            });
        } else {
            $('#login').fadeIn(0);
            $('#account').addClass('hide');
            publicListItems = getPublicListItems();
        }
    buildMovieList();
    });
}

$(function () {
    $('.header').on("mouseover", function () {
        $('#myWatchlistLabel-container').css('opacity', 0);
    })
    $('.header').on("mouseleave", function () {
        $('#myWatchlistLabel-container').css('opacity', 1);
    })
});

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

                    // let url = "/getListFromUser";
                    // let parameters = {
                    //     listTitle: listTitle
                    // };
                    //
                    // $.getJSON(url, parameters, returnStatus);
                    //
                    // function returnStatus(data) {
                        let listItems = data.listItems;
                        if (listItems.length !== 0) {
                            thumbsToLoad += listItems.length;
                            for (let i = 0; i < listItems.length; i++) {
                                privateListItems.push(listItems[i]);
                                let title = listItems[i].title;
                                let year = listItems[i].release_year;
                                retrieveSearch(title, year, 'update');
                            }
                        } else {
                            $('.loaderPage').fadeOut(FADEOUT_TIME, function () {
                                alertSuccess("Welcome! Try making your new list", longTimeOut);
                                removeHidden();
                            });
                        }
                    // }
                } else {
                    $('.loaderPage').fadeOut(FADEOUT_TIME);
                    removeHidden();
                    $('#myWatchlistLabel-container').find('div').text("myWatchlist: a simple list to track my movies");
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
            } else if (publicListItems.length == 0) {
                $('.loaderPage').fadeOut(FADEOUT_TIME, function () {
                    alertSuccess("Welcome! Try making your new list", longTimeOut);
                    removeHidden();
                });
            } else {
                thumbsToLoad += publicListItems.length;
                for (let i = 0; i < publicListItems.length; i++) {
                    let title = publicListItems[i].title;
                    let year = publicListItems[i].release_year;
                    retrieveSearch(title, year, 'update');
                }
            }
            $('#myWatchlistLabel-container').find('div').text("myWatchlist: a simple list to track my movies");
        }
    });
}

function processSearch(search, type, listTitle) {
    // check search identifier for null
    if (search.returnValue == '-1') {
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
        } else if (type === 'update') {
            // executed on list page reload
            addThumb();
            addSlide();
            updateList();
        }
    }
}

function addThumb() {
    const {poster_url} = search.results;
    let cloneThumb = $('#templateThumb').clone(true, true);
    cloneThumb.removeAttr('id');
    cloneThumb.removeClass('hide');
    cloneThumb.addClass('thumb');
    cloneThumb.find('img').attr('src', poster_url);
    cloneThumb.insertAfter('#templateThumb');
}

// below "argument type string not assignable to parameter type Jquery" appeared in 2020.1 version of IntelliJ?
function addSlide() {
    const {vote_average, overview, release_year, poster_url, id, title} = search.results;
    let cloneSlide = $('#templateSlide').clone(true, true);
    cloneSlide.removeAttr('id');
    cloneSlide.attr('name', id);
    cloneSlide.removeClass('hide');
    cloneSlide.addClass('slide');
    cloneSlide.find('.poster').find('img').attr('src', poster_url);
    let deleteButton = cloneSlide.find('#templateDeleteButton');
    deleteButton.removeAttr('id');
    deleteButton.addClass('deleteButton');
    // update slide info
    cloneSlide.find('.title').text(title + " (" + release_year + ")");
    cloneSlide.find('.rating').text(vote_average);
    cloneSlide.find('.plot').text(overview);
    cloneSlide.insertAfter('#templateSlide');
}

function updateList() {
    if (thumbsToLoad === 1) {
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
    // find elements
    slides = document.getElementsByClassName('slide');
    thumbs = document.getElementsByClassName('thumb');
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
    // set element id used by thumb slideshow and create event listeners
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

// Show and hide thumbs
function showThumbs(thumb) {
    let i;
    let visibleThumbs = 0;
    for (i = 0; i < thumbs.length; i++) {
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
    root.style.setProperty("--infoThumbWidth", "calc(" + ((displayItems + 1) - visibleThumbs) + " *" + width + ")");
    root.style.setProperty("--infoThumbMaxWidth", "calc(" + ((displayItems + 1) - visibleThumbs) + " *" + maxWidth + ")");
    showThumbAnimation(thumbs);
}

// Cycle position of slides
function showSlides(slide) {
    let i;
    let row;
    let move;
    let nextRow = 1;
    let rows = getRows();
    for (i = 0; i < slides.length; i++) {
        let slideId = slides[i].getAttribute("slideId");
        if (slide <= slides.length) {
            if (slideId == slide) { //do not change to ===!!
                move = rows[i] - 1;
                row = 1;
                slides[i].style.gridRow = row;
            } else {
                nextRow++;
                move = rows[i] - nextRow;
                row = nextRow;
                slides[i].style.gridRow = row;
            }
            showSlideAnimation(i, move);
        }
    }
}

function deleteSlide(slide) {
    getCurrentList();
    promiseCurrentList.then(data => {
        // promiseLoginStatus.then(data => {
        if (data) {
            let id = slides[slide - 1].getAttribute('name');
            for (let i = 0; i < privateListItems.length; i++) {
                if (privateListItems[i].id === id) {
                    privateListItems.splice(i, 1);
                    let listTitle = data.title;
                    let listItems = JSON.stringify(privateListItems);
                    updatePrivateList(listTitle, listItems);
                    alertSuccess("Successfully deleted movie from \"" + listTitle + "\"", alertSuccessColor);
                }
            }
        } else {
            let id = slides[slide - 1].getAttribute('name');
            for (let i = 0; i < publicListItems.length; i++) {
                if (publicListItems[i].id === id) {
                    publicListItems.splice(i, 1);
                    let listTitle = "movieList";
                    let listItems = publicListItems;
                    updatePublicList(listTitle, listItems);
                    alertSuccess("Successfully deleted movie", shortTimeOut);
                }
            }
        }
        let deleteThumb = thumbs[slide - 1];
        let deleteSlide = slides[slide - 1];
        $(deleteThumb).fadeOut(FADEOUT_TIME, function () {
            deleteThumb.remove();
        });
        $(deleteSlide).fadeOut(FADEOUT_TIME, function () {
            deleteSlide.remove();
            updateList();
        });
    });
}

// Thumbnail controls
function currentThumb(thumb) {
    if (thumb !== slideIndex) {
        slideIndex = thumb;
        showSlides(slideIndex);
    }
}

// slideshow controls
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
    let i;
    let hidden = document.getElementsByClassName("un-hide");
    for (i = 0; i < hidden.length; i++) {
        hidden[i].classList.remove("hide");
    }
}

// find current rows for slides
function getRows() {
    let i;
    let top;
    let rows = [];
    let rowsTop = [];
    for (i = 0; i < slides.length; i++) {
        top = $(slides[i]).offset().top;
        rowsTop.push(top);
    }
    const rowHeight = Math.min.apply(null, rowsTop);
    for (i = 0; i < slides.length; i++) {
        top = $(slides[i]).offset().top;
        let row = ((top - rowHeight) / slideHeight) + 1;
        rows.push(Math.round(row));
    }
    return rows;
}

function clamp(val, min, max) {
    return val < min ? min : (val > max ? max : val);
}