document.addEventListener("readystatechange", () => {
    if (document.readyState == "complete") initMoviePage();
});

function initMoviePage() {
    buildMoviePage();
}

function removeHidden() {
    let i;
    let hidden = document.getElementsByClassName("un-hide");
    for (i = 0; i < hidden.length; i++) {
        hidden[i].classList.remove("hide");
    }
}

function loadMoviePage(type) {
    if (type == 'search') {
        let header = $('.header');
        let aside = $('.aside');
        let main = $('.main');
        header.find('img').attr('src', movieBackDrop);
        aside.find('.poster').find('img').attr('src', moviePoster);
        main.find('.title').text(movieTitle + " (" + movieYear + ")");
        main.find('.rating').text(movieRating);
        main.find('.plot').text(moviePlot);
    } else if (type == 'cast') {
        let i;
        let crewDirectors = document.getElementsByClassName('crewDirector');
        let crewWriters = document.getElementsByClassName('crewWriter');
        let castNames = document.getElementsByClassName('castName');
        let castPosters = document.getElementsByClassName('castPoster');

        for (i = 0; i < crewDirectors.length; i++) {
            $(crewDirectors[i]).text(movieDirectors[i]);
        }
        for (i = 0; i < crewWriters.length; i++) {
            $(crewWriters[i]).text(movieWriters[i]);
        }
        for (i = 0; i < castNames.length; i++) {
            $(castNames[i]).text(movieCastNames[i]);
        }
        for (i = 0; i < castPosters.length; i++) {
            castPosters[i].setAttribute('src', movieCastPosters[i]);
            castPosters[i].setAttribute('title', movieCastNames[i]);
        }
    }
    removeHidden();
}