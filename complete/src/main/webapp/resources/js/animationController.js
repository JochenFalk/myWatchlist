function searchResultAnimation() {
    // Get values for animation
    let imageContainer = document.getElementById('searchPoster');
    let widthSearchBox = $('.searchBox-Inner').width();
    let widthSearchPoster = $('.searchBoxPoster').width();
    let translation = widthSearchBox - widthSearchPoster;
    // Set animation parameters
    let timings = {
        easing: "ease-in-out",
        iterations: 1,
        direction: "normal",
        fill: "both",
    };
    // Perform animation
    timings.delay = 20;
    timings.duration = 600;
    imageContainer.animate([
        {transform: "translateX(" + translation + "px)"},
        {transform: "translateX(0)"}
    ], timings);

    timings.delay = 50;
    timings.duration = 600;
    imageContainer.animate([
        {opacity: 0},
        {opacity: 1}
    ], timings);
}

function showThumbAnimation(thumbs) {
    let visibleThumbs = [];
    for (let i = 0; i < thumbs.length; i++) {
        let style = window.getComputedStyle(thumbs[i]);
        let display = style.getPropertyValue('display');
        if (display === "grid") {
            visibleThumbs.push(thumbs[i]);
        }
    }
    let thumb = Array.prototype.slice.call(visibleThumbs);
    let timings = {
        easing: "ease-in-out",
        iterations: 1,
        direction: "normal",
        fill: "both",
    };
    thumb.forEach(function (element, i) {
        timings.delay = i * 75;
        timings.duration = 800;
        element.animate([
            {transform: "translateX(" + (i + 10) * 75 + "px)"},
            {transform: "translateX(0)"}
        ], timings);

        timings.delay = i * 75;
        timings.duration = 800;
        element.animate([
            {opacity: 0.1},
            {opacity: 1}
        ], timings);
    });
}

function showSlideAnimation(i, move, animationDuration) {
    if (!isNaN(move)) {
        let slide = slides[i];
        slide.keyframes = [{
            transform: "translateY(" + move * slideHeight + "px)"
        }, {
            transform: "translateY(0)"
        }];

        slide.animProps = {
            duration: animationDuration,
            easing: "ease-in-out",
            iterations: 1
        };
        // known error caused on resizing of screen due to unknown issue with slideHeight variable?
        //     update: suspect issue is caused by calling animation when "move" == 0
        let animation = slide.animate(slide.keyframes, slide.animProps);
    }
}

function removeSlideAnimation(i, move, animationDuration) {
    let slide = slides[i];
    slide.keyframes = [{
        transform: "translateY(" + move * slideHeight + "px)"
    }, {
        transform: "translateY(0)"
    }];

    slide.animProps = {
        duration: animationDuration,
        easing: "ease-in-out",
        iterations: 1
    };
    let animation = slide.animate(slide.keyframes, slide.animProps);
}

function arrowUpAnimation() {
    // Get values for animation
    let imageContainer = document.getElementById('dropDownButton');
    let widthPlayer = $('#youtube_player').width();
    let timings = {
        easing: "ease-in-out",
        iterations: 1,
        direction: "normal",
        fill: "both",
    };
    // Perform animation
    timings.delay = 0;
    timings.duration = widthPlayer * 1.5;
    imageContainer.animate([
        {transform: "rotate(0deg)"},
        {transform: "rotate(180deg)"}
    ], timings);
}

function arrowDownAnimation() {
    // Get values for animation
    let imageContainer = document.getElementById('dropDownButton');
    let widthPlayer = $('#youtube_player').width();
    let timings = {
        easing: "ease-in-out",
        iterations: 1,
        direction: "normal",
        fill: "both",
    };
    // Perform animation
    timings.delay = 0;
    timings.duration = widthPlayer * 1.5;
    imageContainer.animate([
        {transform: "rotate(180deg)"},
        {transform: "rotate(0deg)"}
    ], timings);
}