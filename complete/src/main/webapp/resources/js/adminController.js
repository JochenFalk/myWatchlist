history.navigationMode = 'compatible';
jQuery(function () {
    initAdmin()
});

let loadedObject;
let itemIndex;

function initAdmin() {

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
        $('.loaderPage').fadeOut(FADEOUT_TIME);
        $('.adminFormMessage').fadeOut(0);
        $('#templateInput').fadeOut(0);
        publicListItems = getPublicListItems();
        removeHidden();
    });
}

function removeHidden() {
    let i;
    let hidden = document.getElementsByClassName("un-hide");
    for (i = 0; i < hidden.length; i++) {
        hidden[i].classList.remove("hide");
    }
}

function loadAdminBox(index) {

    itemIndex = index;

    for (let i = 0; i < newOptions.length; i++) {
        let label = newOptions[i];
        let value = newQueryResults[newOptions[i]][index]
        let cloneOption = $('#templateInput').clone(true, true);
        cloneOption.removeAttr('id');
        cloneOption.removeClass('hide');
        cloneOption.addClass('delete-input-container');

        if (label === "id" || label === "password" || label === "token" || label === "tokenExpiryDate" || label === "listItems") {
            cloneOption.find('#input').addClass('locked');
        } else {
            cloneOption.find('#input').addClass('input');
        }

        cloneOption.find('#input').text(value);
        cloneOption.find('#input').removeAttr('id');
        cloneOption.find('#label').text(label);
        cloneOption.find('#label').addClass('label');
        cloneOption.find('#label').removeAttr('id');
        cloneOption.insertBefore('#templateInput');
        cloneOption.fadeIn(FADEIN_TIME);

        let width = $('#input').innerWidth();
        let height = (value.toString().length / Math.abs(width) * 3);
        let inputField = $('.adminFormWrap').children('div').children('textarea')[i];
        $(inputField).css('height', height + "px");
    }
}

$(function () {
    $('#closeAdminBox').on('click', function () {
        let existingContainer = document.getElementsByClassName('delete-input-container');
        let i = existingContainer.length;
        while (i--) {
            existingContainer[i].parentNode.removeChild(existingContainer[i]);
        }
        $('.adminBox').removeClass('showAdminBox');
    })
});

$(function () {
    $('#adminBoxDeleteButton').on('click', function () {
        let mappedValues = {};
        let inputContainer = document.getElementsByClassName('delete-input-container');
        for (let i = 0; i < inputContainer.length; i++) {
            let label = $(inputContainer[i]).find('span').text();
            let input = $(inputContainer[i]).find('textarea').val();
            mappedValues[label] = input;
        }

        let url = "/deleteRecord";
        let parameters = {
            mappedValues: JSON.stringify(mappedValues),
            loadedObject: loadedObject
        };

        $.getJSON(url, parameters, function (responseJSON) {
            if (responseJSON === true) {
                alertSuccess("Deletion successful.", shortTimeOut);
            } else {
                let msg = responseJSON["msg"];
                alertFailure(msg, longTimeOut)
            }

        })
            .fail(function () {
                alertFailure('Oeps! An unknown error occurred :(', longTimeOut);
            })
    })
})

$(function () {
    $('#adminBoxSaveButton').on('click', function () {
        let mappedValues = {};
        let inputContainer = document.getElementsByClassName('delete-input-container');
        for (let i = 0; i < inputContainer.length; i++) {
            let label = $(inputContainer[i]).find('span').text();
            let input = $(inputContainer[i]).find('textarea').val();
            mappedValues[label] = input;
        }

        let url = "/saveRecord";
        let parameters = {
            mappedValues: JSON.stringify(mappedValues),
            loadedObject: loadedObject,
            newQueryResults: JSON.stringify(newQueryResults),
            itemIndex: itemIndex
        };

        $.getJSON(url, parameters, function (responseJSON) {
            if (responseJSON === true) {
                alertSuccess("Save successful.", shortTimeOut);
            } else {
                let msg = responseJSON["msg"];
                alertFailure(msg, longTimeOut)
            }

        })
            .fail(function () {
                alertFailure('Oeps! An unknown error occurred :(', longTimeOut);
            })
    })
});