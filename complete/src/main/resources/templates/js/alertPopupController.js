const alertSuccessColor = "rgba(67, 160, 71, 1)";
const alertFailureColor = "rgba(198, 40, 40, 1)";
const root = document.documentElement;
const shortTimeOut = 1500;
const longTimeOut = 2500;
const FADEOUT_TIME = 250;
const FADEIN_TIME = 1350;

let isSuccess = false;
let isFailure = false;

function alertSuccess(messageString, timeOut) {
    if (!isSuccess) {
        root.style.setProperty("--backgroundColor", alertSuccessColor);
        let thisAlert = $("#notifyType");
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
    if (!isFailure) {
        root.style.setProperty("--backgroundColor", alertFailureColor);
        $("#notifyType").text(messageString);
        $(".notify").addClass("active");
        $("#notifyType").addClass("failure");
        isFailure = true;

        setTimeout(function () {
            $(".notify").removeClass("active");
            $("#notifyType").removeClass("failure");
            isFailure = false;
        }, timeOut);
    }
}
