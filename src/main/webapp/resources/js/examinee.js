$(document).ready(function () {
    var currentQ = 0;
    var examTime = 120 * 60;
    var timeTaken = 0;
    var userAnswers = new Array();
    var objJSON;
    var finish = false;


    function countDown(sec) {
        $("#examTime").html(secondsToTimeFormat(sec));
    }

    interval = setInterval(function () {
        if (examTime === 0) {
            clearInterval(interval);
        }
        countDown(examTime);
        examTime--;
        timeTaken++;
    }, 1000);

    function secondsToTimeFormat(sec) {
        var seconds = sec;
        // multiply by 1000 because Date() requires miliseconds
        var date = new Date(seconds * 1000);
        var hh = date.getUTCHours();
        var mm = date.getUTCMinutes();
        var ss = date.getSeconds();
        // If you were building a timestamp instead of a duration, you would uncomment the following line to get 12-hour (not 24) time
        // if (hh > 12) {hh = hh % 12;}
        // These lines ensure you have two-digits
        if (hh < 10) {
            hh = "0" + hh;
        }
        if (mm < 10) {
            mm = "0" + mm;
        }
        if (ss < 10) {
            ss = "0" + ss;
        }
        // This formats your string to HH:MM:SS
        var t = hh + ":" + mm + ":" + ss;
        return t;
    }

    function loadAnswerPaper(exam, id, qSl) {
        $(".loading").toggleClass("hide");
        console.log("Load Exam");
        console.log("url: " + ctx + "/mcq/modeltest/question");
        console.log("exam: " + exam);
        console.log("id: " + id);
        console.log("qSl: " + qSl);
        $.ajax({
            type: "POST",
            url: ctx + "/mcq/modeltest/question",
            data: {'id': id, 'exam': exam, 'qSl': qSl},
            success: function (data) {
                $(".loading").toggleClass("hide");
                console.log(data);
                $("#questionHolder").html(data);
            }
        });
    }

    loadAnswerPaper(exam, mcqIds[currentQ], currentQ + 1);

    $("#btnNext").click(function () {
        memoriseUserAnswer();
        if (currentQ < 99) {
            currentQ++;
            console.log("Current Q " + currentQ);
            loadAnswerPaper(exam, mcqIds[currentQ], currentQ + 1);
        }
    });

    $("#btnFirst").click(function () {
        memoriseUserAnswer();
        currentQ = 0;
        console.log("Current Q " + currentQ);
        loadAnswerPaper(exam, mcqIds[currentQ], currentQ + 1);
    });

    $("#btnPrevious").click(function () {
        memoriseUserAnswer();
        if (currentQ > 0) {
            currentQ--;
            console.log("Current Q " + currentQ);
            loadAnswerPaper(exam, mcqIds[currentQ], currentQ + 1);
        }
    });
    $("#btnFinish").click(function () {
        $(".loading").toggleClass("hide");
        memoriseUserAnswer();
        var response = confirm("Are you sure to finish the exam?");
        if (response) {
            convertArrayToJson();

            timeTaken = secondsToTimeFormat(timeTaken);

            $.ajax({
                type: "POST",
                url: ctx + "/mcq/modeltest/finish",
                data: {'exam': exam, 'subject': subject, 'answers': objJSON, 'totalMark': 100, 'timeTaken': timeTaken},
                success: function (data) {
                    console.log(data);
                    $(".loading").toggleClass("hide");
                    if (data === "success") {
                        finish = true;
                        url = ctx + "/examinee_home";
                        window.location.replace(url);
                    }
                    else if (data === "") {
                        alert("Error!");
                    }
                    else {
                        alert(data);
                    }
                }
            });
        }
    });

    $("#btnCancel").click(function () {
        var response = confirm("Are you sure to cancel the exam?");
        if (response) {
            finish = true;
            url = ctx + "/examinee_home";
            window.location.replace(url);
        }
    });

    function Answer(_qId, _answer) {
        this.qId = _qId;
        this.answer = _answer;
    }

    function convertArrayToJson() {
        var i, n = userAnswers.length;
        var tmp = new Array();
        for (i = 0; i < n; i++)
        {
            tmp[i] = new Answer(userAnswers[i][0], userAnswers[i][1]);
        }
        var mObj = new Object;
        mObj.userAnswers = tmp;
        objJSON = JSON.stringify(tmp);
        console.log(objJSON);
        return objJSON;
    }

    function memoriseUserAnswer() {
        $(":radio").each(function () {
            if (this.checked) {
                userAnswers.push([mcqIds[currentQ], $(this).val()]);
            }
        });

    }

    window.onbeforeunload = function () {
        if (finish === false) {
            return "You should not refresh the page now. Please press finish if your exam has finished!";
        }
    }
});