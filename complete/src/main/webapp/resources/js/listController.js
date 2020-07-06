history.navigationMode = 'compatible';
jQuery(function () {
    initList();
});

const MAX_CHARS_NAME = 15;
const MAX_CHARS_DESCRIPTION = 35;

let promiseCurrentList;
let promiseAllListsFromUser;
let customOptions = [];

function initList() {
    $("#listFormList").attr('maxlength', MAX_CHARS_NAME);
    $("#listCreationName").attr('maxlength', MAX_CHARS_NAME);
    $("#listCreationDescription").attr('maxlength', MAX_CHARS_DESCRIPTION);
    $('.loaderListBox').fadeOut(0);
    $('.listFormReplyText').fadeOut(0);
    $('#refreshListForm').fadeOut(0);
    $('#listImportSubmit').fadeOut(0);
}

function addCustomOption(list) {
    let title = list.title;
    if (!customOptions.includes(title)) {
        let cloneOption = $('#templateOption').clone(true, true);
        cloneOption.removeProp('id');
        cloneOption.addClass('delete-option');
        cloneOption.removeClass('hide');
        cloneOption.attr('data-value', title);
        cloneOption.text(title);
        cloneOption.insertAfter('#templateOption');
        customOptions.push(title);
    }
}

function removeCustomOption(list, listTitle) {
    for (let i = 0; i < customOptions.length; i++) {
        if (customOptions[i] === listTitle) {
            customOptions.pop();
        }
    }
    let existingOptions = document.getElementsByClassName('delete-option');
    for (let i = 0; i < existingOptions.length; i++) {
        existingOptions[i].parentNode.removeChild(existingOptions[i]);
    }
    $('#listValue').text("Select a list");
    addCustomOption(list);
}

function addMovieToCurrentList(movie) {
    getCurrentList();
    promiseCurrentList.then(data => {
        const {title, release_year, id} = movie;
        let newMovie = {
            'title': title,
            'release_year': release_year,
            'id': id
        };
        if (data) {
            if (privateListItems !== null && privateListItems.length !== 0) {
                privateListItems.push(newMovie);
                console.log("test1");
            } else {
                console.log(privateListItems);
                privateListItems = [];
                privateListItems.push(newMovie);
                console.log("test2");
            }

            let listTitle = data.title;
            let listItems = JSON.stringify(privateListItems);
            updatePrivateList(listTitle, listItems);
            alertSuccess("Successfully added movie to", alertSuccessColor);
        } else {
            if (publicListItems !== null && publicListItems.length !== 0) {
                publicListItems.push(newMovie);
            } else {
                publicListItems = [];
                publicListItems.push(newMovie);
            }

            let listTitle = "movieList";
            let listItems = publicListItems;
            updatePublicList(listTitle, listItems);
            alertSuccess("Successfully added movie", shortTimeOut);
        }
        resetSearchBox();
        if (slides != null) {
            movieIndex = slides.length + 1;
            addThumb();
            addSlide();
            updateList();
        }
    });
}

function updatePrivateList(listTitle, listItems) {

    let url = "/updateListFromUser";
    let parameters = {
        listTitle: listTitle,
        listItems: listItems
    };

    $.getJSON(url, parameters, returnStatus);

    function returnStatus(data) {
        if (!data) {
            alertFailure("Oeps! An unknown error occurred. Please try again.", alertFailureColor);
        }
    }
}

function updatePublicList(listTitle, listItems) {
    setCookie(listTitle, listItems, NO_END_DATE);
}
// function addMovieToList(objectList, objectMovie) {
//     let url = "/addMovieToList";
//     let parameters = {
//         objectList: objectList,
//         objectMovie: objectMovie
//     };
//
//     $.getJSON(url, parameters, returnStatus);
//
//     function returnStatus(data) {
//         if (data) {
//             alertSuccess("Movie was added to ", alertSuccessColor);
//         } else {
//             alertFailure("Oeps! The movie could not be added to your list :(", alertFailureColor);
//         }
//     }
// }

function setCurrentList(listTitle) {
    let url = "/setCurrentList";
    let parameters = {
        listTitle: listTitle
    };

    $.getJSON(url, parameters, returnStatus);

    function returnStatus(data) {
        return data;
    }
}

function getCurrentList() {
    promiseCurrentList = new Promise((resolve, reject) => {
        let url = '/getCurrentList';

        $.getJSON(url, data => {
            resolve(data);
        })
            .fail(function (data) {
                resolve(null);
            })
    });
}

function getAllListsFromUser() {
    promiseAllListsFromUser = new Promise((resolve, reject) => {
        let url = "/getAllListsFromUser";
        $.getJSON(url, returnStatus);

        function returnStatus(data) {
            resolve(data);
        }
    });
}

function setLoginStatusMessage() {
    getLoginStatus();
    promiseLoginStatus.then(data => {
        if (data) {
            $('#listFormList').css("pointer-events", "auto");
            let listFromCookie = getPublicListItems();
            if (listFromCookie != null) {
                if (listFromCookie.length !== 0) {
                    console.log(listFromCookie);
                    showListMessage("You have a list stored as a cookie. You can import it to your account and the cookie will be deleted.", alertSuccessColor);
                } else {
                    showListMessage("Let's make some new lists :)", alertSuccessColor);
                }
            }
        } else {
            $('#listFormList').css("pointer-events", "none");
            showListMessage("You don't seem to be logged in :( Please login or sign up for a free account to create more lists.", alertFailureColor);
        }
    });
}

function showListMessage(msg, color) {
    let messageContainer = $('.listFormMessage');
    messageContainer.text(msg);
    messageContainer.css('color', color);
    messageContainer.fadeIn(FADEIN_TIME);
}

function hideListMessage() {
    $('.listFormMessage').fadeOut(0);
}

$(function () {
    $('#list').on('click', function () {
        hideListMessage();
        getAllListsFromUser();
        promiseAllListsFromUser.then(data => {
            for (let i = 0; i < data.length; i++) {
                addCustomOption(data[i]);
            }
        });
        setLoginStatusMessage();
        $('.listForm').addClass('showListForm');
    })
});

$(function () {
    $('#closeListForm').on('click', function () {
        hideListMessage();
        $('.loaderListBox').fadeOut(FADEOUT_TIME);
        document.querySelector('#listFormList').value = "";
        $('.listForm').removeClass('showListForm');
    })
});

$(function () {
    $('#refreshListForm').on('click', function () {
        hideListMessage();
        $('.loaderListBox').fadeOut(FADEOUT_TIME);
        $('.listFormReplyText').fadeOut(FADEOUT_TIME, function () {
            document.querySelector('#listCreationName').value = "";
            document.querySelector('#listCreationDescription').value = "";
            $('#listCreationSubmit').fadeIn(0);
            $('#listImportSubmit').fadeOut(0);
            $('.listFormText').fadeIn(FADEIN_TIME);
        })
    })
});

$(function () {
    $('#listFormSubmit').on('click', function () {
        getLoginStatus();
        promiseLoginStatus.then(data => {
            if (data) {
                hideListMessage();
                let isFilledOut = false;
                if (document.querySelector('#listValue').innerHTML !== "Select a list") {
                    isFilledOut = true;
                }
                if (isFilledOut) {
                    let listName = document.querySelector('#listValue').innerHTML;

                    let url = "/getListFromUser";
                    let parameters = {
                        listTitle: listName
                    };

                    $.getJSON(url, parameters, returnStatus);
                    $('.loaderListBox').fadeIn(FADEIN_TIME);

                    function returnStatus(data) {
                        $('.loaderListBox').fadeOut(FADEOUT_TIME);
                        setTimeout(function () {
                            if (data != null) {
                                showListMessage('List retrieved successfully', alertSuccessColor);
                                setTimeout(function () {
                                    setCurrentList(listName);
                                    window.location.href = "/listPage";
                                }, shortTimeOut);
                            } else {
                                showListMessage('Oeps! Your list could not be retrieved. Please try again', alertSuccessColor);
                            }
                        }, shortTimeOut);
                    }
                } else {
                    showListMessage('Not all fields are filled out.', alertFailureColor);
                }
            } else {
                showListMessage('You seem to be logged out :(', alertFailureColor);
            }
        });
    })
});

$(function () {
    $('#listFormDelete').on('click', function () {
        getLoginStatus();
        promiseLoginStatus.then(data => {
            if (data) {
                hideListMessage();
                let isFilledOut = false;
                if (document.querySelector('#listFormList').value !== "") {
                    isFilledOut = true;
                }
                if (isFilledOut) {
                    let listTitle = document.querySelector('#listValue').innerHTML;

                    let url = "/deleteListFromUser";
                    let parameters = {
                        listTitle: listTitle
                    };

                    $.getJSON(url, parameters, returnStatus);
                    $('.loaderListBox').fadeIn(FADEIN_TIME);

                    function returnStatus(data) {
                        $('.loaderListBox').fadeOut(FADEOUT_TIME);
                        setTimeout(function () {
                            if (data == true) {
                                getAllListsFromUser();
                                promiseAllListsFromUser.then(data => {
                                    let userLists = data;
                                    removeCustomOption(userLists, listTitle);
                                    if (userLists.length !== 0) {
                                        publicListItems = userLists[0];
                                        setCurrentList(listTitle);
                                    } else {
                                        setCurrentList("");
                                    }
                                });
                                showListMessage('Your list was successfully deleted', alertSuccessColor);
                            } else {
                                showListMessage('Oeps! Your list could not be deleted. Please try again', alertSuccessColor);
                            }
                        }, shortTimeOut);
                    }
                } else {
                    showListMessage('Not all fields are filled out.', alertFailureColor);
                }
            }
        });
    })
});

$(function () {
    $('#listFormCreate').on('click', function () {
        getLoginStatus();
        promiseLoginStatus.then(data => {
            if (data) {
                setTimeout(function () {
                    hideListMessage();
                    $('.listFormText').fadeOut(FADEOUT_TIME, function () {
                        $('.listFormReplyText').fadeIn(FADEIN_TIME);
                    });
                }, FADEOUT_TIME);
            }
        });
    })
});

$(function () {
    $('#listCreationSubmit').on('click', function () {
            getLoginStatus();
            promiseLoginStatus.then(data => {
                if (data) {
                    hideListMessage();
                    let isFilledOut = false;
                    if (document.querySelector('#listCreationName').value !== "" &&
                        document.querySelector('#listCreationDescription').value !== "") {
                        isFilledOut = true;
                    }
                    if (isFilledOut) {
                        let listTitle = document.querySelector('#listCreationName').value;
                        let listDesc = document.querySelector('#listCreationDescription').value;

                        let url = "/createList";
                        let parameters = {
                            listTitle: listTitle,
                            listDesc: listDesc
                        };

                        $.getJSON(url, parameters, returnStatus);
                        $('.loaderLoginBox').fadeIn(FADEIN_TIME);

                        function returnStatus(data) {
                            $('.loaderListBox').fadeOut(FADEOUT_TIME);
                            setTimeout(function () {
                                $('listFormMessage').fadeOut(FADEOUT_TIME);
                                if (data == true) {
                                    setCurrentList(listTitle);
                                    window.location.href = "/listPage";
                                    showListMessage('Your new list has been created :)', alertSuccessColor);
                                } else if (data == false) {
                                    showListMessage('Oeps! Something went wrong. Please try again.', alertFailureColor);
                                }
                            }, shortTimeOut);
                        }
                    } else {
                        showListMessage('Not all fields are filled out.', alertFailureColor);
                    }
                }
            });
        }
    );
});

$(function () {
    $('#listImportSubmit').on('click', function () {
            getLoginStatus();
            promiseLoginStatus.then(data => {
                if (data) {
                    hideListMessage();
                    let isFilledOut = false;
                    if (document.querySelector('#listCreationName').value !== "" &&
                        document.querySelector('#listCreationDescription').value !== "") {
                        isFilledOut = true;
                    }
                    if (isFilledOut) {
                        let listTitle = document.querySelector('#listCreationName').value;
                        let listDesc = document.querySelector('#listCreationDescription').value;
                        let listItems = getCookie("movieList");

                        let url = "/createListWithItems";
                        let parameters = {
                            listTitle: listTitle,
                            listDesc: listDesc,
                            listItems: listItems
                        };

                        $.getJSON(url, parameters, returnStatus);
                        $('.loaderLoginBox').fadeIn(FADEIN_TIME);

                        function returnStatus(data) {
                            $('.loaderListBox').fadeOut(FADEOUT_TIME);
                            $('listFormMessage').fadeOut(FADEOUT_TIME);
                            if (data == true) {
                                showListMessage('Your list has been imported :)', alertSuccessColor);
                                setTimeout(function () {
                                    setCurrentList(listTitle);
                                    deleteCookie("movieList");
                                    window.location.href = "/listPage";
                                }, shortTimeOut)
                            } else if (data == false) {
                                showListMessage('Oeps! Something went wrong. Please try again.', alertFailureColor);
                            }
                        }
                    } else {
                        showListMessage('Not all fields are filled out.', alertFailureColor);
                    }
                }
            });
        }
    );
});

$(function () {
    $('#listFormList').on('click', function () {
        this.querySelector('.custom-select').classList.toggle('open');
    })
});

window.addEventListener('click', function (e) {
    for (const option of document.querySelectorAll(".custom-option")) {
        option.addEventListener('click', function () {
            if (!this.classList.contains('selected')) {
                this.parentNode.querySelector('.custom-option.selected').classList.remove('selected');
                this.classList.add('selected');
                this.closest('.custom-select').querySelector('.custom-select__trigger span').textContent = this.textContent;
            }
        })
    }
});
