$(document).ready(function () {

    $(".delete").click(function () {
        var response = confirm("Are you sure to delete the record?");

        var id = $(this).attr("id");
        id = id.substr(4);

        console.log(id);

        if (response) {
            window.location.href = ctx + "/mcq/delete/" + id;
        }
    });

});