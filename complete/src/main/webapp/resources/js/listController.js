history.navigationMode = 'compatible';
jQuery(function () {
    initList();
});

const MAX_CHARS_NAME = 15;
const MAX_CHARS_DESCRIPTION = 35;

let promiseCurrentList;
let promiseAllListsFromUser;
let customOptions = [];
let reloadPage = false;

function initList() {
    $("#listFormList").attr('maxlength', MAX_CHARS_NAME);
    $("#listCreationName").attr('maxlength', MAX_CHARS_NAME);
    $("#listCreationDescription").attr('maxlength', MAX_CHARS_DESCRIPTION);
    $('.loaderListBox').fadeOut(0);
    $('.listFormReplyText').fadeOut(0);
    $('#refreshListForm').fadeOut(0);
    $('#listImportSubmit').fadeOut(0);
}

function addCustomOptions() {
    getAllListsFromUser();
    promiseAllListsFromUser.then(data => {
        for (let i = 0; i < data.length; i++) {
            let title = data[i].title;
            if (!customOptions.includes(title)) {
                let cloneOption = $('#templateOption').clone(true, true);
                cloneOption.removeAttr('id');
                cloneOption.addClass('delete-option');
                cloneOption.removeClass('hide');
                cloneOption.attr('data-value', title);
                cloneOption.text(title);
                cloneOption.insertAfter('#templateOption');
                customOptions.push(title);
            }
        }
        addEventListeners();
    });
}

function addEventListeners() {
    window.addEventListener('click', function () {
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
}

function removeCustomOption(userLists, listTitle) {

    for (let i = 0; i < userLists.length; i++) {
        if (userLists[i].title !== listTitle) {
            let newListTitle = userLists[i].title;
            setCurrentList(newListTitle);
            alertSuccess(newListTitle + " loaded");
            break;
        } else {
            let newListTitle = "";
            setCurrentList(newListTitle);
        }
    }

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
    $('#default-option').addClass('selected');

    addCustomOptions();
}

function setCurrentList(listTitle) {

    let url = "/setCurrentList";
    let parameters = {
        listTitle: listTitle
    };

    $.getJSON(url, parameters, callback);

    function callback(data) {
        return data;
    }
}

function getCurrentList() {
    promiseCurrentList = new Promise((resolve) => {
        let url = '/getCurrentList';

        $.getJSON(url, data => {
            resolve(data);
        })
            .fail(function () {
                resolve(null);
            })
    });
}

function getAllListsFromUser() {
    promiseAllListsFromUser = new Promise((resolve) => {
        let url = "/getAllListsFromUser";
        $.getJSON(url, callback);

        function callback(data) {
            resolve(data);
        }
    });
}

function addMovieToCurrentList(searchResults) {
    getCurrentList();
    promiseCurrentList.then(data => {
        const {title, release_year, id} = searchResults;
        let newMovie = {
            'title': title,
            'release_year': release_year,
            'id': id
        };
        if (data) {
            if (privateListItems !== null && privateListItems.length !== 0) {
                privateListItems.push(newMovie);
            } else {
                privateListItems = [];
                privateListItems.push(newMovie);
            }
            let listTitle = data.title;
            let listItems = JSON.stringify(privateListItems);
            updatePrivateList(listTitle, listItems);
            saveLastPerformedSearch();
            alertSuccess("Added movie to \"" + listTitle + "\"", shortTimeOut);
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
            alertSuccess("Added movie to MyWatchlist", shortTimeOut);
        }
        resetSearchBox();
        if (slides != null) {
            movieIndex = slides.length + 1;
            addThumb();
            addSlide();
            refreshPage();
        }
    });
}

function updatePrivateList(listTitle, listItems) {

    let url = "/updateListFromUser";
    let parameters = {
        listTitle: listTitle,
        listItems: listItems
    };

    $.getJSON(url, parameters, callback);

    function callback(data) {
        if (!data) {
            alertFailure("Oeps! An unknown error occurred. Please try again.", longTimeOut);
        }
    }
}

function updatePublicList(listTitle, listItems) {
    setCookie(listTitle, listItems, NO_END_DATE);
}

function setOpeningStatusMessage() {
    getLoginStatus();
    promiseLoginStatus.then(data => {
        if (data) {
            $('#listFormList').css("pointer-events", "auto");
            showListMessage("Let's make a new lists :)", alertSuccessColor);
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

function importPublicList() {
    if (publicListItems !== null && publicListItems.length !== 0) {
        $('.listForm').addClass('showListForm');
        $('.listFormText').fadeOut(0);
        $('.listFormReplyText').fadeIn(0);
        $('#refreshListForm').fadeOut(0);
        $('#closeListForm').fadeIn(0);
        $('#listCreationSubmit').fadeOut(0);
        $('#listImportSubmit').fadeIn(0);
        showListMessage("You have a list stored in a cookie. Press the button above to import it to your account.", alertSuccessColor);
        document.querySelector('.listTitle').innerHTML = "Import list";
        document.querySelector('#listCreationName').value = "MyWatchlist";
        $('#listCreationDescription').text("My first watchList");
    }
}

$(function () {
    $('#list').on('click', function () {
        reloadPage = false;
        hideListMessage();
        addCustomOptions();
        setOpeningStatusMessage();
        $('.listForm').addClass('showListForm');
    })
});

$(function () {
    $('#closeListForm').on('click', function () {
        hideListMessage();
        $('.loaderListBox').fadeOut(FADEOUT_TIME);
        document.querySelector('#listFormList').value = "";
        $('.listForm').removeClass('showListForm');
        // reload page in case current list was deleted.
        if (reloadPage === true && window.location.pathname === "/listPage") {
            setTimeout(function () {
                location.reload();
            }, shortTimeOut);
        }
    })
});

$(function () {
    $('#refreshListForm').on('click', function () {
        hideListMessage();
        $('.loaderListBox').fadeOut(FADEOUT_TIME);
        $('#refreshListForm').fadeOut(FADEOUT_TIME);
        $('.listFormReplyText').fadeOut(FADEOUT_TIME, function () {
            document.querySelector('#listCreationName').value = "";
            document.querySelector('#listCreationDescription').value = "";
            $('#listCreationSubmit').fadeIn(0);
            $('#listImportSubmit').fadeOut(0);
            $('.listFormText').fadeIn(FADEIN_TIME);
            $('#closeListForm').fadeIn(FADEIN_TIME);
        })
    })
});

$(function () {
    $('#listFormSubmit').on('click', function () {

        getLoginStatus()
        let loader = $('.loaderListBox');

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

                    $.getJSON(url, parameters, callback);
                    loader.fadeIn(FADEIN_TIME);

                    function callback(data) {
                        loader.fadeOut(FADEOUT_TIME);
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
        let loader = $('.loaderListBox');

        promiseLoginStatus.then(data => {
            if (data) {
                hideListMessage();
                let isFilledOut = false;
                if (document.querySelector('#listFormList').value !== "Select a list") {
                    isFilledOut = true;
                }
                if (isFilledOut) {
                    getCurrentList();
                    promiseCurrentList.then(data => {

                        let currentListTitle = data.title;
                        let listTitle = document.querySelector('#listValue').innerHTML;
                        if (currentListTitle === listTitle) {
                            reloadPage = true;
                        }

                        let url = "/deleteListFromUser";
                        let parameters = {
                            listTitle: listTitle
                        };

                        $.getJSON(url, parameters, callback);
                        loader.fadeIn(FADEIN_TIME);

                        function callback(data) {
                            loader.fadeOut(FADEOUT_TIME);
                            setTimeout(function () {
                                if (data === true) {
                                    getAllListsFromUser();
                                    promiseAllListsFromUser.then(data => {
                                        removeCustomOption(data, listTitle);
                                    });
                                    showListMessage('Your list was successfully deleted', alertSuccessColor);
                                } else {
                                    showListMessage('Oeps! Your list could not be deleted. Please try again', alertSuccessColor);
                                }
                            }, shortTimeOut);
                        }
                    });
                } else {
                    showListMessage('Please select a list to delete.', alertFailureColor);
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
                hideListMessage();
                $('#closeListForm').fadeOut(FADEOUT_TIME);
                $('.listFormText').fadeOut(FADEOUT_TIME, function () {
                    $('.listFormReplyText').fadeIn(FADEIN_TIME);
                    $('#refreshListForm').fadeIn(FADEIN_TIME);
                    showListMessage("Please add a name and description for your list.", alertSuccessColor);
                });
            }
        });
    })
});

$(function () {
    $('#listCreationSubmit').on('click', function () {

            getLoginStatus();
            let loader = $('.loaderListBox');

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

                        $.getJSON(url, parameters, callback);
                        loader.fadeIn(FADEIN_TIME);

                        function callback(responseJSON) {
                            loader.fadeOut(FADEOUT_TIME);
                            setTimeout(function () {
                                if (responseJSON === true) {
                                    setCurrentList(listTitle);
                                    showListMessage('Your new list has been created :)', alertSuccessColor);
                                    setTimeout(function () {
                                        window.location.href = "/listPage";
                                    }, shortTimeOut);
                                } else if (responseJSON === false) {
                                    showListMessage('Oeps! Something went wrong. Please try again.', alertFailureColor);
                                } else {
                                    let msg = responseJSON["msg"];
                                    showListMessage(msg, alertFailureColor)
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
            let loader = $('.loaderListBox');

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

                        $.getJSON(url, parameters, callback);
                        loader.fadeIn(FADEIN_TIME);

                        function callback(data) {
                            loader.fadeOut(FADEOUT_TIME);
                            if (data === true) {
                                setTimeout(function () {
                                    showListMessage('Your list has been imported :)', alertSuccessColor);
                                    setCurrentList(listTitle);
                                    deleteCookie("movieList");
                                    setTimeout(function () {
                                        window.location.href = "/listPage";
                                    }, shortTimeOut);
                                }, shortTimeOut)
                            } else if (data === false) {
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