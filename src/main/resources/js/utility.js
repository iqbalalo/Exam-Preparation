function getBirthDateFromAge(age) {
    //age must be string type with year and month sperated by '.'. Example: 10/6 (10 Years 6 Months)
    var ageArray = age.split('.');
    var year = 0;
    var month = 0;

    $.each(ageArray, function (index, value) {
        if (index === 0) {
            year = parseInt(value);
        }
        if (index === 1) {
            month = parseInt(value);
        }
    });
    month = (year * 12) + month;  //Convert year and month to only month;

    var curDate = new Date();
    curDate.setMonth(curDate.getMonth() - month);

    var resultYear = (curDate).getFullYear();
    var resultMonth = (curDate).getMonth() + 1;
    var resultDay = (curDate).getDate();

    return(resultDay + "/" + resultMonth + "/" + resultYear);
}

function getContextPath() {
    return window.location.pathname.substring(0, window.location.pathname.indexOf("/", 2));
}