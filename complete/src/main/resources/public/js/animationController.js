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

function showThumbAnimation() {
    // Get values for animation
    let thumb = Array.prototype.slice.call(thumbs);
    // Set animation parameters
    let timings = {
        easing: "ease-in-out",
        iterations: 1,
        direction: "normal",
        fill: "both",
    };
    // Perform animation
    thumb.forEach(function (element, i) {
        timings.delay = i * 5;
        timings.duration = 800;
        element.animate([
            {transform: "translateX(" + (i + 10) * 75 + "px)"},
            {transform: "translateX(0)"}
        ], timings);

        timings.delay = i * 50;
        timings.duration = 600;
        element.parentNode.animate([
            {transform: "rotateY(40deg)"},
            {transform: "rotateY(0deg)"}
        ], timings);

        timings.delay = i * 50;
        timings.duration = 800;
        element.parentNode.animate([
            {transform: "rotateX(-20deg)"},
            {transform: "rotateX(0deg)"}
        ], timings);

        timings.delay = i * 50;
        timings.duration = 800;
        element.animate([
            {opacity: 0.1},
            {opacity: 1}
        ], timings);
    });
}

function showSlideAnimation(i, move) {
    let slide = slides[i];
    slide.keyframes = [{
        transform: "translateY(" + move * slideHeight + "px)"
    }, {
        transform: "translateY(0)"
    }];

    slide.animProps = {
        duration: 1000,
        easing: "ease-in-out",
        iterations: 1
    };
    let animation = slide.animate(slide.keyframes, slide.animProps);
}

function removeSlideAnimation(i, move) {
    let slide = slides[i];
    slide.keyframes = [{
        transform: "translateY(0)"
    }, {
        transform: "translateY(" + move * slideHeight + "px)"
    }];

    slide.animProps = {
        duration: 1000,
        easing: "ease-in-out",
        iterations: 1
    };
    let animation = slide.animate(slide.keyframes, slide.animProps);
}