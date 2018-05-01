/* global ctx */

$(document).ready(function () {
    $(".delete").click(function () {
        var response = confirm("Are you sure to delete the record?");
        if (response) {
            window.location.href = ctx + "/contact/delete/" + $(this).attr("id");
        }
    });

    $("#assignedAsUser").click(function () {
        var $this = $(this);
        id = $(this).attr("value");
        siteUrl = ctx + "/contact/assignedAsUser/" + id;

        $(this).children(".fa-refresh").toggleClass("hide");
        $.ajax({
            type: "GET",
            url: siteUrl,
            success: function (data) {
                alert(data);
                $this.children(".fa-refresh").toggleClass("hide");
            }
        });
    });
});