history.navigationMode = 'compatible';
jQuery(function () {
    initAccount();
});

function initAccount() {
    $('.loaderAccountBox').fadeOut(0);
    $('#adminPageButton').fadeOut(0);
}

function showAccountMessage(msg, color) {
    let messageContainer = $('.accountFormMessage');
    messageContainer.text(msg);
    messageContainer.css('color', color);
    messageContainer.fadeIn(FADEIN_TIME);
}

function hideAccountMessage() {
    $('.accountFormMessage').fadeOut(0);
}

$(function () {
    $('#account').on('click', function () {
        hideAccountMessage();
        $('.accountForm').addClass('showAccountForm');
        getRole();
        promiseRole.then(data => {
            if (data.role === "Admin") {
                $('#adminPageButton').fadeIn(0);
            }
        });
    })
});

$(function () {
    $('#closeAccountForm').on('click', function () {
        hideAccountMessage();
        $('.loaderAccountBox').fadeOut(FADEOUT_TIME);
        $('.accountForm').removeClass('showAccountForm');
    })
});

$(function () {
    $('#accountFormSubmit').on('click', function () {

        hideAccountMessage();

        let loader = $('.loaderAccountBox');
        let account = $('#account');
        let url = "/logOut";

        $.getJSON(url, callback);
        loader.fadeIn(FADEIN_TIME);

        function callback(data) {
            loader.fadeOut(FADEOUT_TIME);
            setTimeout(function () {
                if (data === true) {
                    account.fadeOut(0);
                    account.addClass('hide');
                    $('#login').fadeIn(0);
                    showAccountMessage('Log out successful', alertSuccessColor);
                    setTimeout(function () {
                        $('#closeAccountForm').trigger("click");
                        setTimeout(function () {
                            window.location.href = "/homePage";
                        },quickTimeOut);
                    }, shortTimeOut);
                } else if (data === false) {
                    showAccountMessage('Oeps! An unknown error occurred :(', alertFailureColor);
                }
            }, shortTimeOut);
        }
    })
});

$(function () {
    $('#accountFormDelete').on('click', function () {

        hideAccountMessage();

        let loader = $('.loaderAccountBox');
        let account = $('#account');
        let url = "/deleteAccount";

        $.getJSON(url, callback);
        loader.fadeIn(FADEIN_TIME);

        function callback(data) {
            loader.fadeOut(FADEOUT_TIME);
            setTimeout(function () {
                if (data === true) {
                    account.fadeOut(0);
                    account.addClass('hide');
                    $('#login').fadeIn(0);
                    showAccountMessage('Your account has successfully been deleted', alertSuccessColor);
                    setTimeout(function () {
                        $('#closeAccountForm').trigger("click");
                        setTimeout(function () {
                            location.reload();
                        },shortTimeOut);
                    }, shortTimeOut);
                } else if (data === false) {
                    showAccountMessage('Oeps! An unknown error occurred :(', alertFailureColor);
                }
            }, shortTimeOut);
        }
    })
});

$(function () {
    $('#adminPageButton').on('click', function () {
        window.location.href = "/adminPage";
    })
});
