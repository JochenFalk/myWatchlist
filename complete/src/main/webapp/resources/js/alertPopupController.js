const alertSuccessColor = "rgba(67, 160, 71, 1)";
const alertFailureColor = "rgba(198, 40, 40, 1)";
const root = document.documentElement;
const quickTimeOut = 750;
const shortTimeOut = 2000;
const longTimeOut = 3000;
const FADEOUT_TIME = 250;
const FADEIN_TIME = 1350;

let isSuccess = false;
let isFailure = false;

function alertSuccess(messageString, timeOut) {

    let thisAlert = $("#notifyType");

    if (!isSuccess) {
        root.style.setProperty("--backgroundColor", alertSuccessColor);
        thisAlert.text(messageString);
        thisAlert.toggleClass("success");
        $(".notify").toggleClass("active");
        isSuccess = true;

        setTimeout(function () {
            $(".notify").removeClass("active");
            thisAlert.removeClass("success");
            isSuccess = false;
        }, timeOut);
    }
}

function alertFailure(messageString, timeOut) {

    let thisAlert = $("#notifyType");

    if (!isFailure) {
        root.style.setProperty("--backgroundColor", alertFailureColor);
        thisAlert.text(messageString);
        thisAlert.addClass("failure");
        $(".notify").addClass("active");
        isFailure = true;

        setTimeout(function () {
            $(".notify").removeClass("active");
            $("#notifyType").removeClass("failure");
            isFailure = false;
        }, timeOut);
    }
}
