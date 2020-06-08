document.addEventListener("readystatechange", () => {
    if (document.readyState == "complete") initLogin();
});

const MAX_CHARS = 12; // Take value from "newUserCreationValidator" regex

function initLogin() {
    $("#loginFormUser").attr('maxlength', MAX_CHARS);
    $("#loginFormPass").attr('maxlength', MAX_CHARS);
    $("#accountCreationUser").attr('maxlength', MAX_CHARS);
    $("#accountCreationPass").attr('maxlength', MAX_CHARS);
    $('.loader').fadeOut(0);
    hideMessage();
}

function showMessage(msg, color) {
    let messageContainer = $('.loginFormMessage');
    messageContainer.text(msg);
    messageContainer.css('color', color);
    messageContainer.fadeIn(FADEIN_TIME);
}

function hideMessage() {
    let messageContainer = $('.loginFormMessage');
    messageContainer.fadeOut(0);
}

$(function () {
    $('#login').on('click', function () {
        hideMessage();
        $('.loginForm').addClass('showLoginForm');
    })
});

$(function () {
    $('#closeLoginForm').on('click', function () {
        hideMessage();
        $('.loader').fadeOut(FADEOUT_TIME);
        document.querySelector('#loginFormUser').value = "";
        document.querySelector('#loginFormPass').value = "";
        $('.loginForm').removeClass('showLoginForm');
    })
});

$(function () {
    $('#refreshLoginForm').on('click', function () {
        hideMessage();
        $('.loader').fadeOut(FADEOUT_TIME);
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
        hideMessage();
        let isFilledOut = false;
        if (document.querySelector('#loginFormUser').value !== "" &&
            document.querySelector('#loginFormPass').value !== "") {
            isFilledOut = true;
        }
        if (isFilledOut) {
            let userName = document.querySelector('#loginFormUser').value;
            let userPass = document.querySelector('#loginFormPass').value;
            let url = "/isRegisteredUser";
            let parameters = {
                userName: userName,
                userPass: userPass
            };

            $.getJSON(url, parameters, printUser);
            $('.loader').fadeIn(FADEIN_TIME);

            function printUser(responseJSON) {
                $('.loader').fadeOut(FADEOUT_TIME);
                setTimeout(function () {
                    if (responseJSON == true) {
                        showMessage('Login Successful', alertSuccessColor);
                        setTimeout(function () {
                            $('#closeLoginForm').trigger("click");
                        }, FADEOUT_TIME);
                    } else if (responseJSON == false) {
                        showMessage('Login failed. Please verify your login credentials.', alertFailureColor);
                    } else {
                        let msg = responseJSON["msg"];
                        showMessage(msg, alertFailureColor);
                    }
                }, shortTimeOut);
            }
        } else {
            showMessage('Not all fields are filled out.', alertFailureColor);
        }
    })
});

$(function () {
    $('#loginFormCreate').on('click', function () {
        setTimeout(function () {
            hideMessage();
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
        hideMessage();
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
            let url = "/createUser";
            let parameters = {
                userName: userName,
                userPass: userPass,
                userEmail: userEmail
            };

            $.getJSON(url, parameters, printUser);
            $('.loader').fadeIn(FADEIN_TIME);

            function printUser(responseJSON) {
                $('.loader').fadeOut(FADEOUT_TIME);
                setTimeout(function () {
                    $('loginFormMessage').fadeOut(FADEOUT_TIME);
                    if (responseJSON == true) {
                        showMessage('A verification email has been sent. Please check your email.', alertSuccessColor);
                        setTimeout(function () {
                            $('#refreshLoginForm').trigger("click");
                            $('#closeLoginForm').trigger("click");
                        }, longTimeOut * 2.5);
                    } else if (responseJSON == false) {
                        showMessage('Oeps! Something went wrong. Please try again.', alertFailureColor);
                    } else {
                        let msg = responseJSON["msg"];
                        showMessage(msg, alertFailureColor);
                    }
                }, shortTimeOut);
            }
        } else {
            showMessage('Not all fields are filled out.', alertFailureColor);
        }
    });
});

$(function () {
    $('#loginVerificationSubmit').on('click', function () {
        hideMessage();
        let isFilledOut = false;
        if (document.querySelector('#loginFormUser').value !== "" &&
            document.querySelector('#loginFormPass').value !== "") {
            isFilledOut = true;
        }
        if (isFilledOut) {
            let userName = document.querySelector('#loginFormUser').value;
            let userPass = document.querySelector('#loginFormPass').value;
            let url = "/isVerifiedUser";
            let parameters = {
                userName: userName,
                userPass: userPass
            };

            $.getJSON(url, parameters, printUser);
            $('.loader').fadeIn(FADEIN_TIME);

            function printUser(data) {
                $('.loader').fadeOut(FADEOUT_TIME);
                setTimeout(function () {
                    if (data == true) {
                        showMessage('Registration Successful!', alertSuccessColor)
                        setTimeout(function () {
                            window.location = '/';
                        }, longTimeOut);
                    } else {
                        showMessage('Login failed. Please verify your login credentials.', alertFailureColor);
                    }
                }, shortTimeOut);
            }
        } else {
            showMessage('Not all fields are filled out.', alertFailureColor);
        }
    })
});

$(function () {
    $('#requestLink').on('click', function () {
        hideMessage();
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
            $('.loader').fadeIn(FADEIN_TIME);

            function printUser(responseJSON) {
                $('.loader').fadeOut(FADEOUT_TIME);
                setTimeout(function () {
                    if (responseJSON == true) {
                        $('.formWrapLoginForm').fadeOut(FADEOUT_TIME, function () {
                            $('.loginFormText').find("h1").text("Email sent!");
                            showMessage('Success! A verification link has been sent to your email.', alertSuccessColor);
                        });
                    } else if (responseJSON == false) {
                        showMessage('The email address provided is not valid!', alertFailureColor);
                    }
                }, shortTimeOut);
            }
        } else {
            showMessage('Not all fields are filled out.', alertFailureColor);
        }
    });
});

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