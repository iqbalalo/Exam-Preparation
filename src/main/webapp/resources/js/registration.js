$(document).ready(function () {
    $(".delete").click(function () {
        var response = confirm("Are you sure to delete the record?");

        if (response) {
            url = ctx + "/registration/delete/" + $(this).attr("id");
            window.location.href = url;
        }
    });
});