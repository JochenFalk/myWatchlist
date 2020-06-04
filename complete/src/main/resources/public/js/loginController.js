// Replacing Jquery elements with constants breaks the code??
// set login form "click" event handlers
$(function () {
    $('#login').on('click', function () {
        $('.loginForm').addClass('showLoginForm');
    })
});

$(function () {
    $('#closeLoginForm').on('click', function () {
        document.querySelector('#loginFormUser').value = "";
        document.querySelector('#loginFormPass').value = "";
        $('.loginForm').removeClass('showLoginForm');
    })
});

$(function () {
    $('#refreshLoginForm').on('click', function () {
        $('.LoginFormReplyText').fadeOut(FADEOUT_TIME, function () {
            document.querySelector('#accountCreationUser').value = "";
            document.querySelector('#accountCreationPass').value = "";
            document.querySelector('#accountCreationEmail').value = "";
            $('.LoginFormReplyText').display = "none";
            $('.loginFormText').display = "block";
            $('.loginFormText').fadeIn(FADEIN_TIME);
        })
    })
});

$(function () {
    $('#loginFormSubmit').on('click', function () {
        let isFilledOut = false;
        if (document.querySelector('#loginFormUser').value !== "" &&
            document.querySelector('#loginFormPass').value !== "") {
                isFilledOut = true;
            }
        if (isFilledOut) {
            // alertSuccess("Logging in user", shortTimeOut);
            let userName = document.querySelector('#loginFormUser').value;
            let userPass = document.querySelector('#loginFormPass').value;
            let url = "/isExistingUser";
            let parameters = {
                userName: userName,
                userPass: userPass
            };
            $.getJSON(url, parameters, printUser);

            function printUser(data) {
                if (data == true) {
                    alertSuccess("Login Successful", shortTimeOut);
                    setTimeout(function () {
                        $('#closeLoginForm').click()
                    }, FADEOUT_TIME);
                } else {
                    alertFailure("Login failed. Please verify your login credentials", longTimeOut);
                }
            }
        } else {
            alertFailure("Not all fields are filled out", longTimeOut);
        }
    })
});

$(function () {
    $('#loginFormCreate').on('click', function () {
        setTimeout(function () {
            $('.loginFormText').fadeOut(FADEOUT_TIME, function () {
                document.querySelector('#accountCreationUser').value = document.querySelector('#loginFormUser').value;
                document.querySelector('#accountCreationPass').value = document.querySelector('#loginFormPass').value;
                $('.loginFormText').display = "none";
                $('.LoginFormReplyText').display = "block";
                $('.LoginFormReplyText').fadeIn(FADEIN_TIME);
            });
        }, FADEOUT_TIME);
    })
});

$(function () {
    $('#accountCreationSubmit').on('click', function () {
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

            function printUser(responseJSON) {
                if (responseJSON == true) {
                    alertSuccess("Account created successfully", shortTimeOut);
                    $('#refreshLoginForm').click();
                } else if ( responseJSON == false) {
                    alertFailure("Oeps! Something went wrong. Please try again", longTimeOut);
                } else {
                    let msg = responseJSON["msg"];
                    alertFailure(msg, longTimeOut);
                }
            }
        } else {
            alertFailure("Not all fields are filled out", longTimeOut);
        }
    });
});