history.navigationMode = 'compatible';
jQuery(function () {
    initLogin()
});

const USERNAME_REGEX = /^[a-zA-Z0-9_]{6,12}$/;
const USERPASS_REGEX = /^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{6,12}$/;
const USEREMAIL_REGEX = /^[a-zA-Z0-9.!#$%&''*+/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$/;
const MAX_CHARS = 12; // Take value from above regex

let promiseLoginStatus;
let isLoggedIn;


function initLogin() {
    $("#loginFormUser").attr('maxlength', MAX_CHARS);
    $("#loginFormPass").attr('maxlength', MAX_CHARS);
    $("#accountCreationUser").attr('maxlength', MAX_CHARS);
    $("#accountCreationPass").attr('maxlength', MAX_CHARS);
    $('.loaderLoginBox').fadeOut(0);
    $('.LoginFormReplyText').fadeOut(0);

    getLoginStatus();
    promiseLoginStatus.then(data => {
        if (data) {
            $('#login').fadeOut(0);
            $('#account').removeClass('hide');
        } else {
            $('#login').fadeIn(0);
            $('#account').addClass('hide');
            // publicListItems = getPublicListItems();
        }
    });
}

function getLoginStatus() {
    promiseLoginStatus = new Promise((resolve, reject) => {
        const url = '/getLoginStatus';
        $.getJSON(url, data => {
            resolve(data);
        });
    });
}

function showLoginMessage(msg, color) {
    let messageContainer = $('.loginFormMessage');
    messageContainer.text(msg);
    messageContainer.css('color', color);
    messageContainer.fadeIn(FADEIN_TIME);
}

function hideLoginMessage() {
    $('.loginFormMessage').fadeOut(0);
}

function validateUserName(username) {
    return USERNAME_REGEX.test(username);
}

function validatePassword(password) {
    return USERPASS_REGEX.test(password);
}

function validateEmail(email) {
    return USEREMAIL_REGEX.test(email);
}

$(function () {
    $('#login').on('click', function () {
        hideLoginMessage();
        $('.loginForm').addClass('showLoginForm');
    })
});

$(function () {
    $('#closeLoginForm').on('click', function () {
        hideLoginMessage();
        $('.loaderLoginBox').fadeOut(FADEOUT_TIME);
        document.querySelector('#loginFormUser').value = "";
        document.querySelector('#loginFormPass').value = "";
        $('.loginForm').removeClass('showLoginForm');
    })
});

$(function () {
    $('#refreshLoginForm').on('click', function () {
        hideLoginMessage();
        $('.loaderLoginBox').fadeOut(FADEOUT_TIME);
        $('.LoginFormReplyText').fadeOut(FADEOUT_TIME, function () {
            document.querySelector('#accountCreationUser').value = "";
            document.querySelector('#accountCreationPass').value = "";
            document.querySelector('#accountCreationEmail').value = "";
            $('.loginFormText').fadeIn(FADEIN_TIME);
        })
    })
});

$(function () {
    $('#loginFormSubmit').on('click', function () {
        hideLoginMessage();
        let isFilledOut = false;
        if (document.querySelector('#loginFormUser').value !== "" &&
            document.querySelector('#loginFormPass').value !== "") {
            isFilledOut = true;
        }
        if (isFilledOut) {
            let userName = document.querySelector('#loginFormUser').value;
            let userPass = document.querySelector('#loginFormPass').value;

            if (!validateUserName(userName)) {
                showLoginMessage("The provided username is not valid", alertFailureColor);
            } else if (!validatePassword(userPass)) {
                showLoginMessage("The provided password is not valid", alertFailureColor);
            } else {
                let url = "/login";
                let parameters = {
                    userName: userName,
                    userPass: userPass
                };

                $.getJSON(url, parameters, printUser);
                $('.loaderLoginBox').fadeIn(FADEIN_TIME);

                function printUser(responseJSON) {
                    $('.loaderLoginBox').fadeOut(FADEOUT_TIME);
                    setTimeout(function () {
                        if (responseJSON == true) {
                            $('#login').fadeOut(0);
                            $('#account').removeClass('hide');
                            $('#account').fadeIn(0);
                            showLoginMessage('Login Successful', alertSuccessColor);
                            setTimeout(function () {
                                $('#closeLoginForm').trigger("click");
                                importPublicList();
                            }, shortTimeOut);
                        } else if (responseJSON == false) {
                            showLoginMessage('Login failed. Please verify your login credentials.', alertFailureColor);
                        } else {
                            let msg = responseJSON["msg"];
                            showLoginMessage(msg, alertFailureColor);
                        }
                    }, shortTimeOut);
                }
            }
        } else {
            showLoginMessage('Not all fields are filled out.', alertFailureColor);
        }
    })
});

$(function () {
    $('#loginFormCreate').on('click', function () {
        setTimeout(function () {
            hideLoginMessage();
            $('.loginFormText').fadeOut(FADEOUT_TIME, function () {
                document.querySelector('#accountCreationUser').value = document.querySelector('#loginFormUser').value;
                document.querySelector('#accountCreationPass').value = document.querySelector('#loginFormPass').value;
                $('.LoginFormReplyText').fadeIn(FADEIN_TIME);
            });
        }, FADEOUT_TIME);
    })
});

$(function () {
    $('#accountCreationSubmit').on('click', function () {
            hideLoginMessage();
            let isFilledOut = false;
            if (document.querySelector('#accountCreationUser').value !== "" &&
                document.querySelector('#accountCreationPass').value !== "" &&
                document.querySelector('#accountCreationEmail').value !== "") {
                isFilledOut = true;
            }
            if (isFilledOut) {
                let userName = document.querySelector('#accountCreationUser').value;
                let userPass = document.querySelector('#accountCreationPass').value;
                let userEmail = document.querySelector('#accountCreationEmail').value;

                if (!validateUserName(userName)) {
                    showLoginMessage("The provided username is not valid", alertFailureColor);
                } else if (!validatePassword(userPass)) {
                    showLoginMessage("The provided password is not valid", alertFailureColor);
                } else if (!validateEmail(userEmail)) {
                    showLoginMessage("The provided email address is not valid", alertFailureColor);
                } else {

                    let url = "/createUser";
                    let parameters = {
                        userName: userName,
                        userPass: userPass,
                        userEmail: userEmail
                    };

                    $.getJSON(url, parameters, printUser);
                    $('.loaderLoginBox').fadeIn(FADEIN_TIME);

                    function printUser(responseJSON) {
                        $('.loaderLoginBox').fadeOut(FADEOUT_TIME);
                        setTimeout(function () {
                            $('loginFormMessage').fadeOut(FADEOUT_TIME);
                            if (responseJSON == true) {
                                showLoginMessage('A verification email has been sent. Please check your email.', alertSuccessColor);
                                setTimeout(function () {
                                    $('#refreshLoginForm').trigger("click");
                                    $('#closeLoginForm').trigger("click");
                                }, longTimeOut * 2.5);
                            } else if (responseJSON == false) {
                                showLoginMessage('Oeps! Something went wrong. Please try again.', alertFailureColor);
                            } else {
                                let msg = responseJSON["msg"];
                                showLoginMessage(msg, alertFailureColor);
                            }
                        }, shortTimeOut);
                    }
                }
            } else {
                showLoginMessage('Not all fields are filled out.', alertFailureColor);
            }
        }
    );
});

$(function () {
    $('#loginVerificationSubmit').on('click', function () {
        hideLoginMessage();
        let isFilledOut = false;
        if (document.querySelector('#loginFormUser').value !== "" &&
            document.querySelector('#loginFormPass').value !== "") {
            isFilledOut = true;
        }
        if (isFilledOut) {
            let userName = document.querySelector('#loginFormUser').value;
            let userPass = document.querySelector('#loginFormPass').value;

            if (!validateUserName(userName)) {
                showLoginMessage("The provided username is not valid", alertFailureColor);
            } else if (!validatePassword(userPass)) {
                showLoginMessage("The provided password is not valid", alertFailureColor);
            } else {

                let url = "/isVerifiedUser";
                let parameters = {
                    userName: userName,
                    userPass: userPass
                };

                $.getJSON(url, parameters, printUser);
                $('.loaderLoginBox').fadeIn(FADEIN_TIME);

                function printUser(data) {
                    $('.loaderLoginBox').fadeOut(FADEOUT_TIME);
                    setTimeout(function () {
                        if (data == true) {
                            showLoginMessage('Registration Successful!', alertSuccessColor)
                            setTimeout(function () {
                                window.location = '/homePage';
                            }, longTimeOut);
                        } else {
                            showLoginMessage('Login failed. Please verify your login credentials.', alertFailureColor);
                        }
                    }, shortTimeOut);
                }
            }
        } else {
            showLoginMessage('Not all fields are filled out.', alertFailureColor);
        }
    })
});

$(function () {
    $('#requestLink').on('click', function () {
        hideLoginMessage();
        let isFilledOut = false;
        if (document.querySelector('#requestLinkEmail').value !== "") {
            isFilledOut = true;
        }
        if (isFilledOut) {
            let userEmail = document.querySelector('#requestLinkEmail').value;
            let url = "/requestLink";
            let parameters = {
                userEmail: userEmail
            };

            $.getJSON(url, parameters, printUser);
            $('.loaderLoginBox').fadeIn(FADEIN_TIME);

            function printUser(responseJSON) {
                $('.loaderLoginBox').fadeOut(FADEOUT_TIME);
                setTimeout(function () {
                    if (responseJSON == true) {
                        $('.formWrapLoginForm').fadeOut(FADEOUT_TIME, function () {
                            $('.loginFormText').find("h1").text("Email sent!");
                            showLoginMessage('A verification link has been sent to your email.', alertSuccessColor);
                        });
                    } else if (responseJSON == false) {
                        showLoginMessage('The email address provided is not valid!', alertFailureColor);
                    }
                }, shortTimeOut);
            }
        } else {
            showLoginMessage('Not all fields are filled out.', alertFailureColor);
        }
    });
});

function importPublicList() {
    if (publicListItems !== null && publicListItems.length !== 0) {
        $('#list').trigger("click");
        $('.listFormText').fadeOut(0);
        $('.listFormReplyText').fadeIn(0);
        $('#refreshListForm').fadeOut(0);
        $('#closeListForm').fadeIn(0);
        $('#listCreationSubmit').fadeOut(0);
        $('#listImportSubmit').fadeIn(0);
        document.querySelector('.listTitle').innerHTML = "Import list";
        document.querySelector('#listCreationName').value = "myWatchlist";
        $('#listCreationDescription').text("My first watchList");
    } else {
        setTimeout(function () {
            location.reload();
        }, shortTimeOut);
    }
}

$(function () {
    $(".togglePassword").on('click', function () {
        $(this).toggleClass("fa-eye fa-eye-slash");
        let input = $($(this).attr("toggle"));
        if (input.attr("type") == "password") {
            input.attr("type", "text");
        } else {
            input.attr("type", "password");
        }
    })
});