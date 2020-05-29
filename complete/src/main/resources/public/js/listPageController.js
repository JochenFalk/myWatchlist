document.addEventListener("readystatechange", () => {
    if (document.readyState == "complete") initGallery();
});

const displayItems = 5;
const slideHeight = 220; // includes 10px grid border

let thumbIndex = 1;
let slideIndex = 1;

function initGallery() {
    buildMovieList()
    removeHidden();
    $('.replyText').fadeOut();
    let height = (slideHeight - 10) + "px";
    root.style.setProperty("--slideHeight", height);
    deleteCookie(pushMovie);
    // test!!!
    $('#login').on('click', function () {
        let url = "/user";
        let params = {
            id : 1
        };
        $.getJSON(url, params, printUser);
    });

    function printUser(data) {
        console.log(JSON.stringify(data));
    }
    // test!!!
}

function removeHidden() {
    let i;
    let hidden = document.getElementsByClassName("un-hide");
    for (i = 0; i < hidden.length; i++) {
        hidden[i].classList.remove("hide");
    }
}

function addThumb() {
    let cloneThumb = $('#templateThumb').clone(true, true);
    cloneThumb.removeAttr('id');
    cloneThumb.removeClass('hide');
    cloneThumb.addClass('thumb');
    cloneThumb.find('img').attr('src', moviePoster);
    cloneThumb.insertAfter('#templateThumb');
}

// below "argument type string not assignable to parameter type Jquery" appeared in 2020.1 version of IntelliJ?
function addSlide() {
    let cloneSlide = $('#templateSlide').clone(true, true);
    cloneSlide.removeAttr('id');
    cloneSlide.attr('name', movieId);
    cloneSlide.removeClass('hide');
    cloneSlide.addClass('slide');
    cloneSlide.find('.poster').find('img').attr('src', moviePoster);
    let deleteButton = cloneSlide.find('#templateDeleteButton');
    deleteButton.removeAttr('id');
    deleteButton.addClass('deleteButton');
    // update slide info
    cloneSlide.find('.title').text(movieTitle + " (" + movieYear + ")");
    cloneSlide.find('.rating').text(movieRating);
    cloneSlide.find('.plot').text(moviePlot);
    cloneSlide.insertAfter('#templateSlide');
}

function deleteSlide(slide) {
    let i;
    let id = slides[slide - 1].getAttribute('name');
    for (i = 0; i < publicList.length; i++) {
        if (publicList[i].id == id) {
            publicList.splice(i, 1);
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
        alertSuccess("Successfully deleted movie", shortTimeOut);
    });
}

function pushMoviePage(slide) {
    let i;
    pushList = [];
    let id = slides[slide - 1].getAttribute('name');
    for (i = 0; i < publicList.length; i++) {
        if (publicList[i].id == id) {
            let newPush = {
                'title': publicList[i].title,
                'year': publicList[i].year,
                'rating': publicList[i].rating,
                'release': publicList[i].release,
                'poster': publicList[i].poster,
                'id': publicList[i].id
            };
            pushList.push(newPush);
            setSessionCookie(pushMovie, pushList);
        }
    }
}

// slideshow controls
function nextThumb(thumb) {
    let currentThumb = thumbIndex;
    currentThumb += (thumb * displayItems);
    currentThumb = clamp(currentThumb, 1, thumbs.length - 1)
    if (currentThumb != thumbIndex) {
        thumbIndex = currentThumb;
        showThumbs(thumbIndex);
    } else {
        alertFailure("Reached end of list", longTimeOut);
    }
}

// Thumbnail controls
function currentThumb(thumb) {
    if (thumb != slideIndex) {
        slideIndex = thumb;
        showSlides(slideIndex);
    }
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
    showThumbAnimation();
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