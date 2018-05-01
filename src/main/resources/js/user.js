$(document).ready(function () {
    $("#print").click(function () {
        $('#list').submit();
    });

    $(".delete").click(function () {
        var response = confirm("Are you sure to delete the record?");
        if (response) {
            window.location.href = ctx + "/user/delete/" + $(this).attr("id");
        }
    });
});